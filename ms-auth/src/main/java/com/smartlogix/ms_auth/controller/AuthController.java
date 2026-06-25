package com.smartlogix.ms_auth.controller;

import com.smartlogix.ms_auth.dto.LoginRequest;
import com.smartlogix.ms_auth.dto.LoginResponse;
import com.smartlogix.ms_auth.model.Usuario;
import com.smartlogix.ms_auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para las operaciones de autenticación.
 * <p>
 * Expone los endpoints de login, registro y validación de tokens JWT
 * bajo la ruta base {@code /api/auth}.
 * </p>
 *
 * @author SmartLogix Team
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Autentica a un usuario y devuelve un token JWT.
     *
     * @param request credenciales de acceso (email y contraseña)
     * @return {@code 200 OK} con el token JWT, rol y email del usuario
     * @throws RuntimeException si el usuario no existe o la contraseña es incorrecta
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param usuario datos del usuario a registrar (email, contraseña, rol)
     * @return {@code 200 OK} con mensaje de confirmación
     */
    @PostMapping("/registro")
    public ResponseEntity<String> registro(
            @RequestBody Usuario usuario) {
        authService.registro(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    /**
     * Valida si un token JWT es válido y no ha expirado.
     *
     * @param token cabecera {@code Authorization} con formato {@code Bearer <token>}
     * @return {@code 200 OK} con {@code true} si el token es válido, {@code false} en caso contrario
     */
    @GetMapping("/validar")
    public ResponseEntity<Boolean> validar(
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        return ResponseEntity.ok(authService.validarToken(jwt));
    }
}