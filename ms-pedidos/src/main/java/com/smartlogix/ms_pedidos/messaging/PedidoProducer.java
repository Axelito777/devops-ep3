package com.smartlogix.ms_pedidos.messaging;

import com.smartlogix.ms_pedidos.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Productor de eventos de pedidos: publica en RabbitMQ el evento de
 * pedido creado para que otros microservicios (como ms-notificaciones)
 * reaccionen a él.
 *
 * @author SmartLogix Team
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publica el evento de pedido creado en el exchange de pedidos.
     *
     * @param evento datos del pedido recién creado
     */
    public void enviarPedidoCreado(PedidoEventoDTO evento) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_PEDIDOS,
            RabbitMQConfig.ROUTING_KEY_PEDIDO_CREADO,
            evento
        );
        log.info("Evento pedido.creado enviado para pedido {}", evento.getPedidoId());
    }
}
