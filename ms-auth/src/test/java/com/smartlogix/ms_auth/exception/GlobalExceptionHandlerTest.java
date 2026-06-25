package com.smartlogix.ms_auth.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de {@link GlobalExceptionHandler}.
 *
 * @author SmartLogix Team
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntimeException_debeRetornar401ConMensajeDeError() {
        RuntimeException ex = new RuntimeException("Contraseña incorrecta");

        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Contraseña incorrecta", response.getBody().get("error"));
    }

    @Test
    void handleRuntimeException_usuarioNoEncontrado_debeRetornar401() {
        RuntimeException ex = new RuntimeException("Usuario no encontrado");

        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Usuario no encontrado", response.getBody().get("error"));
    }
}
