# ms-clientes

Microservicio de gestión de clientes de SmartLogix. Permite crear, consultar y actualizar clientes, y obtener sus pedidos asociados mediante integración con ms-pedidos vía Feign.

## Puerto

`8082`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/clientes` | Lista todos los clientes |
| `POST` | `/api/clientes` | Crea un nuevo cliente |
| `GET` | `/api/clientes/{id}` | Obtiene un cliente por ID |
| `PUT` | `/api/clientes/{id}` | Actualiza datos de un cliente |
| `GET` | `/api/clientes/{id}/pedidos` | Obtiene los pedidos de un cliente |

### Ejemplo de creación

```json
POST /api/clientes
{
  "nombre": "Juan Pérez",
  "rut": "12345678-9",
  "email": "juan@empresa.cl",
  "telefono": "+56912345678",
  "direccion": "Av. Providencia 1234"
}
```

Todos los endpoints requieren `Authorization: Bearer <token>` (validado por el gateway).

## Swagger UI

Disponible en: `http://localhost:8082/swagger-ui/index.html`

## Ejecución local con Maven

```bash
cd ms-clientes
./mvnw spring-boot:run
```

## Ejecución con Docker (desde la raíz de Smartlogix)

```bash
docker-compose up --build ms-clientes
```

## Variables de configuración (`application.properties`)

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL de conexión PostgreSQL | Supabase pooler |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos | `postgres.gkcyyzzaizzuglmyetpe` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos | — |
| `JWT_SECRET` | Clave secreta JWT (usada por el gateway) | Valor de desarrollo incluido |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | URL del servidor Eureka | `http://localhost:8761/eureka/` |

## Dependencias principales

- Spring Boot 3.2.0
- Spring Data JPA + PostgreSQL
- Spring Cloud Netflix Eureka Client
- Spring Cloud OpenFeign (integración con ms-pedidos)
- Spring Validation
- Springdoc OpenAPI (Swagger)
