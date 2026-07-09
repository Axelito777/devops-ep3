package com.smartlogix.ms_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOriginPatterns(java.util.List.of("*"));
                config.setAllowedMethods(java.util.List.of("GET","POST","PUT","DELETE","OPTIONS"));
                config.setAllowedHeaders(java.util.List.of("*"));
                config.setAllowCredentials(false);
                return config;
            }))
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll()
            )
            .build();
    }
}
