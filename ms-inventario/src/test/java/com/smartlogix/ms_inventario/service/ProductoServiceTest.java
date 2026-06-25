package com.smartlogix.ms_inventario.service;

import com.smartlogix.ms_inventario.dto.ProductoRequest;
import com.smartlogix.ms_inventario.dto.ProductoResponse;
import com.smartlogix.ms_inventario.model.Producto;
import com.smartlogix.ms_inventario.repository.ProductoRepository;
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
 * Pruebas unitarias de {@code ProductoService} usando un mock de
 * {@link ProductoRepository}.
 *
 * @author SmartLogix Team
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock private ProductoRepository productoRepository;
    @InjectMocks private ProductoService productoService;

    private Producto producto;
    private ProductoRequest request;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId("p-1");
        producto.setNombre("Tornillo");
        producto.setDescripcion("Tornillo 10mm");
        producto.setPrecio(new BigDecimal("1.50"));
        producto.setStock(100);
        producto.setStockMinimo(10);
        producto.setBodegaId("b-1");
        producto.setProveedorId("prov-1");

        request = new ProductoRequest();
        request.setNombre("Tornillo");
        request.setDescripcion("Tornillo 10mm");
        request.setPrecio(new BigDecimal("1.50"));
        request.setStock(100);
        request.setStockMinimo(10);
        request.setBodegaId("b-1");
        request.setProveedorId("prov-1");
    }

    @Test
    void listar_retornaLista() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponse> lista = productoService.listar();

        assertEquals(1, lista.size());
        assertEquals("Tornillo", lista.get(0).getNombre());
    }

    @Test
    void listar_vacio_retornaListaVacia() {
        when(productoRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(productoService.listar().isEmpty());
    }

    @Test
    void obtener_existente_retornaResponse() {
        when(productoRepository.findById("p-1")).thenReturn(Optional.of(producto));

        ProductoResponse response = productoService.obtener("p-1");

        assertNotNull(response);
        assertEquals("p-1", response.getId());
    }

    @Test
    void obtener_noExiste_lanzaExcepcion() {
        when(productoRepository.findById("xx")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.obtener("xx"));
    }

    @Test
    void crear_exitoso_retornaResponse() {
        when(productoRepository.save(any())).thenReturn(producto);

        ProductoResponse response = productoService.crear(request);

        assertNotNull(response);
        assertEquals("Tornillo", response.getNombre());
        verify(productoRepository).save(any());
    }

    @Test
    void actualizarStock_suma_retornaResponseActualizado() {
        when(productoRepository.findById("p-1")).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenReturn(producto);

        ProductoResponse response = productoService.actualizarStock("p-1", 10);

        assertNotNull(response);
        verify(productoRepository).save(any());
    }

    @Test
    void actualizarStock_resta_stockInsuficiente_lanzaExcepcion() {
        when(productoRepository.findById("p-1")).thenReturn(Optional.of(producto));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.actualizarStock("p-1", -999));

        assertEquals("Stock insuficiente", ex.getMessage());
    }

    @Test
    void actualizarStock_noExiste_lanzaExcepcion() {
        when(productoRepository.findById("xx")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.actualizarStock("xx", 5));
    }

    @Test
    void bajoStock_retornaProductosBajoStock() {
        when(productoRepository.findByStockLessThanEqual(10)).thenReturn(List.of(producto));

        List<ProductoResponse> lista = productoService.bajoStock();

        assertEquals(1, lista.size());
    }

    @Test
    void actualizar_existente_retornaResponseActualizado() {
        when(productoRepository.findById("p-1")).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenReturn(producto);

        ProductoResponse response = productoService.actualizar("p-1", request);

        assertNotNull(response);
        verify(productoRepository).save(any());
    }

    @Test
    void actualizar_noExiste_lanzaExcepcion() {
        when(productoRepository.findById("xx")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.actualizar("xx", request));
    }

    @Test
    void eliminar_existente_llamaDelete() {
        when(productoRepository.findById("p-1")).thenReturn(Optional.of(producto));

        productoService.eliminar("p-1");

        verify(productoRepository).deleteById("p-1");
    }

    @Test
    void eliminar_noExiste_lanzaExcepcion() {
        when(productoRepository.findById("xx")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.eliminar("xx"));
    }
}
