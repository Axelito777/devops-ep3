package com.smartlogix.ms_clientes.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link ClienteRequest}.
 *
 * @author SmartLogix Team
 */
class ClienteRequestTest {
    @Test void gettersSetters() {
        ClienteRequest r = new ClienteRequest();
        r.setNombre("Juan"); r.setRut("12345678-9"); r.setEmail("j@t.cl"); r.setTelefono("+569"); r.setDireccion("Calle 1");
        assertEquals("Juan", r.getNombre()); assertEquals("12345678-9", r.getRut()); assertEquals("j@t.cl", r.getEmail());
        assertEquals("+569", r.getTelefono()); assertEquals("Calle 1", r.getDireccion());
    }
    @Test void equals_mismosDatos() {
        ClienteRequest r1 = new ClienteRequest(); r1.setNombre("A"); r1.setEmail("a@b.com");
        ClienteRequest r2 = new ClienteRequest(); r2.setNombre("A"); r2.setEmail("a@b.com");
        assertEquals(r1, r2); assertEquals(r1.hashCode(), r2.hashCode());
    }
    @Test void equals_distinto() {
        ClienteRequest r1 = new ClienteRequest(); r1.setEmail("a@b.com");
        ClienteRequest r2 = new ClienteRequest(); r2.setEmail("b@b.com");
        assertNotEquals(r1, r2);
    }
    @Test void equals_null_otroTipo_mismaInstancia() {
        ClienteRequest r = new ClienteRequest();
        assertNotEquals(r, null); assertEquals(r, r); assertNotEquals(r, "str");
    }
    @Test void toString_noEsNulo() { assertNotNull(new ClienteRequest().toString()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new ClienteRequest()::hashCode); }
}
