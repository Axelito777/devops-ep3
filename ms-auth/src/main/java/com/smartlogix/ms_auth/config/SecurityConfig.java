package com.smartlogix.ms_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad del microservicio de autenticación.
 * <p>
 * Define la cadena de filtros HTTP en modo stateless (sin sesiones, apto
 * para JWT) y expone el codificador de contraseñas usado por
 * {@code AuthService}.
 * </p>
 *
 * @author SmartLogix Team
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad HTTP: deshabilita CORS y CSRF,
     * establece la política de sesión como stateless y permite acceso público
     * a los endpoints de autenticación, manejo de errores y documentación Swagger,
     * exigiendo autenticación para el resto de las solicitudes.
     *
     * @param http builder de configuración de seguridad HTTP provisto por Spring Security
     * @return la cadena de filtros de seguridad construida
     * @throws Exception si ocurre un error al construir la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**", "/error",
                    "/swagger-ui/**", "/swagger-ui.html",
                    "/v3/api-docs/**", "/v3/api-docs"
                ).permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }

    /**
     * Expone el codificador de contraseñas utilizado en el registro y
     * validación de credenciales de usuarios.
     *
     * @return una instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
