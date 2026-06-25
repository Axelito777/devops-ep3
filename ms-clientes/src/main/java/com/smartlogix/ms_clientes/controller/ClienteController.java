package com.smartlogix.ms_clientes.controller;

import com.smartlogix.ms_clientes.dto.ClienteRequest;
import com.smartlogix.ms_clientes.dto.ClienteResponse;
import com.smartlogix.ms_clientes.dto.PedidoResponse;
import com.smartlogix.ms_clientes.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de clientes.
 * <p>
 * Expone los endpoints CRUD de clientes y la consulta de sus pedidos asociados
 * bajo la ruta base {@code /api/clientes}.
 * </p>
 *
 * @author SmartLogix Team
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Retorna la lista completa de clientes registrados.
     *
     * @return {@code 200 OK} con la lista de {@link ClienteResponse}
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    /**
     * Obtiene un cliente por su identificador único.
     *
     * @param id identificador UUID del cliente
     * @return {@code 200 OK} con el {@link ClienteResponse} encontrado
     * @throws RuntimeException si no existe un cliente con el id indicado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtener(
            @PathVariable String id) {
        return ResponseEntity.ok(clienteService.obtener(id));
    }

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param request datos del cliente a crear; email y RUT deben ser únicos
     * @return {@code 200 OK} con el {@link ClienteResponse} del cliente creado
     * @throws RuntimeException si el email o el RUT ya están registrados
     */
    @PostMapping
    public ResponseEntity<ClienteResponse> crear(
            @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.crear(request));
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id      identificador UUID del cliente a actualizar
     * @param request nuevos datos del cliente
     * @return {@code 200 OK} con el {@link ClienteResponse} actualizado
     * @throws RuntimeException si no existe un cliente con el id indicado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable String id,
            @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(
            clienteService.actualizar(id, request));
    }

    /**
     * Obtiene los pedidos asociados a un cliente consultando {@code ms-pedidos} vía Feign.
     *
     * @param id identificador UUID del cliente
     * @return {@code 200 OK} con la lista de {@link PedidoResponse} del cliente
     * @throws RuntimeException si no existe un cliente con el id indicado
     */
    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoResponse>> getPedidos(@PathVariable String id) {
        return ResponseEntity.ok(clienteService.getPedidosByCliente(id));
    }
}