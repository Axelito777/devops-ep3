package com.smartlogix.ms_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Configuración de CORS para el API Gateway.
 * <p>
 * Permite solicitudes desde los clientes frontend en localhost:5173 y localhost:5174,
 * habilitando todos los métodos HTTP y cabeceras con soporte de credenciales.
 * </p>
 *
 * @author SmartLogix Team
 */
@Configuration
public class GatewayConfig {

    /**
     * Registra el filtro CORS global que aplica a todas las rutas del gateway ({@code /**}).
     *
     * @return {@link CorsWebFilter} configurado con los orígenes y métodos permitidos
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:5174");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}