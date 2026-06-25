package com.smartlogix.ms_auth.service;

import com.smartlogix.ms_auth.dto.LoginRequest;
import com.smartlogix.ms_auth.dto.LoginResponse;
import com.smartlogix.ms_auth.model.Usuario;
import com.smartlogix.ms_auth.repository.UsuarioRepository;
import com.smartlogix.ms_auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests para AuthService.
 * Usa Mockito para aislar el servicio de sus dependencias (BD, JWT, encoder).
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Usuario usuarioMock;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setEmail("test@smartlogix.com");
        usuarioMock.setPassword("$2a$10$hashedpassword");
        usuarioMock.setRol("USER");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@smartlogix.com");
        loginRequest.setPassword("password123");
    }

    // ── login ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("login: credenciales correctas deben retornar LoginResponse con token")
    void login_credencialesCorrectas_debeRetornarLoginResponse() {
        when(usuarioRepository.findByEmail("test@smartlogix.com"))
            .thenReturn(Optional.of(usuarioMock));
        when(passwordEncoder.matches("password123", "$2a$10$hashedpassword"))
            .thenReturn(true);
        when(jwtUtil.generarToken("test@smartlogix.com", "USER"))
            .thenReturn("jwt-token-generado");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token-generado", response.getToken());
        assertEquals("USER", response.getRol());
        assertEquals("test@smartlogix.com", response.getEmail());
    }

    @Test
    @DisplayName("login: usuario inexistente debe lanzar RuntimeException")
    void login_usuarioNoExiste_debeLanzarExcepcion() {
        when(usuarioRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> authService.login(loginRequest));

        assertEquals("Usuario no encontrado", ex.getMessage());
        // Nunca debe intentarse generar token si el usuario no existe
        verifyNoInteractions(jwtUtil);
    }

    @Test
    @DisplayName("login: contraseña incorrecta debe lanzar RuntimeException")
    void login_contrasenaIncorrecta_debeLanzarExcepcion() {
        when(usuarioRepository.findByEmail("test@smartlogix.com"))
            .thenReturn(Optional.of(usuarioMock));
        when(passwordEncoder.matches(anyString(), anyString()))
            .thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> authService.login(loginRequest));

        assertEquals("Contraseña incorrecta", ex.getMessage());
        verifyNoInteractions(jwtUtil);
    }

    @Test
    @DisplayName("login: se debe llamar findByEmail exactamente una vez")
    void login_debeConsultarRepositorioUnaVez() {
        when(usuarioRepository.findByEmail("test@smartlogix.com"))
            .thenReturn(Optional.of(usuarioMock));
        when(passwordEncoder.matches(anyString(), anyString()))
            .thenReturn(true);
        when(jwtUtil.generarToken(anyString(), anyString()))
            .thenReturn("token");

        authService.login(loginRequest);

        verify(usuarioRepository, times(1)).findByEmail("test@smartlogix.com");
    }

    // ── registro ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("registro: debe codificar la contraseña antes de guardar")
    void registro_debeEncriptarContrasenaAntesDeGuardar() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("nuevo@smartlogix.com");
        nuevoUsuario.setPassword("plaintext");
        nuevoUsuario.setRol("USER");

        when(passwordEncoder.encode("plaintext")).thenReturn("$2a$10$encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(nuevoUsuario);

        authService.registro(nuevoUsuario);

        // La contraseña debe haber sido reemplazada por el hash
        assertEquals("$2a$10$encoded", nuevoUsuario.getPassword());
        verify(passwordEncoder, times(1)).encode("plaintext");
        verify(usuarioRepository, times(1)).save(nuevoUsuario);
    }

    @Test
    @DisplayName("registro: debe llamar save en el repositorio")
    void registro_debeGuardarUsuarioEnRepositorio() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("otro@smartlogix.com");
        nuevoUsuario.setPassword("pass");

        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(usuarioRepository.save(any())).thenReturn(nuevoUsuario);

        authService.registro(nuevoUsuario);

        verify(usuarioRepository).save(nuevoUsuario);
    }

    // ── validarToken ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("validarToken: token válido debe retornar true")
    void validarToken_tokenValido_debeRetornarTrue() {
        when(jwtUtil.validarToken("token-valido")).thenReturn(true);

        assertTrue(authService.validarToken("token-valido"));
        verify(jwtUtil).validarToken("token-valido");
    }

    @Test
    @DisplayName("validarToken: token inválido debe retornar false")
    void validarToken_tokenInvalido_debeRetornarFalse() {
        when(jwtUtil.validarToken("token-invalido")).thenReturn(false);

        assertFalse(authService.validarToken("token-invalido"));
    }
}