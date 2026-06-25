# ms-pedidos

Microservicio de gestión de pedidos de SmartLogix. Orquesta la creación de pedidos coordinando con ms-inventario (descuento de stock), ms-pagos (procesamiento), ms-envios (creación de envío) y ms-notificaciones (aviso al cliente).

## Puerto

`8084`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/pedidos` | Lista todos los pedidos |
| `GET` | `/api/pedidos/{id}` | Obtiene un pedido por ID |
| `POST` | `/api/pedidos` | Crea un nuevo pedido (orquesta todos los servicios) |
| `PUT` | `/api/pedidos/{id}/estado` | Actualiza el estado de un pedido |

### Ejemplo de creación

```json
POST /api/pedidos
{
  "clienteId": "uuid-del-cliente",
  "tipo": "NORMAL",
  "productos": [
    { "productoId": "uuid-producto-1", "cantidad": 3 },
    { "productoId": "uuid-producto-2", "cantidad": 1 }
  ]
}
```

El estado inicial del pedido es `PENDIENTE` (asignado automáticamente).

### Ejemplo de actualización de estado

```json
PUT /api/pedidos/{id}/estado
{
  "estado": "ENTREGADO"
}
```

Al actualizar el estado se envía automáticamente una notificación al cliente.

Todos los endpoints requieren `Authorization: Bearer <token>` (validado por el gateway).

## Swagger UI

Disponible en: `http://localhost:8084/swagger-ui/index.html`

## Ejecución local con Maven

```bash
cd ms-pedidos
./mvnw spring-boot:run
```

## Ejecución con Docker (desde la raíz de Smartlogix)

```bash
docker-compose up --build ms-pedidos
```

## Variables de configuración (`application.properties`)

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL de conexión PostgreSQL | Supabase pooler |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos | `postgres.gkcyyzzaizzuglmyetpe` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos | — |
| `JWT_SECRET` | Clave secreta JWT | Valor de desarrollo incluido |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | URL del servidor Eureka | `http://localhost:8761/eureka/` |

## Dependencias principales

- Spring Boot 3.2.0
- Spring Data JPA + PostgreSQL
- Spring Cloud Netflix Eureka Client
- Spring Cloud OpenFeign (ms-inventario, ms-pagos, ms-envios, ms-notificaciones)
- Springdoc OpenAPI (Swagger)
