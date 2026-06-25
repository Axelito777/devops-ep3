package com.smartlogix.ms_auth.repository;

import com.smartlogix.ms_auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link com.smartlogix.ms_auth.model.Usuario}.
 * <p>
 * Proporciona las operaciones CRUD estándar heredadas de {@link JpaRepository}
 * más la búsqueda por email utilizada en el flujo de autenticación.
 * </p>
 *
 * @author SmartLogix Team
 */
public interface UsuarioRepository
    extends JpaRepository<Usuario, String> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email dirección de email a buscar
     * @return un {@link Optional} con el usuario si existe, o vacío si no se encuentra
     */
    Optional<Usuario> findByEmail(String email);
}