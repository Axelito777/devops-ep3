package com.smartlogix.ms_clientes.service;

import com.smartlogix.ms_clientes.client.PedidosClient;
import com.smartlogix.ms_clientes.dto.ClienteRequest;
import com.smartlogix.ms_clientes.dto.ClienteResponse;
import com.smartlogix.ms_clientes.dto.PedidoResponse;
import com.smartlogix.ms_clientes.model.Cliente;
import com.smartlogix.ms_clientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
/**
 * Pruebas unitarias de {@link ClienteService} usando mocks de
 * {@link ClienteRepository} y {@link PedidosClient}.
 *
 * @author SmartLogix Team
 */
public class ClienteServiceTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private PedidosClient pedidosClient;
    @InjectMocks private ClienteService clienteService;

    private ClienteRequest request;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        request = new ClienteRequest();
        request.setNombre("Juan Perez");
        request.setRut("12345678-9");
        request.setEmail("juan@smartlogix.cl");
        request.setTelefono("+56912345678");
        request.setDireccion("Calle 1");

        cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Juan Perez");
        cliente.setRut("12345678-9");
        cliente.setEmail("juan@smartlogix.cl");
    }

    @Test
    void crear_exitoso_retornaClienteResponse() {
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.findByRut(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponse response = clienteService.crear(request);

        assertNotNull(response);
        assertEquals("Juan Perez", response.getNombre());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void crear_emailDuplicado_lanzaExcepcion() {
        when(clienteRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(cliente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> clienteService.crear(request));
        assertEquals("El email ya está registrado", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void crear_rutDuplicado_lanzaExcepcion() {
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.findByRut(request.getRut())).thenReturn(Optional.of(cliente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> clienteService.crear(request));
        assertEquals("El rut ya está registrado", ex.getMessage());
    }

    @Test
    void obtener_existente_retornaResponse() {
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente));

        ClienteResponse response = clienteService.obtener("1");

        assertNotNull(response);
        assertEquals("1", response.getId());
    }

    @Test
    void obtener_noExiste_lanzaExcepcion() {
        when(clienteRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clienteService.obtener("99"));
    }

    @Test
    void listar_retornaLista() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<ClienteResponse> lista = clienteService.listar();

        assertEquals(1, lista.size());
    }

    @Test
    void listar_vacio_retornaListaVacia() {
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(clienteService.listar().isEmpty());
    }

    @Test
    void actualizar_existente_retornaResponseActualizado() {
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponse response = clienteService.actualizar("1", request);

        assertNotNull(response);
        verify(clienteRepository).save(any());
    }

    @Test
    void actualizar_noExiste_lanzaExcepcion() {
        when(clienteRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clienteService.actualizar("99", request));
    }

    @Test
    void getPedidosByCliente_existente_retornaPedidos() {
        PedidoResponse pedido = new PedidoResponse();
        pedido.setId("p1");
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente));
        when(pedidosClient.getPedidosByCliente("1")).thenReturn(List.of(pedido));

        List<PedidoResponse> pedidos = clienteService.getPedidosByCliente("1");

        assertFalse(pedidos.isEmpty());
    }

    @Test
    void getPedidosByCliente_noExiste_lanzaExcepcion() {
        when(clienteRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clienteService.getPedidosByCliente("99"));
    }
}
