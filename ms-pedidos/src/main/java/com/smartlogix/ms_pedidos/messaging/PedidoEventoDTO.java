package com.smartlogix.ms_pedidos.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO del evento de pedido creado, publicado en RabbitMQ para que lo
 * consuma el microservicio ms-notificaciones.
 *
 * @author SmartLogix Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEventoDTO {
    private String pedidoId;
    private String clienteId;
    private BigDecimal total;
}
