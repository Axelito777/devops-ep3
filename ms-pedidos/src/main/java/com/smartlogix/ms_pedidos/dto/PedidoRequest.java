package com.smartlogix.ms_pedidos.dto;

import lombok.Data;
import java.util.List;

/**
 * DTO de entrada con los datos requeridos para crear un pedido,
 * incluyendo la lista de productos solicitados.
 *
 * @author SmartLogix Team
 */
@Data
public class PedidoRequest {
    private String clienteId;
    private String tipo;
    private List<DetallePedidoRequest> productos;
}