package com.smartlogix.ms_pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Punto de entrada del microservicio de pedidos de SmartLogix.
 * <p>
 * Orquesta el flujo de creación de pedidos coordinando con
 * {@code ms-inventario}, {@code ms-pagos}, {@code ms-envios}
 * y {@code ms-notificaciones} vía OpenFeign.
 * </p>
 *
 * @author SmartLogix Team
 */
@SpringBootApplication
@EnableFeignClients
public class MsPedidosApplication {

    /**
     * Inicia el microservicio de pedidos.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(MsPedidosApplication.class, args);
    }
}