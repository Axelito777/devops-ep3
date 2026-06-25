package com.smartlogix.ms_clientes.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link ClienteResponse}.
 *
 * @author SmartLogix Team
 */
class ClienteResponseTest {
    @Test void constructor_y_getters() {
        LocalDateTime now = LocalDateTime.now();
        ClienteResponse r = new ClienteResponse("1", "Juan", "12345678-9", "j@t.cl", "+569", "Calle 1", now);
        assertEquals("1", r.getId()); assertEquals("Juan", r.getNombre()); assertEquals("12345678-9", r.getRut());
        assertEquals("j@t.cl", r.getEmail()); assertEquals("+569", r.getTelefono()); assertEquals("Calle 1", r.getDireccion());
    }
    @Test void setters() {
        ClienteResponse r = new ClienteResponse("1","a","b","c","d","e",null);
        r.setId("2"); r.setNombre("X"); r.setRut("R"); r.setEmail("x@x.com"); r.setTelefono("T"); r.setDireccion("D"); r.setCreatedAt(LocalDateTime.now());
        assertEquals("2", r.getId()); assertEquals("X", r.getNombre());
    }
    @Test void equals_mismosDatos() {
        LocalDateTime now = LocalDateTime.now();
        ClienteResponse r1 = new ClienteResponse("1","a","b","c","d","e",now);
        ClienteResponse r2 = new ClienteResponse("1","a","b","c","d","e",now);
        assertEquals(r1, r2); assertEquals(r1.hashCode(), r2.hashCode());
    }
    @Test void equals_distinto() { assertNotEquals(new ClienteResponse("1","a","b","c","d","e",null), new ClienteResponse("2","a","b","c","d","e",null)); }
    @Test void equals_null_otroTipo_mismaInstancia() {
        ClienteResponse r = new ClienteResponse("1","a","b","c","d","e",null);
        assertNotEquals(r, null); assertEquals(r, r); assertNotEquals(r, "str");
    }
    @Test void toString_noEsNulo() { assertNotNull(new ClienteResponse("1","a","b","c","d","e",null).toString()); }
    @Test void hashCode_nulos() { assertDoesNotThrow(new ClienteResponse(null,null,null,null,null,null,null)::hashCode); }
}
