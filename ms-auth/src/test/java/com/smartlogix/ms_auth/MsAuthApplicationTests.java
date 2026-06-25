package com.smartlogix.ms_auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
/**
 * Prueba de integración que verifica que el contexto de Spring Boot
 * del microservicio de autenticación carga correctamente.
 *
 * @author SmartLogix Team
 */
class MsAuthApplicationTests {

    @Test
    void contextLoads() {
    }
}