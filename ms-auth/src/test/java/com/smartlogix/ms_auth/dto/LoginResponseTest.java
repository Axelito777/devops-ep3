package com.smartlogix.ms_auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link LoginResponse}.
 *
 * @author SmartLogix Team
 */
class LoginResponseTest {

    @Test
    void constructor_tresArgs_y_getters() {
        LoginResponse r = new LoginResponse("mi-token", "ADMIN", "admin@test.com");

        assertEquals("mi-token", r.getToken());
        assertEquals("ADMIN", r.getRol());
        assertEquals("admin@test.com", r.getEmail());
    }

    @Test
    void setters_actualizanValores() {
        LoginResponse r = new LoginResponse("t", "r", "e");
        r.setToken("nuevo-token");
        r.setRol("USER");
        r.setEmail("nuevo@test.com");

        assertEquals("nuevo-token", r.getToken());
        assertEquals("USER", r.getRol());
        assertEquals("nuevo@test.com", r.getEmail());
    }

    @Test
    void equals_mismosDatos_debeSerIgual() {
        LoginResponse r1 = new LoginResponse("tok", "ADMIN", "a@b.com");
        LoginResponse r2 = new LoginResponse("tok", "ADMIN", "a@b.com");

        assertEquals(r1, r2);
    }

    @Test
    void hashCode_mismosDatos_debeSerIgual() {
        LoginResponse r1 = new LoginResponse("tok", "ADMIN", "a@b.com");
        LoginResponse r2 = new LoginResponse("tok", "ADMIN", "a@b.com");

        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_tokenDistinto_noDebeSerIgual() {
        LoginResponse r1 = new LoginResponse("tok1", "ADMIN", "a@b.com");
        LoginResponse r2 = new LoginResponse("tok2", "ADMIN", "a@b.com");

        assertNotEquals(r1, r2);
    }

    @Test
    void equals_rolDistinto_noDebeSerIgual() {
        LoginResponse r1 = new LoginResponse("tok", "ADMIN", "a@b.com");
        LoginResponse r2 = new LoginResponse("tok", "USER", "a@b.com");

        assertNotEquals(r1, r2);
    }

    @Test
    void equals_emailDistinto_noDebeSerIgual() {
        LoginResponse r1 = new LoginResponse("tok", "ADMIN", "a@b.com");
        LoginResponse r2 = new LoginResponse("tok", "ADMIN", "b@b.com");

        assertNotEquals(r1, r2);
    }

    @Test
    void equals_null_debeRetornarFalse() {
        LoginResponse r = new LoginResponse("t", "r", "e");
        assertNotEquals(r, null);
    }

    @Test
    void equals_otroTipo_debeRetornarFalse() {
        LoginResponse r = new LoginResponse("t", "r", "e");
        assertNotEquals(r, "string");
    }

    @Test
    void equals_mismaInstancia_debeSerIgual() {
        LoginResponse r = new LoginResponse("t", "r", "e");
        assertEquals(r, r);
    }

    @Test
    void toString_contieneLosDatos() {
        LoginResponse r = new LoginResponse("mi-jwt", "ADMIN", "admin@b.com");
        String str = r.toString();

        assertNotNull(str);
        assertTrue(str.contains("mi-jwt"));
        assertTrue(str.contains("ADMIN"));
        assertTrue(str.contains("admin@b.com"));
    }

    @Test
    void hashCode_camposNulos_noLanzaExcepcion() {
        LoginResponse r = new LoginResponse(null, null, null);
        assertDoesNotThrow(r::hashCode);
    }
}
