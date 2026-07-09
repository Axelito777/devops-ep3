package com.smartlogix.ms_clientes.service;

import com.smartlogix.ms_clientes.dto.ClienteRequest;
import com.smartlogix.ms_clientes.dto.ClienteResponse;
import com.smartlogix.ms_clientes.model.Cliente;
import com.smartlogix.ms_clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.smartlogix.ms_clientes.client.PedidosClient;
import com.smartlogix.ms_clientes.dto.PedidoResponse;

/**
 * Servicio de negocio para la gestión de clientes.
 * <p>
 * Implementa las operaciones CRUD sobre la entidad {@link com.smartlogix.ms_clientes.model.Cliente}
 * y delega en {@link PedidosClient} para obtener los pedidos de un cliente desde {@code ms-pedidos}.
 * </p>
 *
 * @author SmartLogix Team
 */
@Service
@RequiredArgsConstructor
public class ClienteService {

    private static final String CLIENTE_NO_ENCONTRADO = "Cliente no encontrado";

    private final ClienteRepository clienteRepository;

    /**
     * Retorna la lista de todos los clientes registrados.
     *
     * @return lista de {@link ClienteResponse}; vacía si no hay clientes
     */
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /**
     * Obtiene un cliente por su identificador único.
     *
     * @param id identificador UUID del cliente
     * @return {@link ClienteResponse} con los datos del cliente
     * @throws RuntimeException si no existe un cliente con el id indicado
     */
    public ClienteResponse obtener(String id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(CLIENTE_NO_ENCONTRADO));
        return convertirAResponse(cliente);
    }

    /**
     * Crea y persiste un nuevo cliente tras verificar unicidad de email y RUT.
     *
     * @param request datos del nuevo cliente
     * @return {@link ClienteResponse} del cliente recién creado
     * @throws RuntimeException si el email o el RUT ya están registrados
     */
    public ClienteResponse crear(ClienteRequest request) {
        // Verifica que no exista el email
        if (clienteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        // Verifica que no exista el rut
        if (clienteRepository.findByRut(request.getRut()).isPresent()) {
            throw new RuntimeException("El rut ya está registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setRut(request.getRut());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());

        clienteRepository.save(cliente);
        return convertirAResponse(cliente);
    }

    /**
     * Actualiza los datos de contacto de un cliente existente.
     *
     * @param id      identificador UUID del cliente
     * @param request nuevos datos del cliente
     * @return {@link ClienteResponse} con los datos actualizados
     * @throws RuntimeException si no existe un cliente con el id indicado
     */
    public ClienteResponse actualizar(String id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> 
                    new RuntimeException(CLIENTE_NO_ENCONTRADO));

        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());

        clienteRepository.save(cliente);
        return convertirAResponse(cliente);
    }

    private ClienteResponse convertirAResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getRut(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getCreatedAt()
        );
    }
    private final PedidosClient pedidosClient;

    /**
     * Obtiene los pedidos de un cliente consultando {@code ms-pedidos} vía Feign.
     *
     * @param clienteId identificador UUID del cliente
     * @return lista de {@link PedidoResponse} asociados al cliente
     * @throws RuntimeException si no existe un cliente con el id indicado
     */
    public List<PedidoResponse> getPedidosByCliente(String clienteId) {
    // Verifica que el cliente existe
    clienteRepository.findById(clienteId)
            .orElseThrow(() -> 
                new RuntimeException(CLIENTE_NO_ENCONTRADO));
    
    return pedidosClient.getPedidosByCliente(clienteId);
}
}
