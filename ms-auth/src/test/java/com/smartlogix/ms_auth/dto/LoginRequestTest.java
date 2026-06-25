package com.smartlogix.ms_auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del DTO {@link LoginRequest}.
 *
 * @author SmartLogix Team
 */
class LoginRequestTest {

    @Test
    void getters_setters_funcionanCorrectamente() {
        LoginRequest r = new LoginRequest();
        r.setEmail("usuario@test.com");
        r.setPassword("clave123");

        assertEquals("usuario@test.com", r.getEmail());
        assertEquals("clave123", r.getPassword());
    }

    @Test
    void equals_mismosDatos_debeSerIgual() {
        LoginRequest r1 = new LoginRequest();
        r1.setEmail("a@b.com");
        r1.setPassword("pass");

        LoginRequest r2 = new LoginRequest();
        r2.setEmail("a@b.com");
        r2.setPassword("pass");

        assertEquals(r1, r2);
    }

    @Test
    void hashCode_mismosDatos_debeSerIgual() {
        LoginRequest r1 = new LoginRequest();
        r1.setEmail("a@b.com");
        r1.setPassword("pass");

        LoginRequest r2 = new LoginRequest();
        r2.setEmail("a@b.com");
        r2.setPassword("pass");

        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_emailDistinto_noDebeSerIgual() {
        LoginRequest r1 = new LoginRequest();
        r1.setEmail("a@b.com");
        r1.setPassword("pass");

        LoginRequest r2 = new LoginRequest();
        r2.setEmail("otro@b.com");
        r2.setPassword("pass");

        assertNotEquals(r1, r2);
    }

    @Test
    void equals_passwordDistinta_noDebeSerIgual() {
        LoginRequest r1 = new LoginRequest();
        r1.setEmail("a@b.com");
        r1.setPassword("pass1");

        LoginRequest r2 = new LoginRequest();
        r2.setEmail("a@b.com");
        r2.setPassword("pass2");

        assertNotEquals(r1, r2);
    }

    @Test
    void equals_null_debeRetornarFalse() {
        LoginRequest r = new LoginRequest();
        assertNotEquals(r, null);
    }

    @Test
    void equals_otroTipo_debeRetornarFalse() {
        LoginRequest r = new LoginRequest();
        r.setEmail("a@b.com");
        assertNotEquals(r, "string");
    }

    @Test
    void equals_mismaInstancia_debeSerIgual() {
        LoginRequest r = new LoginRequest();
        r.setEmail("a@b.com");
        assertEquals(r, r);
    }

    @Test
    void toString_contieneLosDatos() {
        LoginRequest r = new LoginRequest();
        r.setEmail("test@ejemplo.com");
        r.setPassword("secreto");

        String str = r.toString();

        assertNotNull(str);
        assertTrue(str.contains("test@ejemplo.com"));
        assertTrue(str.contains("secreto"));
    }

    @Test
    void camposNulos_noRompenHashCode() {
        LoginRequest r = new LoginRequest();
        assertDoesNotThrow(r::hashCode);
    }

    @Test
    void camposNulos_equalsConOtroNulo_debeSerIgual() {
        LoginRequest r1 = new LoginRequest();
        LoginRequest r2 = new LoginRequest();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
