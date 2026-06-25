package com.smartlogix.ms_clientes.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la entidad {@link Cliente}.
 *
 * @author SmartLogix Team
 */
class ClienteModelTest {
    @Test void settersGetters() {
        Cliente c = new Cliente();
        LocalDateTime now = LocalDateTime.now();
        c.setId("1"); c.setNombre("Juan"); c.setRut("12345678-9"); c.setEmail("j@t.cl");
        c.setTelefono("+569"); c.setDireccion("Calle 1"); c.setCreatedAt(now); c.setUpdatedAt(now);
        assertEquals("1", c.getId()); assertEquals("Juan", c.getNombre()); assertEquals("12345678-9", c.getRut());
        assertEquals("j@t.cl", c.getEmail()); assertEquals("+569", c.getTelefono()); assertEquals("Calle 1", c.getDireccion());
        assertEquals(now, c.getCreatedAt()); assertEquals(now, c.getUpdatedAt());
    }
    @Test void equals_mismosDatos() {
        Cliente c1 = new Cliente(); c1.setId("1"); c1.setEmail("a@b.com");
        Cliente c2 = new Cliente(); c2.setId("1"); c2.setEmail("a@b.com");
        assertEquals(c1, c2); assertEquals(c1.hashCode(), c2.hashCode());
    }
    @Test void equals_distinto() {
        Cliente c1 = new Cliente(); c1.setId("1");
        Cliente c2 = new Cliente(); c2.setId("2");
        assertNotEquals(c1, c2);
    }
    @Test void equals_null_otroTipo_mismaInstancia() {
        Cliente c = new Cliente(); c.setId("1");
        assertNotEquals(c, null); assertEquals(c, c); assertNotEquals(c, "str");
    }
    @Test void toString_contieneEmail() { Cliente c = new Cliente(); c.setEmail("vis@test.cl"); assertTrue(c.toString().contains("vis@test.cl")); }
    @Test void onCreate_asignaFechas() { Cliente c = new Cliente(); c.onCreate(); assertNotNull(c.getCreatedAt()); assertNotNull(c.getUpdatedAt()); }
    @Test void onUpdate_actualizaFecha() { Cliente c = new Cliente(); c.onUpdate(); assertNotNull(c.getUpdatedAt()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new Cliente()::hashCode); }
}
