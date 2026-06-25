package com.smartlogix.ms_pedidos.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link DetallePedidoRequest}.
 *
 * @author SmartLogix Team
 */
class DetallePedidoRequestTest {
    @Test void gettersSetters() {
        DetallePedidoRequest r = new DetallePedidoRequest();
        r.setProductoId("p1"); r.setCantidad(3);
        assertEquals("p1", r.getProductoId()); assertEquals(3, r.getCantidad());
    }
    @Test void equals_mismosDatos() {
        DetallePedidoRequest r1 = new DetallePedidoRequest(); r1.setProductoId("p1"); r1.setCantidad(2);
        DetallePedidoRequest r2 = new DetallePedidoRequest(); r2.setProductoId("p1"); r2.setCantidad(2);
        assertEquals(r1, r2); assertEquals(r1.hashCode(), r2.hashCode());
    }
    @Test void equals_distinto() {
        DetallePedidoRequest r1 = new DetallePedidoRequest(); r1.setProductoId("p1");
        DetallePedidoRequest r2 = new DetallePedidoRequest(); r2.setProductoId("p2");
        assertNotEquals(r1, r2);
    }
    @Test void equals_null_otroTipo_mismaInstancia() {
        DetallePedidoRequest r = new DetallePedidoRequest();
        assertNotEquals(r, null); assertEquals(r, r); assertNotEquals(r, "str");
    }
    @Test void toString_noEsNulo() { assertNotNull(new DetallePedidoRequest().toString()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new DetallePedidoRequest()::hashCode); }
}
