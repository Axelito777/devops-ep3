package com.smartlogix.ms_clientes.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO con los datos de un pedido, recibido desde el microservicio
 * ms-pedidos a través de {@link com.smartlogix.ms_clientes.client.PedidosClient}.
 *
 * @author SmartLogix Team
 */
@Data
public class PedidoResponse {
    private String id;
    private String clienteId;
    private String estado;
    private String tipo;
    private BigDecimal total;
    private LocalDateTime createdAt;
}