package com.smartlogix.ms_auth.service;

import com.smartlogix.ms_auth.dto.LoginRequest;
import com.smartlogix.ms_auth.dto.LoginResponse;
import com.smartlogix.ms_auth.model.Usuario;
import com.smartlogix.ms_auth.repository.UsuarioRepository;
import com.smartlogix.ms_auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de negocio para la autenticación y gestión de usuarios.
 * <p>
 * Encapsula la lógica de autenticación con JWT, registro con contraseña
 * cifrada mediante BCrypt y validación de tokens.
 * </p>
 *
 * @author SmartLogix Team
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Autentica a un usuario verificando sus credenciales y genera un token JWT.
     *
     * @param request DTO con el email y la contraseña del usuario
     * @return {@link LoginResponse} con el token JWT, el rol y el email del usuario autenticado
     * @throws RuntimeException si el email no existe en el sistema o la contraseña no coincide
     */
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() ->
                new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(
                request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generarToken(
            usuario.getEmail(), usuario.getRol());

        return new LoginResponse(token, usuario.getRol(),
            usuario.getEmail());
    }

    /**
     * Registra un nuevo usuario cifrando su contraseña antes de persistirla.
     *
     * @param usuario entidad con los datos del nuevo usuario; la contraseña
     *                se sobreescribe con su versión cifrada
     */
    public void registro(Usuario usuario) {
        usuario.setPassword(
            passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    /**
     * Verifica si un token JWT es válido y no ha expirado.
     *
     * @param token cadena JWT sin el prefijo {@code Bearer}
     * @return {@code true} si el token es válido y vigente; {@code false} en caso contrario
     */
    public boolean validarToken(String token) {
        return jwtUtil.validarToken(token);
    }
}