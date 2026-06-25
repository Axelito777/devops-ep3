package com.smartlogix.ms_pedidos.dto;

import lombok.Data;

/**
 * DTO de entrada con un producto y cantidad solicitados dentro de un pedido.
 *
 * @author SmartLogix Team
 */
@Data
public class DetallePedidoRequest {
    private String productoId;
    private Integer cantidad;
}