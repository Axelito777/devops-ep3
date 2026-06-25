package com.smartlogix.ms_inventario.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la entidad {@link Producto}.
 *
 * @author SmartLogix Team
 */
class ProductoModelTest {
    @Test void settersGetters() {
        Producto p = new Producto();
        LocalDateTime now = LocalDateTime.now();
        p.setId("p1"); p.setNombre("Tornillo"); p.setDescripcion("10mm"); p.setPrecio(new BigDecimal("1.5"));
        p.setStock(100); p.setStockMinimo(10); p.setBodegaId("b1"); p.setProveedorId("prov1");
        p.setCreatedAt(now); p.setUpdatedAt(now);
        assertEquals("p1", p.getId()); assertEquals("Tornillo", p.getNombre()); assertEquals("10mm", p.getDescripcion());
        assertEquals(new BigDecimal("1.5"), p.getPrecio()); assertEquals(100, p.getStock()); assertEquals(10, p.getStockMinimo());
        assertEquals("b1", p.getBodegaId()); assertEquals("prov1", p.getProveedorId()); assertEquals(now, p.getCreatedAt());
    }
    @Test void equals_mismosDatos() {
        Producto p1 = new Producto(); p1.setId("1"); p1.setNombre("A");
        Producto p2 = new Producto(); p2.setId("1"); p2.setNombre("A");
        assertEquals(p1, p2); assertEquals(p1.hashCode(), p2.hashCode());
    }
    @Test void equals_distinto() {
        Producto p1 = new Producto(); p1.setId("1");
        Producto p2 = new Producto(); p2.setId("2");
        assertNotEquals(p1, p2);
    }
    @Test void equals_null_otroTipo_mismaInstancia() {
        Producto p = new Producto(); p.setId("1");
        assertNotEquals(p, null); assertEquals(p, p); assertNotEquals(p, "str");
    }
    @Test void toString_noEsNulo() { Producto p = new Producto(); p.setNombre("Test"); assertNotNull(p.toString()); }
    @Test void onCreate_asignaFechas() { Producto p = new Producto(); p.onCreate(); assertNotNull(p.getCreatedAt()); assertNotNull(p.getUpdatedAt()); }
    @Test void onUpdate_actualizaFecha() { Producto p = new Producto(); p.onUpdate(); assertNotNull(p.getUpdatedAt()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new Producto()::hashCode); }
}
