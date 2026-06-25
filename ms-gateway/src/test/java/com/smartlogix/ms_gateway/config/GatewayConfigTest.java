package com.smartlogix.ms_gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de {@code GatewayConfig}.
 *
 * @author SmartLogix Team
 */
class GatewayConfigTest {

    @Test
    void corsWebFilter_debeCrearFiltroNoNulo() {
        GatewayConfig config = new GatewayConfig();
        CorsWebFilter filter = config.corsWebFilter();
        assertNotNull(filter);
    }
}
