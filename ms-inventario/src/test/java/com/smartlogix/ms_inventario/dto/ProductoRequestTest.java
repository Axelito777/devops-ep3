package com.smartlogix.ms_inventario.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link ProductoRequest}.
 *
 * @author SmartLogix Team
 */
class ProductoRequestTest {
    @Test void gettersSetters() {
        ProductoRequest r = new ProductoRequest();
        r.setNombre("T"); r.setDescripcion("D"); r.setPrecio(new BigDecimal("1.0")); r.setStock(10);
        r.setStockMinimo(2); r.setBodegaId("b1"); r.setProveedorId("p1");
        assertEquals("T", r.getNombre()); assertEquals("D", r.getDescripcion()); assertEquals(new BigDecimal("1.0"), r.getPrecio());
        assertEquals(10, r.getStock()); assertEquals(2, r.getStockMinimo()); assertEquals("b1", r.getBodegaId()); assertEquals("p1", r.getProveedorId());
    }
    @Test void equals_mismosDatos() {
        ProductoRequest r1 = new ProductoRequest(); r1.setNombre("A"); r1.setStock(5);
        ProductoRequest r2 = new ProductoRequest(); r2.setNombre("A"); r2.setStock(5);
        assertEquals(r1, r2); assertEquals(r1.hashCode(), r2.hashCode());
    }
    @Test void equals_distinto() {
        ProductoRequest r1 = new ProductoRequest(); r1.setNombre("A");
        ProductoRequest r2 = new ProductoRequest(); r2.setNombre("B");
        assertNotEquals(r1, r2);
    }
    @Test void equals_null_otroTipo_mismaInstancia() {
        ProductoRequest r = new ProductoRequest();
        assertNotEquals(r, null); assertEquals(r, r); assertNotEquals(r, "str");
    }
    @Test void toString_noEsNulo() { assertNotNull(new ProductoRequest().toString()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new ProductoRequest()::hashCode); }
}
