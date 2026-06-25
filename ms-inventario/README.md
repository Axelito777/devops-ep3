# ms-inventario

Microservicio de gestión de inventario de SmartLogix. Administra el catálogo de productos, controla el stock y alerta sobre productos bajo el mínimo configurado.

## Puerto

`8083`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/inventario/productos` | Lista todos los productos |
| `GET` | `/api/inventario/productos/{id}` | Obtiene un producto por ID |
| `POST` | `/api/inventario/productos` | Crea un nuevo producto |
| `PUT` | `/api/inventario/productos/{id}` | Actualiza un producto |
| `PUT` | `/api/inventario/productos/{id}/stock?cantidad=N` | Ajusta el stock (positivo suma, negativo resta) |
| `GET` | `/api/inventario/productos/bajo-stock` | Lista productos con stock ≤ 10 unidades |
| `DELETE` | `/api/inventario/productos/{id}` | Elimina un producto |

### Ejemplo de creación

```json
POST /api/inventario/productos
{
  "nombre": "Tornillo M8",
  "descripcion": "Tornillo Allen M8 x 30mm",
  "precio": 150.00,
  "stock": 500,
  "stockMinimo": 50,
  "bodegaId": "bodega-central",
  "proveedorId": "proveedor-uuid"
}
```

### Ejemplo de ajuste de stock

```
PUT /api/inventario/productos/{id}/stock?cantidad=-10
```

Descuenta 10 unidades. Retorna `RuntimeException` si el stock resultante es negativo.

Todos los endpoints requieren `Authorization: Bearer <token>` (validado por el gateway).

## Swagger UI

Disponible en: `http://localhost:8083/swagger-ui/index.html`

## Ejecución local con Maven

```bash
cd ms-inventario
./mvnw spring-boot:run
```

## Ejecución con Docker (desde la raíz de Smartlogix)

```bash
docker-compose up --build ms-inventario
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
- Spring Cloud OpenFeign
- Springdoc OpenAPI (Swagger)
