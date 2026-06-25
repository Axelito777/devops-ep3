package com.smartlogix.ms_auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * Utilidad para generar, parsear y validar tokens JWT usados en la
 * autenticación del sistema.
 *
 * @author SmartLogix Team
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Construye la clave criptográfica HMAC a partir del secreto configurado.
     *
     * @return la {@link Key} usada para firmar y verificar tokens JWT
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT firmado para el usuario indicado.
     *
     * @param email email del usuario, usado como subject del token
     * @param rol   rol del usuario, incluido como claim
     * @return el token JWT compactado y firmado
     */
    public String generarToken(String email, String rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    /**
     * Extrae el email (subject) contenido en un token JWT.
     *
     * @param token token JWT sin el prefijo {@code Bearer}
     * @return el email del usuario al que pertenece el token
     */
    public String obtenerEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Verifica si un token JWT es válido y no ha expirado.
     *
     * @param token cadena JWT a validar
     * @return {@code true} si el token es válido y vigente; {@code false} en caso contrario
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}