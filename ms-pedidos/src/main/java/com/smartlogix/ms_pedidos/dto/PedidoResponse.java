package com.smartlogix.ms_pedidos.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida con los datos públicos de un pedido.
 *
 * @author SmartLogix Team
 */
@Data
@AllArgsConstructor
public class PedidoResponse {
    private String id;
    private String clienteId;
    private String estado;
    private String tipo;
    private BigDecimal total;
    private LocalDateTime createdAt;
}