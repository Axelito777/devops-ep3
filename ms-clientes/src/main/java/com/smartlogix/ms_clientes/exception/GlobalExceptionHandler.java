package com.smartlogix.ms_clientes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones del microservicio de clientes.
 * <p>
 * Traduce los errores de validación de los DTOs de entrada en respuestas
 * HTTP 400 con el detalle de cada campo inválido.
 * </p>
 *
 * @author SmartLogix Team
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura los errores de validación de Bean Validation (por ejemplo,
     * los definidos en {@link com.smartlogix.ms_clientes.dto.ClienteRequest})
     * y los convierte en una respuesta HTTP 400 con un mapa campo→mensaje.
     *
     * @param ex excepción lanzada por Spring al fallar la validación del request body
     * @return respuesta con estado 400 y los errores de validación por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        // Aquí se rescatan los mensajes que pusiste en el ClienteRequest (ej: "Formato de RUT inválido")
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
