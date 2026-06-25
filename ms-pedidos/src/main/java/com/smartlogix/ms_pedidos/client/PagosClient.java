package com.smartlogix.ms_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

/**
 * Cliente Feign para procesar el pago de un pedido a través del
 * microservicio ms-pagos.
 *
 * @author SmartLogix Team
 */
@FeignClient(name = "ms-pagos")
public interface PagosClient {

    /**
     * Solicita el procesamiento de un pago asociado a un pedido.
     *
     * @param pago datos del pago a procesar
     */
    @PostMapping("/api/pagos/procesar")
    void procesarPago(@RequestBody Map<String, Object> pago);
}