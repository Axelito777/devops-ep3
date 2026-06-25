package com.smartlogix.ms_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Punto de entrada del API Gateway de SmartLogix.
 * <p>
 * Actúa como punto de entrada único para todos los microservicios,
 * aplicando validación JWT mediante {@link com.smartlogix.ms_gateway.security.JwtFilter}
 * y configurando CORS para clientes frontend.
 * </p>
 *
 * @author SmartLogix Team
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MsGatewayApplication {

    /**
     * Inicia el API Gateway.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(MsGatewayApplication.class, args);
    }
}
