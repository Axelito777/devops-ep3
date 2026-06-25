package com.smartlogix.ms_clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Punto de entrada del microservicio de gestión de clientes de SmartLogix.
 * <p>
 * Administra el ciclo de vida de los clientes (creación, consulta y actualización)
 * y se integra con {@code ms-pedidos} vía OpenFeign para consultar sus pedidos.
 * </p>
 *
 * @author SmartLogix Team
 */
@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class MsClientesApplication {

	/**
	 * Inicia el microservicio de clientes.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(MsClientesApplication.class, args);
	}

}
