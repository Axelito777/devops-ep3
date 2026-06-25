package com.smartlogix.ms_pedidos.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la entidad {@link DetallePedido}.
 *
 * @author SmartLogix Team
 */
class DetallePedidoModelTest {
    @Test void settersGetters() {
        DetallePedido d = new DetallePedido();
        Pedido pedido = new Pedido(); pedido.setId("p1");
        d.setId("d1"); d.setPedido(pedido); d.setProductoId("prod1"); d.setCantidad(3); d.setPrecioUnitario(BigDecimal.TEN);
        assertEquals("d1", d.getId()); assertEquals(pedido, d.getPedido());
        assertEquals("prod1", d.getProductoId()); assertEquals(3, d.getCantidad()); assertEquals(BigDecimal.TEN, d.getPrecioUnitario());
    }
    @Test void equals_mismosDatos() {
        DetallePedido d1 = new DetallePedido(); d1.setId("1"); d1.setProductoId("p");
        DetallePedido d2 = new DetallePedido(); d2.setId("1"); d2.setProductoId("p");
        assertEquals(d1, d2); assertEquals(d1.hashCode(), d2.hashCode());
    }
    @Test void equals_distinto() {
        DetallePedido d1 = new DetallePedido(); d1.setId("1");
        DetallePedido d2 = new DetallePedido(); d2.setId("2");
        assertNotEquals(d1, d2);
    }
    @Test void equals_null_otroTipo_mismaInstancia() {
        DetallePedido d = new DetallePedido(); d.setId("1");
        assertNotEquals(d, null); assertEquals(d, d); assertNotEquals(d, "str");
    }
    @Test void toString_noEsNulo() { assertNotNull(new DetallePedido().toString()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new DetallePedido()::hashCode); }
}
