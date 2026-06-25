package com.smartlogix.ms_pedidos.service;

import com.smartlogix.ms_pedidos.client.InventarioClient;
import com.smartlogix.ms_pedidos.client.PagosClient;
import com.smartlogix.ms_pedidos.client.EnviosClient;
import com.smartlogix.ms_pedidos.client.NotificacionesClient;
import com.smartlogix.ms_pedidos.dto.*;
import com.smartlogix.ms_pedidos.messaging.PedidoEventoDTO;
import com.smartlogix.ms_pedidos.messaging.PedidoProducer;
import com.smartlogix.ms_pedidos.model.*;
import com.smartlogix.ms_pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para la gestión de pedidos.
 * <p>
 * Orquesta el flujo de creación de pedidos: descuenta stock en {@code ms-inventario},
 * procesa el pago en {@code ms-pagos}, crea el envío en {@code ms-envios}
 * y notifica al cliente vía {@code ms-notificaciones}. Los fallos en servicios
 * auxiliares se ignoran para no bloquear la creación del pedido.
 * </p>
 *
 * @author SmartLogix Team
 */
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final InventarioClient inventarioClient;
    private final PagosClient pagosClient;
    private final EnviosClient enviosClient;
    private final NotificacionesClient notificacionesClient;
    private final PedidoProducer pedidoProducer;

    /**
     * Retorna la lista completa de pedidos.
     *
     * @return lista de {@link PedidoResponse}; vacía si no hay pedidos
     */
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un pedido por su identificador UUID.
     *
     * @param id identificador UUID del pedido
     * @return {@link PedidoResponse} con los datos del pedido
     * @throws RuntimeException si no existe el pedido
     */
    public PedidoResponse obtener(String id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Pedido no encontrado"));
        return convertirAResponse(pedido);
    }

    /**
     * Crea un pedido orquestando el descuento de stock, pago, envío y notificación.
     * <p>
     * Las llamadas a servicios auxiliares (inventario, pagos, envíos, notificaciones)
     * se ejecutan de forma tolerante a fallos: si alguna falla, el pedido se crea igualmente.
     * </p>
     *
     * @param request datos del pedido con cliente, tipo y líneas de producto
     * @return {@link PedidoResponse} del pedido creado con el total calculado
     */
    public PedidoResponse crear(PedidoRequest request) {

        // 1. Crea el pedido
        Pedido pedido = new Pedido();
        pedido.setClienteId(request.getClienteId());
        pedido.setTipo(request.getTipo());

        // 2. Crea los detalles y calcula el total
        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedidoRequest item : request.getProductos()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setProductoId(item.getProductoId());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(BigDecimal.TEN);
            detalle.setPedido(pedido);
            detalles.add(detalle);

            try {
                inventarioClient.actualizarStock(
                    item.getProductoId(), -item.getCantidad());
            } catch (Exception ignored) {}

            total = total.add(
                BigDecimal.TEN.multiply(
                    BigDecimal.valueOf(item.getCantidad())));
        }

        pedido.setDetalles(detalles);
        pedido.setTotal(total);
        pedidoRepository.save(pedido);

        // 4. Procesa el pago en ms-pagos
        try {
            Map<String, Object> pago = new HashMap<>();
            pago.put("pedidoId", 1L);
            pago.put("monto", total.doubleValue() > 0 ? total.doubleValue() : 1.0);
            pago.put("metodoPago", "TARJETA");
            pagosClient.procesarPago(pago);
        } catch (Exception ignored) {}

        // 5. Crea el envio en ms-envios
        try {
            Map<String, Object> envio = new HashMap<>();
            envio.put("pedido_id", pedido.getId());
            envio.put("cliente_id", request.getClienteId());
            enviosClient.crearEnvio(envio);
        } catch (Exception ignored) {}

        // 6. Notifica al cliente vía Feign (tolerante a fallos)
        try {
            Map<String, Object> notificacion = new HashMap<>();
            notificacion.put("usuarioId", 1L);
            notificacion.put("mensaje", "Tu pedido fue creado exitosamente");
            notificacionesClient.enviarNotificacion(notificacion);
        } catch (Exception ignored) {}

        // 7. Publica evento en RabbitMQ
        try {
            pedidoProducer.enviarPedidoCreado(
                new PedidoEventoDTO(pedido.getId(), pedido.getClienteId(), total));
        } catch (Exception ignored) {}

        return convertirAResponse(pedido);
    }

    /**
     * Actualiza el estado de un pedido y envía una notificación al cliente.
     *
     * @param id     identificador UUID del pedido
     * @param estado nuevo estado del pedido (ej. {@code ENVIADO}, {@code ENTREGADO})
     * @return {@link PedidoResponse} con el estado actualizado
     * @throws RuntimeException si no existe el pedido
     */
    public PedidoResponse actualizarEstado(
            String id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(estado);
        pedidoRepository.save(pedido);

        // Notifica el cambio de estado
        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("usuarioId", Long.valueOf(1));
        notificacion.put("tipo", "ESTADO_ACTUALIZADO");
        notificacion.put("mensaje", 
            "Tu pedido está ahora en estado: " + estado);
        notificacionesClient.enviarNotificacion(notificacion);

        return convertirAResponse(pedido);
    }

    private PedidoResponse convertirAResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getEstado(),
                pedido.getTipo(),
                pedido.getTotal(),
                pedido.getCreatedAt()
        );
    }
}
