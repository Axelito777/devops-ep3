package com.smartlogix.ms_inventario.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link ProductoResponse}.
 *
 * @author SmartLogix Team
 */
class ProductoResponseTest {
    @Test void constructor_y_getters() {
        LocalDateTime now = LocalDateTime.now();
        ProductoResponse r = new ProductoResponse("1","Tornillo","D",new BigDecimal("1.5"),10,2,"b1","p1",now);
        assertEquals("1", r.getId()); assertEquals("Tornillo", r.getNombre()); assertEquals(10, r.getStock());
    }
    @Test void setters() {
        ProductoResponse r = new ProductoResponse("1","a","d",BigDecimal.ONE,5,1,"b","p",null);
        r.setId("2"); r.setNombre("X"); r.setDescripcion("Y"); r.setPrecio(BigDecimal.TEN);
        r.setStock(20); r.setStockMinimo(3); r.setBodegaId("b2"); r.setProveedorId("p2"); r.setCreatedAt(LocalDateTime.now());
        assertEquals("2", r.getId()); assertEquals("X", r.getNombre()); assertEquals(20, r.getStock());
    }
    @Test void equals_mismosDatos() {
        ProductoResponse r1 = new ProductoResponse("1","a","d",BigDecimal.ONE,5,1,"b","p",null);
        ProductoResponse r2 = new ProductoResponse("1","a","d",BigDecimal.ONE,5,1,"b","p",null);
        assertEquals(r1, r2); assertEquals(r1.hashCode(), r2.hashCode());
    }
    @Test void equals_distinto() { assertNotEquals(new ProductoResponse("1","a","d",null,5,1,"b","p",null), new ProductoResponse("2","a","d",null,5,1,"b","p",null)); }
    @Test void equals_null_otroTipo_mismaInstancia() {
        ProductoResponse r = new ProductoResponse("1","a","d",null,5,1,"b","p",null);
        assertNotEquals(r, null); assertEquals(r, r); assertNotEquals(r, "str");
    }
    @Test void toString_noEsNulo() { assertNotNull(new ProductoResponse("1","a","d",null,5,1,"b","p",null).toString()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new ProductoResponse(null,null,null,null,null,null,null,null,null)::hashCode); }
}
