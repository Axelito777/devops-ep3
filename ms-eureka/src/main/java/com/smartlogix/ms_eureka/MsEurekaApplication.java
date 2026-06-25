package com.smartlogix.ms_eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Punto de entrada del servidor de descubrimiento de servicios (Eureka Server).
 * <p>
 * Actúa como registro central donde todos los microservicios de SmartLogix
 * se registran y descubren entre sí. Escucha en el puerto {@code 8761} por defecto.
 * </p>
 *
 * @author SmartLogix Team
 */
@SpringBootApplication
@EnableEurekaServer
public class MsEurekaApplication {

	/**
	 * Inicia el servidor Eureka.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(MsEurekaApplication.class, args);
	}

}
