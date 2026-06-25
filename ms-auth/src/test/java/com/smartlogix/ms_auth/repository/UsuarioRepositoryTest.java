package com.smartlogix.ms_auth.repository;

import com.smartlogix.ms_auth.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests para UsuarioRepository.
 * @DataJpaTest levanta solo la capa JPA con H2 en memoria.
 * No necesita el contexto completo de Spring Boot.
 */
@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioGuardado;

    @BeforeEach
    void setUp() {
        // Limpia la BD H2 y crea un usuario base para los tests
        usuarioRepository.deleteAll();

        Usuario u = new Usuario();
        u.setEmail("repo@smartlogix.com");
        u.setPassword("$2a$10$hashed");
        u.setRol("USER");
        usuarioGuardado = usuarioRepository.save(u);
    }

    // ── findByEmail ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("findByEmail: email existente debe retornar el usuario")
    void findByEmail_emailExistente_debeRetornarUsuario() {
        Optional<Usuario> resultado = usuarioRepository
            .findByEmail("repo@smartlogix.com");

        assertTrue(resultado.isPresent());
        assertEquals("repo@smartlogix.com", resultado.get().getEmail());
        assertEquals("USER", resultado.get().getRol());
    }

    @Test
    @DisplayName("findByEmail: email inexistente debe retornar Optional vacío")
    void findByEmail_emailNoExistente_debeRetornarVacio() {
        Optional<Usuario> resultado = usuarioRepository
            .findByEmail("noexiste@smartlogix.com");

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("findByEmail: búsqueda es case-sensitive")
    void findByEmail_emailEnMayusculas_debeRetornarVacio() {
        Optional<Usuario> resultado = usuarioRepository
            .findByEmail("REPO@SMARTLOGIX.COM");

        // JPA/SQL es case-sensitive por defecto en H2 para este tipo de query
        assertFalse(resultado.isPresent());
    }

    // ── save ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("save: debe asignar un ID autogenerado al guardar")
    void save_debeAsignarIdAutogenerado() {
        Usuario nuevo = new Usuario();
        nuevo.setEmail("nuevo@smartlogix.com");
        nuevo.setPassword("hashed");
        nuevo.setRol("ADMIN");

        Usuario guardado = usuarioRepository.save(nuevo);

        assertNotNull(guardado.getId());
        assertFalse(guardado.getId().isBlank());
    }

    @Test
    @DisplayName("save: @PrePersist debe asignar createdAt y updatedAt automáticamente")
    void save_debeAsignarFechasAutomaticamente() {
        Usuario nuevo = new Usuario();
        nuevo.setEmail("fechas@smartlogix.com");
        nuevo.setPassword("hashed");
        nuevo.setRol("USER");

        Usuario guardado = usuarioRepository.save(nuevo);

        assertNotNull(guardado.getCreatedAt(),  "createdAt no debe ser null");
        assertNotNull(guardado.getUpdatedAt(),  "updatedAt no debe ser null");
    }

    @Test
    @DisplayName("save: email duplicado debe lanzar excepción (constraint unique)")
    void save_emailDuplicado_debeLanzarExcepcion() {
        Usuario duplicado = new Usuario();
        duplicado.setEmail("repo@smartlogix.com"); // mismo email que setUp
        duplicado.setPassword("otrapass");
        duplicado.setRol("USER");

        assertThrows(Exception.class,
            () -> usuarioRepository.saveAndFlush(duplicado),
            "Guardar un email duplicado debe violar la constraint UNIQUE");
    }

    // ── findAll / count ───────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll: debe retornar exactamente los usuarios guardados")
    void findAll_debeRetornarTodosLosUsuarios() {
        Usuario extra = new Usuario();
        extra.setEmail("extra@smartlogix.com");
        extra.setPassword("hashed");
        extra.setRol("USER");
        usuarioRepository.save(extra);

        assertEquals(2, usuarioRepository.findAll().size());
    }

    @Test
    @DisplayName("deleteAll: después de borrar, el repositorio debe estar vacío")
    void deleteAll_debeDejarRepositorioVacio() {
        usuarioRepository.deleteAll();

        assertEquals(0, usuarioRepository.count());
    }
}