# ms-eureka

Servidor de descubrimiento de servicios de SmartLogix basado en Netflix Eureka. Todos los microservicios se registran aquí al arrancar, y el gateway lo consulta para resolver las rutas con balanceo de carga (`lb://`).

## Puerto

`8761`

## Dashboard

Interfaz web de Eureka disponible en: `http://localhost:8761`

Muestra en tiempo real los microservicios registrados, su estado (`UP`/`DOWN`) y las instancias activas.

## Endpoints de Eureka (API interna)

| Ruta | Descripción |
|------|-------------|
| `GET /eureka/apps` | Lista todas las aplicaciones registradas (XML) |
| `GET /actuator/health` | Health check (usado por Docker Compose) |

Estos endpoints son consumidos automáticamente por los clientes Eureka — no están pensados para uso directo.

## Ejecución local con Maven

```bash
cd ms-eureka
./mvnw spring-boot:run
```

## Ejecución con Docker (desde la raíz de Smartlogix)

```bash
docker-compose up --build ms-eureka
```

## Variables de configuración (`application.properties`)

| Propiedad | Valor | Descripción |
|-----------|-------|-------------|
| `server.port` | `8761` | Puerto del servidor |
| `eureka.client.register-with-eureka` | `false` | Eureka no se registra a sí mismo |
| `eureka.client.fetch-registry` | `false` | No descarga el registro local |
| `eureka.server.wait-time-in-ms-when-sync-empty` | `0` | Arranque inmediato sin esperar réplicas |

## Notas

- Debe arrancar **antes** que cualquier otro microservicio. En Docker Compose los demás servicios tienen `depends_on: ms-eureka: condition: service_healthy`.
- El health check de Docker usa `GET /actuator/health` (requiere `spring-boot-starter-actuator`).
