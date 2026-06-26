package com.smartlogix.ms_gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filtro reactivo de validación JWT para el API Gateway.
 * <p>
 * Intercepta todas las peticiones entrantes y verifica que la cabecera
 * {@code Authorization} contenga un token JWT válido, salvo para las rutas
 * públicas de autenticación y las peticiones preflight CORS ({@code OPTIONS}).
 * Responde con {@code 401 Unauthorized} si el token está ausente o es inválido.
 * </p>
 *
 * @author SmartLogix Team
 */
@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    private static final String[] RUTAS_PUBLICAS = {
        "/api/auth/login",
        "/api/auth/registro",
        "/actuator",
        "/actuator/health",
        "/actuator/health/readiness",
        "/actuator/health/liveness",
        "/actuator/info"
    };

    /**
     * Aplica la validación JWT a cada petición entrante.
     * <p>
     * Deja pasar sin validar las peticiones {@code OPTIONS} (preflight CORS)
     * y las rutas públicas definidas en {@code RUTAS_PUBLICAS}.
     * Para el resto, extrae y valida el token de la cabecera {@code Authorization}.
     * </p>
     *
     * @param exchange objeto de intercambio HTTP reactivo con request y response
     * @param chain    cadena de filtros a continuar si la validación es exitosa
     * @return {@link Mono} vacío; completa normalmente si el token es válido,
     *         o con {@code 401} si está ausente o es inválido
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // ✅ Dejar pasar preflight CORS
        if (HttpMethod.OPTIONS.equals(exchange.getRequest().getMethod())) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getURI().getPath();

        for (String ruta : RUTAS_PUBLICAS) {
            if (path.equals(ruta)) {
                return chain.filter(exchange);
            }
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.replace("Bearer ", "");

        if (!jwtUtil.validarToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}