package com.smartlogix.ms_inventario;

import com.smartlogix.ms_pedidos.MsPedidosApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MsPedidosApplication.class)
/**
 * Prueba de integración que verifica que el contexto de Spring Boot
 * del microservicio de pedidos ({@link MsPedidosApplication}) carga
 * correctamente.
 *
 * @author SmartLogix Team
 */
class MsInventarioApplicationTests {

	@Test
	void contextLoads() {
	}

}
