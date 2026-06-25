package com.smartlogix.ms_pedidos.dto;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link PedidoRequest}.
 *
 * @author SmartLogix Team
 */
class PedidoRequestTest {
    @Test void gettersSetters() {
        PedidoRequest r = new PedidoRequest();
        r.setClienteId("c1"); r.setTipo("NORMAL"); r.setProductos(Collections.emptyList());
        assertEquals("c1", r.getClienteId()); assertEquals("NORMAL", r.getTipo()); assertNotNull(r.getProductos());
    }
    @Test void equals_mismosDatos() {
        PedidoRequest r1 = new PedidoRequest(); r1.setClienteId("c1"); r1.setTipo("N");
        PedidoRequest r2 = new PedidoRequest(); r2.setClienteId("c1"); r2.setTipo("N");
        assertEquals(r1, r2); assertEquals(r1.hashCode(), r2.hashCode());
    }
    @Test void equals_distinto() {
        PedidoRequest r1 = new PedidoRequest(); r1.setClienteId("c1");
        PedidoRequest r2 = new PedidoRequest(); r2.setClienteId("c2");
        assertNotEquals(r1, r2);
    }
    @Test void equals_null_otroTipo_mismaInstancia() {
        PedidoRequest r = new PedidoRequest();
        assertNotEquals(r, null); assertEquals(r, r); assertNotEquals(r, "str");
    }
    @Test void toString_noEsNulo() { assertNotNull(new PedidoRequest().toString()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new PedidoRequest()::hashCode); }
}
