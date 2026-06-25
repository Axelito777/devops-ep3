package com.smartlogix.ms_clientes;

import com.smartlogix.ms_clientes.model.Cliente;
import com.smartlogix.ms_clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos de prueba: crea clientes de ejemplo al arrancar
 * la aplicación si la base de datos está vacía.
 *
 * @author SmartLogix Team
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    /**
     * Crea un conjunto de clientes de ejemplo si el repositorio no
     * contiene registros, para facilitar pruebas manuales del servicio.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    @Override
    public void run(String... args) {
        if (clienteRepository.count() == 0) {
            clienteRepository.save(crearCliente("Juan Pérez", "12345678-9", "juan@smartlogix.com", "912345678", "Av. Principal 123"));
            clienteRepository.save(crearCliente("María López", "98765432-1", "maria@smartlogix.com", "987654321", "Calle Sur 456"));
            clienteRepository.save(crearCliente("Carlos Soto", "11111111-1", "carlos@smartlogix.com", "911111111", "Pasaje Norte 789"));
            System.out.println("✅ Clientes de prueba creados");
        }
    }

    /**
     * Construye una entidad {@link Cliente} con los datos provistos.
     *
     * @param nombre    nombre completo del cliente
     * @param rut       RUT del cliente
     * @param email     correo electrónico del cliente
     * @param telefono  teléfono de contacto del cliente
     * @param direccion dirección del cliente
     * @return la entidad {@link Cliente} construida, aún no persistida
     */
    private Cliente crearCliente(String nombre, String rut, String email, String telefono, String direccion) {
        Cliente c = new Cliente();
        c.setNombre(nombre);
        c.setRut(rut);
        c.setEmail(email);
        c.setTelefono(telefono);
        c.setDireccion(direccion);
        return c;
    }
}
