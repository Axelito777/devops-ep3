package com.smartlogix.ms_clientes.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartlogix.ms_clientes.dto.PedidoResponse;

/**
 * Cliente Feign para consultar el microservicio ms-pedidos.
 *
 * @author SmartLogix Team
 */
@FeignClient(name = "ms-pedidos")
public interface PedidosClient {

    /**
     * Obtiene los pedidos asociados a un cliente.
     *
     * @param clienteId identificador del cliente
     * @return lista de pedidos del cliente
     */
    @GetMapping("/api/pedidos/cliente/{clienteId}")
    List<PedidoResponse> getPedidosByCliente(@PathVariable String clienteId);
}
