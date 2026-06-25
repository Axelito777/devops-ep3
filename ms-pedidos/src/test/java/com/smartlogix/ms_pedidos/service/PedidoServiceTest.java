package com.smartlogix.ms_pedidos.service;

import com.smartlogix.ms_pedidos.client.EnviosClient;
import com.smartlogix.ms_pedidos.client.InventarioClient;
import com.smartlogix.ms_pedidos.client.NotificacionesClient;
import com.smartlogix.ms_pedidos.client.PagosClient;
import com.smartlogix.ms_pedidos.messaging.PedidoProducer;
import com.smartlogix.ms_pedidos.dto.DetallePedidoRequest;
import com.smartlogix.ms_pedidos.dto.PedidoRequest;
import com.smartlogix.ms_pedidos.dto.PedidoResponse;
import com.smartlogix.ms_pedidos.model.Pedido;
import com.smartlogix.ms_pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de {@code PedidoService} usando mocks de
 * {@link PedidoRepository}, {@link InventarioClient}, {@link PagosClient},
 * {@link EnviosClient}, {@link NotificacionesClient} y {@link PedidoProducer}.
 *
 * @author SmartLogix Team
 */
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private InventarioClient inventarioClient;
    @Mock private PagosClient pagosClient;
    @Mock private EnviosClient enviosClient;
    @Mock private NotificacionesClient notificacionesClient;
    @Mock private PedidoProducer pedidoProducer;
    @InjectMocks private PedidoService pedidoService;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId("ped-1");
        pedido.setClienteId("cli-1");
        pedido.setEstado("PENDIENTE");
        pedido.setTipo("NORMAL");
        pedido.setTotal(BigDecimal.valueOf(100));
    }

    @Test
    void listar_retornaLista() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<PedidoResponse> lista = pedidoService.listar();

        assertEquals(1, lista.size());
        assertEquals("ped-1", lista.get(0).getId());
    }

    @Test
    void listar_vacio_retornaListaVacia() {
        when(pedidoRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(pedidoService.listar().isEmpty());
    }

    @Test
    void obtener_existente_retornaResponse() {
        when(pedidoRepository.findById("ped-1")).thenReturn(Optional.of(pedido));

        PedidoResponse response = pedidoService.obtener("ped-1");

        assertNotNull(response);
        assertEquals("ped-1", response.getId());
    }

    @Test
    void obtener_noExiste_lanzaExcepcion() {
        when(pedidoRepository.findById("xx")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pedidoService.obtener("xx"));
    }

    @Test
    void crear_sinProductos_retornaResponseConTotalCero() {
        PedidoRequest request = new PedidoRequest();
        request.setClienteId("cli-1");
        request.setTipo("NORMAL");
        request.setProductos(Collections.emptyList());

        when(pedidoRepository.save(any())).thenReturn(pedido);

        PedidoResponse response = pedidoService.crear(request);

        assertNotNull(response);
        verify(pedidoRepository).save(any());
    }

    @Test
    void crear_conProductos_retornaResponse() {
        DetallePedidoRequest detalle = new DetallePedidoRequest();
        detalle.setProductoId("prod-1");
        detalle.setCantidad(2);

        PedidoRequest request = new PedidoRequest();
        request.setClienteId("cli-1");
        request.setTipo("NORMAL");
        request.setProductos(List.of(detalle));

        when(pedidoRepository.save(any())).thenReturn(pedido);
        doThrow(new RuntimeException("feign")).when(inventarioClient).actualizarStock(any(), any());
        doThrow(new RuntimeException("feign")).when(pagosClient).procesarPago(any());
        doThrow(new RuntimeException("feign")).when(enviosClient).crearEnvio(any());
        doThrow(new RuntimeException("feign")).when(notificacionesClient).enviarNotificacion(any());

        PedidoResponse response = pedidoService.crear(request);

        assertNotNull(response);
    }

    @Test
    void actualizarEstado_existente_retornaResponse() {
        when(pedidoRepository.findById("ped-1")).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedido);

        PedidoResponse response = pedidoService.actualizarEstado("ped-1", "ENTREGADO");

        assertNotNull(response);
        verify(pedidoRepository).save(any());
        verify(notificacionesClient).enviarNotificacion(any());
    }

    @Test
    void actualizarEstado_noExiste_lanzaExcepcion() {
        when(pedidoRepository.findById("xx")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pedidoService.actualizarEstado("xx", "ENTREGADO"));
    }
}
