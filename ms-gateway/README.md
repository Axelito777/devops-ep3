# ms-gateway

API Gateway de SmartLogix basado en Spring Cloud Gateway. Punto de entrada único para todas las peticiones del frontend. Valida tokens JWT, aplica CORS y enruta las solicitudes a los microservicios correspondientes mediante balanceo de carga con Eureka.

## Puerto

`8080`

## Tabla de rutas

| Prefijo | Microservicio destino | Puerto destino |
|---------|-----------------------|---------------|
| `/api/auth/**` | ms-auth | 8081 |
| `/api/clientes/**` | ms-clientes | 8082 |
| `/api/inventario/**` | ms-inventario | 8083 |
| `/api/pedidos/**` | ms-pedidos | 8084 |
| `/api/envios/**` | ms-envios | 8085 |
| `/api/proveedores/**` | ms-proveedores | 8086 |
| `/api/pagos/**` | ms-pagos | 8087 |
| `/api/notificaciones/**` | ms-notificaciones | 8088 |
| `/api/reportes/**` | ms-reportes | 8089 |

## Rutas públicas (sin JWT)

- `POST /api/auth/login`
- `POST /api/auth/registro`
- Peticiones `OPTIONS` (preflight CORS)

Todas las demás rutas requieren el header `Authorization: Bearer <token>`.

## CORS

El gateway permite peticiones desde:
- `http://localhost:5173` (Vite dev server)
- `http://localhost:5174`

## Ejecución local con Maven

```bash
cd ms-gateway
./mvnw spring-boot:run
```

## Ejecución con Docker (desde la raíz de Smartlogix)

```bash
docker-compose up --build ms-gateway
```

## Variables de configuración (`application.properties`)

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `JWT_SECRET` | Clave secreta para validar tokens JWT | Requerida vía env var |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | URL del servidor Eureka | `http://localhost:8761/eureka/` |

## Dependencias principales

- Spring Cloud Gateway (WebFlux)
- Spring Cloud Netflix Eureka Client
- JJWT 0.11.5 (validación de tokens)
