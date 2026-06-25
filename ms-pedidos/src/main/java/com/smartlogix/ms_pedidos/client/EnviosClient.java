package com.smartlogix.ms_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

/**
 * Cliente Feign para crear envíos en el microservicio ms-envios una vez
 * confirmado un pedido.
 *
 * @author SmartLogix Team
 */
@FeignClient(name = "ms-envios")
public interface EnviosClient {

    /**
     * Solicita la creación de un envío para un pedido.
     *
     * @param envio datos del envío a crear
     */
    @PostMapping("/api/envios/crear")
    void crearEnvio(@RequestBody Map<String, Object> envio);
}
