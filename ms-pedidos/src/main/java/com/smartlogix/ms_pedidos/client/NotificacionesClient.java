package com.smartlogix.ms_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

/**
 * Cliente Feign para enviar notificaciones a través del microservicio
 * ms-notificaciones cuando cambia el estado de un pedido.
 *
 * @author SmartLogix Team
 */
@FeignClient(name = "ms-notificaciones")
public interface NotificacionesClient {

    /**
     * Envía una notificación al microservicio ms-notificaciones.
     *
     * @param notificacion datos de la notificación a enviar
     */
    @PostMapping("/api/notificaciones/enviar")
    void enviarNotificacion(@RequestBody Map<String, Object> notificacion);
}