package com.smartlogix.ms_pedidos.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la entidad {@link Pedido}.
 *
 * @author SmartLogix Team
 */
class PedidoModelTest {
    @Test void settersGetters() {
        Pedido p = new Pedido();
        LocalDateTime now = LocalDateTime.now();
        p.setId("1"); p.setClienteId("c1"); p.setEstado("PENDIENTE"); p.setTipo("NORMAL");
        p.setTotal(BigDecimal.valueOf(1000)); p.setDetalles(Collections.emptyList()); p.setCreatedAt(now); p.setUpdatedAt(now);
        assertEquals("1", p.getId()); assertEquals("c1", p.getClienteId()); assertEquals("PENDIENTE", p.getEstado());
        assertEquals("NORMAL", p.getTipo()); assertEquals(BigDecimal.valueOf(1000), p.getTotal()); assertEquals(now, p.getCreatedAt());
    }
    @Test void equals_mismosDatos() {
        Pedido p1 = new Pedido(); p1.setId("1"); p1.setClienteId("c1");
        Pedido p2 = new Pedido(); p2.setId("1"); p2.setClienteId("c1");
        assertEquals(p1, p2); assertEquals(p1.hashCode(), p2.hashCode());
    }
    @Test void equals_distinto() {
        Pedido p1 = new Pedido(); p1.setId("1");
        Pedido p2 = new Pedido(); p2.setId("2");
        assertNotEquals(p1, p2);
    }
    @Test void equals_null_otroTipo_mismaInstancia() {
        Pedido p = new Pedido(); p.setId("1");
        assertNotEquals(p, null); assertEquals(p, p); assertNotEquals(p, "str");
    }
    @Test void toString_noEsNulo() { Pedido p = new Pedido(); p.setEstado("PENDIENTE"); assertNotNull(p.toString()); }
    @Test void onCreate_asignaFechasYEstado() { Pedido p = new Pedido(); p.onCreate(); assertNotNull(p.getCreatedAt()); assertEquals("PENDIENTE", p.getEstado()); }
    @Test void onUpdate_actualizaFecha() { Pedido p = new Pedido(); p.onUpdate(); assertNotNull(p.getUpdatedAt()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new Pedido()::hashCode); }
}
