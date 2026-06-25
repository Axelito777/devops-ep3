package com.smartlogix.ms_gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de {@code JwtFilter} usando un mock de {@link JwtUtil}.
 *
 * @author SmartLogix Team
 */
@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock private JwtUtil jwtUtil;
    @InjectMocks private JwtFilter jwtFilter;

    private static final String SECRET =
            "smartlogix-secret-key-2024-super-segura-para-produccion-abc123";

    private String generarToken() {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.builder()
                .setSubject("user@test.com")
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
                .signWith(key)
                .compact();
    }

    @Test
    void filter_optionsRequest_pasaDirectamente() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.options("/api/clientes").build());
        WebFilterChain chain = ex -> Mono.empty();

        jwtFilter.filter(exchange, chain).block();

        assertNull(exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_rutaPublicaLogin_pasaDirectamente() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/api/auth/login").build());
        WebFilterChain chain = ex -> Mono.empty();

        jwtFilter.filter(exchange, chain).block();

        assertNull(exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_rutaPublicaRegistro_pasaDirectamente() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/api/auth/registro").build());
        WebFilterChain chain = ex -> Mono.empty();

        jwtFilter.filter(exchange, chain).block();

        assertNull(exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_sinAuthHeader_retorna401() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/clientes").build());
        WebFilterChain chain = mock(WebFilterChain.class);

        jwtFilter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any());
    }

    @Test
    void filter_authHeaderSinBearer_retorna401() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/clientes")
                        .header("Authorization", "Basic abc123")
                        .build());
        WebFilterChain chain = mock(WebFilterChain.class);

        jwtFilter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_tokenInvalido_retorna401() {
        when(jwtUtil.validarToken("token-invalido")).thenReturn(false);
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/clientes")
                        .header("Authorization", "Bearer token-invalido")
                        .build());
        WebFilterChain chain = mock(WebFilterChain.class);

        jwtFilter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_tokenValido_pasaAlSiguienteFiltro() {
        String token = generarToken();
        when(jwtUtil.validarToken(token)).thenReturn(true);
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/clientes")
                        .header("Authorization", "Bearer " + token)
                        .build());
        WebFilterChain chain = ex -> Mono.empty();

        jwtFilter.filter(exchange, chain).block();

        assertNull(exchange.getResponse().getStatusCode());
    }
}
