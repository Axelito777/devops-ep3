package com.smartlogix.ms_auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Manejador global de excepciones del microservicio de autenticación.
 * <p>
 * Traduce las {@link RuntimeException} de negocio (credenciales inválidas,
 * usuario no encontrado, token inválido) en respuestas HTTP 401.
 * </p>
 *
 * @author SmartLogix Team
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura cualquier {@link RuntimeException} no manejada y la convierte
     * en una respuesta HTTP 401 con el mensaje de error.
     *
     * @param ex excepción de negocio lanzada durante la autenticación
     * @return respuesta con estado 401 y un mapa {@code {"error": mensaje}}
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }
}
