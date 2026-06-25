package com.smartlogix.ms_gateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;

/**
 * Utilidad para validar tokens JWT y extraer el email del usuario,
 * usada por el filtro de seguridad del gateway para proteger las rutas.
 *
 * @author SmartLogix Team
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Construye la clave criptográfica HMAC a partir del secreto configurado.
     *
     * @return la {@link Key} usada para verificar tokens JWT
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
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
}
