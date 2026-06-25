package com.smartlogix.ms_gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de {@link JwtUtil}.
 *
 * @author SmartLogix Team
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String SECRET =
            "smartlogix-secret-key-2024-super-segura-para-produccion-abc123";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
    }

    private String generarToken(String email) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
                .signWith(key)
                .compact();
    }

    @Test
    void validarToken_tokenValido_retornaTrue() {
        String token = generarToken("user@test.com");
        assertTrue(jwtUtil.validarToken(token));
    }

    @Test
    void validarToken_tokenMalformado_retornaFalse() {
        assertFalse(jwtUtil.validarToken("esto.no.es.jwt"));
    }

    @Test
    void validarToken_tokenVacio_retornaFalse() {
        assertFalse(jwtUtil.validarToken(""));
    }

    @Test
    void validarToken_tokenExpirado_retornaFalse() {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        String tokenExpirado = Jwts.builder()
                .setSubject("user@test.com")
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(key)
                .compact();
        assertFalse(jwtUtil.validarToken(tokenExpirado));
    }

    @Test
    void validarToken_secretoDistinto_retornaFalse() {
        String token = generarToken("user@test.com");
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "otro-secreto-completamente-diferente-1234567890abcdef");
        assertFalse(jwtUtil.validarToken(token));
    }

    @Test
    void obtenerEmail_retornaEmailCorrecto() {
        String token = generarToken("admin@smartlogix.com");
        assertEquals("admin@smartlogix.com", jwtUtil.obtenerEmail(token));
    }

    @Test
    void obtenerEmail_emailsDistintos_retornanDistintos() {
        String t1 = generarToken("a@test.com");
        String t2 = generarToken("b@test.com");
        assertNotEquals(jwtUtil.obtenerEmail(t1), jwtUtil.obtenerEmail(t2));
    }
}
