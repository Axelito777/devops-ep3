package com.smartlogix.ms_auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests para JwtUtil.
 * No necesita contexto de Spring — se inyectan los valores con ReflectionTestUtils.
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET =
        "smartlogix-secret-key-2024-super-segura-para-produccion-abc123";
    private static final long EXPIRATION = 86400000L; // 24h en ms

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRATION);
    }

    // ── generarToken ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("generarToken: debe retornar un token no nulo y no vacío")
    void generarToken_debeRetornarTokenNoVacio() {
        String token = jwtUtil.generarToken("user@test.com", "ADMIN");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("generarToken: el token debe tener formato JWT (3 partes separadas por '.')")
    void generarToken_debeSerFormatoJWT() {
        String token = jwtUtil.generarToken("user@test.com", "USER");

        String[] partes = token.split("\\.");
        assertEquals(3, partes.length, "Un JWT válido tiene exactamente 3 partes");
    }

    // ── obtenerEmail ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerEmail: debe extraer el email correcto del token")
    void obtenerEmail_debeRetornarEmailCorrecto() {
        String email = "usuario@smartlogix.com";
        String token = jwtUtil.generarToken(email, "USER");

        String emailExtraido = jwtUtil.obtenerEmail(token);

        assertEquals(email, emailExtraido);
    }

    @Test
    @DisplayName("obtenerEmail: emails distintos producen emails distintos en el token")
    void obtenerEmail_emailsDistintosProducenResultadosDistintos() {
        String token1 = jwtUtil.generarToken("admin@test.com", "ADMIN");
        String token2 = jwtUtil.generarToken("cliente@test.com", "USER");

        assertNotEquals(jwtUtil.obtenerEmail(token1), jwtUtil.obtenerEmail(token2));
    }

    // ── validarToken ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("validarToken: un token recién generado debe ser válido")
    void validarToken_tokenValido_debeRetornarTrue() {
        String token = jwtUtil.generarToken("user@test.com", "USER");

        assertTrue(jwtUtil.validarToken(token));
    }

    @Test
    @DisplayName("validarToken: un token malformado debe retornar false")
    void validarToken_tokenMalformado_debeRetornarFalse() {
        assertFalse(jwtUtil.validarToken("esto.no.es.un.jwt.valido"));
    }

    @Test
    @DisplayName("validarToken: un token vacío debe retornar false")
    void validarToken_tokenVacio_debeRetornarFalse() {
        assertFalse(jwtUtil.validarToken(""));
    }

    @Test
    @DisplayName("validarToken: un token expirado debe retornar false")
    void validarToken_tokenExpirado_debeRetornarFalse() {
        // Configuramos expiración en 0 ms → el token ya nació vencido
        ReflectionTestUtils.setField(jwtUtil, "expiration", 0L);
        String tokenExpirado = jwtUtil.generarToken("user@test.com", "USER");

        assertFalse(jwtUtil.validarToken(tokenExpirado));
    }

    @Test
    @DisplayName("validarToken: token firmado con otro secreto debe retornar false")
    void validarToken_tokenFirmadoConOtroSecreto_debeRetornarFalse() {
        // Generamos con el secreto normal
        String tokenOriginal = jwtUtil.generarToken("user@test.com", "USER");

        // Ahora cambiamos el secreto (simula token de otro servicio)
        ReflectionTestUtils.setField(jwtUtil, "secret",
            "otro-secreto-completamente-diferente-que-no-coincide-1234567890");

        assertFalse(jwtUtil.validarToken(tokenOriginal));
    }
}