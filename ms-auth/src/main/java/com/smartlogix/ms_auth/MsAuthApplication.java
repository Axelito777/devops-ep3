package com.smartlogix.ms_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio de autenticación de SmartLogix.
 * <p>
 * Gestiona el registro de usuarios, inicio de sesión y validación de tokens JWT.
 * </p>
 *
 * @author SmartLogix Team
 */
@SpringBootApplication
public class MsAuthApplication {

	/**
	 * Inicia el microservicio de autenticación.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(MsAuthApplication.class, args);
	}

}
