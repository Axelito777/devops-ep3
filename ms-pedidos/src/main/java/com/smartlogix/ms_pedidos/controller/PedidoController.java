package com.smartlogix.ms_pedidos.controller;

import com.smartlogix.ms_pedidos.dto.PedidoRequest;
import com.smartlogix.ms_pedidos.dto.PedidoResponse;
import com.smartlogix.ms_pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de pedidos.
 * <p>
 * Expone operaciones de consulta, creación y cambio de estado de pedidos
 * bajo la ruta base {@code /api/pedidos}.
 * </p>
 *
 * @author SmartLogix Team
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Retorna la lista completa de pedidos registrados.
     *
     * @return {@code 200 OK} con la lista de {@link PedidoResponse}
     */
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    /**
     * Obtiene un pedido por su identificador UUID.
     *
     * @param id identificador UUID del pedido
     * @return {@code 200 OK} con el {@link PedidoResponse} encontrado
     * @throws RuntimeException si no existe el pedido
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtener(
            @PathVariable String id) {
        return ResponseEntity.ok(pedidoService.obtener(id));
    }

    /**
     * Crea un nuevo pedido orquestando inventario, pago, envío y notificación.
     *
     * @param request datos del pedido con {@code clienteId}, {@code tipo} y lista de productos
     * @return {@code 200 OK} con el {@link PedidoResponse} del pedido creado
     */
    @PostMapping
    public ResponseEntity<PedidoResponse> crear(
            @RequestBody PedidoRequest request) {
        return ResponseEntity.ok(pedidoService.crear(request));
    }

    /**
     * Actualiza el estado de un pedido y notifica el cambio al cliente.
     *
     * @param id   identificador UUID del pedido
     * @param body mapa con la clave {@code estado} y el nuevo valor
     * @return {@code 200 OK} con el {@link PedidoResponse} actualizado
     * @throws RuntimeException si no existe el pedido
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
            pedidoService.actualizarEstado(id, body.get("estado")));
    }
}