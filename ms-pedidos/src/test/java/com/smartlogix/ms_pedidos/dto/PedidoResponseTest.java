package com.smartlogix.ms_pedidos.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link PedidoResponse}.
 *
 * @author SmartLogix Team
 */
class PedidoResponseTest {
    @Test void constructor_y_getters() {
        LocalDateTime now = LocalDateTime.now();
        PedidoResponse r = new PedidoResponse("1","c1","PENDIENTE","NORMAL",BigDecimal.TEN,now);
        assertEquals("1", r.getId()); assertEquals("c1", r.getClienteId()); assertEquals("PENDIENTE", r.getEstado());
    }
    @Test void setters() {
        PedidoResponse r = new PedidoResponse("1","c","e","t",BigDecimal.ONE,null);
        r.setId("2"); r.setClienteId("c2"); r.setEstado("ENTREGADO"); r.setTipo("EXPRESS");
        r.setTotal(BigDecimal.TEN); r.setCreatedAt(LocalDateTime.now());
        assertEquals("2", r.getId()); assertEquals("ENTREGADO", r.getEstado());
    }
    @Test void equals_mismosDatos() {
        LocalDateTime now = LocalDateTime.now();
        PedidoResponse r1 = new PedidoResponse("1","c","e","t",BigDecimal.TEN,now);
        PedidoResponse r2 = new PedidoResponse("1","c","e","t",BigDecimal.TEN,now);
        assertEquals(r1, r2); assertEquals(r1.hashCode(), r2.hashCode());
    }
    @Test void equals_distinto() { assertNotEquals(new PedidoResponse("1","c","e","t",null,null), new PedidoResponse("2","c","e","t",null,null)); }
    @Test void equals_null_otroTipo_mismaInstancia() {
        PedidoResponse r = new PedidoResponse("1","c","e","t",null,null);
        assertNotEquals(r, null); assertEquals(r, r); assertNotEquals(r, "str");
    }
    @Test void toString_noEsNulo() { assertNotNull(new PedidoResponse("1","c","e","t",null,null).toString()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new PedidoResponse(null,null,null,null,null,null)::hashCode); }
}
