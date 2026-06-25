package com.smartlogix.ms_clientes.repository;

import com.smartlogix.ms_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link com.smartlogix.ms_clientes.model.Cliente}.
 * <p>
 * Hereda las operaciones CRUD de {@link JpaRepository} y añade búsquedas
 * por email y RUT utilizadas para validar unicidad en el registro.
 * </p>
 *
 * @author SmartLogix Team
 */
public interface ClienteRepository
    extends JpaRepository<Cliente, String> {

    /**
     * Busca un cliente por su dirección de correo electrónico.
     *
     * @param email dirección de email a buscar
     * @return un {@link Optional} con el cliente si existe, o vacío si no se encuentra
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Busca un cliente por su RUT.
     *
     * @param rut RUT del cliente a buscar
     * @return un {@link Optional} con el cliente si existe, o vacío si no se encuentra
     */
    Optional<Cliente> findByRut(String rut);
}