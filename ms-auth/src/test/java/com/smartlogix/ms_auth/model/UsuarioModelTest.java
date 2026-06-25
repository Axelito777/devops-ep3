package com.smartlogix.ms_auth.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la entidad {@link Usuario}.
 *
 * @author SmartLogix Team
 */
class UsuarioModelTest {

    @Test
    void settersGetters_cubrenTodosLosCampos() {
        Usuario u = new Usuario();
        LocalDateTime ahora = LocalDateTime.now();

        u.setId("test-uuid");
        u.setEmail("test@smartlogix.com");
        u.setPassword("hashedPass");
        u.setRol("ADMIN");
        u.setCreatedAt(ahora);
        u.setUpdatedAt(ahora);

        assertEquals("test-uuid", u.getId());
        assertEquals("test@smartlogix.com", u.getEmail());
        assertEquals("hashedPass", u.getPassword());
        assertEquals("ADMIN", u.getRol());
        assertEquals(ahora, u.getCreatedAt());
        assertEquals(ahora, u.getUpdatedAt());
    }

    @Test
    void equals_mismosDatos_debeSerIgual() {
        Usuario u1 = new Usuario();
        u1.setId("1");
        u1.setEmail("a@b.com");
        u1.setPassword("p");
        u1.setRol("USER");

        Usuario u2 = new Usuario();
        u2.setId("1");
        u2.setEmail("a@b.com");
        u2.setPassword("p");
        u2.setRol("USER");

        assertEquals(u1, u2);
    }

    @Test
    void hashCode_mismosDatos_debeSerIgual() {
        Usuario u1 = new Usuario();
        u1.setId("1");
        u1.setEmail("a@b.com");
        u1.setPassword("p");

        Usuario u2 = new Usuario();
        u2.setId("1");
        u2.setEmail("a@b.com");
        u2.setPassword("p");

        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void equals_emailDistinto_noDebeSerIgual() {
        Usuario u1 = new Usuario();
        u1.setId("1");
        u1.setEmail("a@b.com");

        Usuario u2 = new Usuario();
        u2.setId("1");
        u2.setEmail("otro@b.com");

        assertNotEquals(u1, u2);
    }

    @Test
    void equals_passwordDistinto_noDebeSerIgual() {
        Usuario u1 = new Usuario();
        u1.setEmail("a@b.com");
        u1.setPassword("pass1");

        Usuario u2 = new Usuario();
        u2.setEmail("a@b.com");
        u2.setPassword("pass2");

        assertNotEquals(u1, u2);
    }

    @Test
    void equals_null_debeRetornarFalse() {
        Usuario u = new Usuario();
        u.setEmail("a@b.com");
        assertNotEquals(u, null);
    }

    @Test
    void equals_otroTipo_debeRetornarFalse() {
        Usuario u = new Usuario();
        u.setEmail("a@b.com");
        assertNotEquals(u, "string");
    }

    @Test
    void equals_mismaInstancia_debeSerIgual() {
        Usuario u = new Usuario();
        u.setEmail("a@b.com");
        assertEquals(u, u);
    }

    @Test
    void toString_contieneEmail() {
        Usuario u = new Usuario();
        u.setEmail("visible@test.com");
        u.setRol("USER");

        String str = u.toString();

        assertNotNull(str);
        assertTrue(str.contains("visible@test.com"));
    }

    @Test
    void onCreate_debeAsignarCreatedAtYUpdatedAt() {
        Usuario u = new Usuario();
        assertNull(u.getCreatedAt());

        u.onCreate();

        assertNotNull(u.getCreatedAt());
        assertNotNull(u.getUpdatedAt());
    }

    @Test
    void onUpdate_debeActualizarUpdatedAt() {
        Usuario u = new Usuario();
        u.onUpdate();

        assertNotNull(u.getUpdatedAt());
    }

    @Test
    void hashCode_camposNulos_noLanzaExcepcion() {
        Usuario u = new Usuario();
        assertDoesNotThrow(u::hashCode);
    }

    @Test
    void equals_camposNulos_dobleNulo_debeSerIgual() {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        assertEquals(u1, u2);
    }
}
