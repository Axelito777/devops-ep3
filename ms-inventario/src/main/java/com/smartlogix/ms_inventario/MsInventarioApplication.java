package com.smartlogix.ms_inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Punto de entrada del microservicio de inventario de SmartLogix.
 * <p>
 * Gestiona el catálogo de productos, controla el stock disponible
 * y expone alertas de bajo stock para los demás microservicios.
 * </p>
 *
 * @author SmartLogix Team
 */
@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class MsInventarioApplication {

	/**
	 * Inicia el microservicio de inventario.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(MsInventarioApplication.class, args);
	}

}
