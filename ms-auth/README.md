# ms-auth

Microservicio de autenticación y autorización de SmartLogix. Gestiona el registro de usuarios, login con JWT y validación de tokens.

## Puerto

`8081`

## Endpoints

| Método | Ruta | Descripción | Auth requerida |
|--------|------|-------------|----------------|
| `POST` | `/api/auth/login` | Login. Devuelve JWT, rol y email | No |
| `POST` | `/api/auth/registro` | Registra un nuevo usuario | No |
| `GET` | `/api/auth/validar` | Valida un token JWT | Sí (`Authorization: Bearer <token>`) |

### Ejemplos

**Login:**
```json
POST /api/auth/login
{
  "email": "usuario@smartlogix.com",
  "password": "contraseña"
}
```

**Registro:**
```json
POST /api/auth/registro
{
  "email": "nuevo@smartlogix.com",
  "password": "contraseña",
  "rol": "USER"
}
```

## Swagger UI

Disponible en: `http://localhost:8081/swagger-ui/index.html`

## Ejecución local con Maven

```bash
cd ms-auth
./mvnw spring-boot:run
```

## Ejecución con Docker (desde la raíz de Smartlogix)

```bash
docker-compose up --build ms-auth
```

## Variables de configuración (`application.properties`)

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL de conexión PostgreSQL | Supabase pooler |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos | `postgres.gkcyyzzaizzuglmyetpe` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos | — |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | Valor de desarrollo incluido |
| `jwt.expiration` | Expiración del token en ms | `86400000` (24 h) |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | URL del servidor Eureka | `http://localhost:8761/eureka/` |

## Dependencias principales

- Spring Boot 3.2.0
- Spring Security + JWT (JJWT 0.11.5)
- Spring Data JPA + PostgreSQL
- Spring Cloud Netflix Eureka Client
- Springdoc OpenAPI (Swagger)
