# SmartLogix — EP3 DevOps

Sistema de gestión logística basado en microservicios Spring Boot, desplegado en **AWS EKS** con pipeline CI/CD automatizado mediante **GitHub Actions**, análisis de calidad con **SonarCloud** y monitoreo con **Prometheus + Grafana**.

---

## Descripción del proyecto

SmartLogix es una plataforma de logística empresarial compuesta por **6 microservicios independientes** que gestionan autenticación, clientes, inventario y pedidos. Cada servicio es un módulo Maven autónomo empaquetado en imagen Docker y orquestado en Kubernetes.

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| **ms-eureka** | 8761 | Service discovery (Spring Cloud Netflix Eureka) |
| **ms-gateway** | 8080 | API Gateway + filtro JWT (Spring Cloud Gateway) |
| **ms-auth** | 8081 | Autenticación, registro y validación de tokens JWT |
| **ms-clientes** | 8082 | CRUD de clientes, caché Redis, integración con pedidos |
| **ms-inventario** | 8083 | Gestión de stock, caché Redis, alertas de bajo stock |
| **ms-pedidos** | 8084 | Orquestación de pedidos, mensajería RabbitMQ |

**Stack tecnológico:**

- Java 17 · Spring Boot 3.2.0 · Spring Cloud 2023.0.0
- PostgreSQL (Supabase) · Redis · RabbitMQ
- Docker · Kubernetes · AWS EKS · Amazon ECR
- GitHub Actions · SonarCloud · JaCoCo · Prometheus · Grafana

---

## Arquitectura

```
                         ┌─────────────────────────────────────────────────────┐
                         │              GitHub / Source Control                │
                         │                                                     │
                         │  feature/* ──PR──► main ──push──► CI/CD Pipeline   │
                         └──────────────────────────┬──────────────────────────┘
                                                    │ GitHub Actions
                    ┌───────────────────────────────▼──────────────────────────┐
                    │                   CI/CD Pipeline                         │
                    │                                                          │
                    │  ① Build & Test (Maven)                                 │
                    │  ② SonarCloud Analysis + Quality Gate                   │
                    │  ③ Build Docker images → Push a Amazon ECR              │
                    │  ④ kubectl apply → Deploy en AWS EKS                    │
                    └───────┬───────────────────────┬──────────────────────────┘
                            │                       │
              ┌─────────────▼──────────┐  ┌────────▼───────────────┐
              │     Amazon ECR         │  │      AWS EKS            │
              │  (Container Registry)  │  │  Cluster: devops-ep3    │
              │                        │  │  Region:  us-east-1     │
              │  ms-eureka:sha         │  │  NS:      smartlogix    │
              │  ms-gateway:sha        │  │                         │
              │  ms-auth:sha           │  │  ┌────────────────────┐ │
              │  ms-clientes:sha       │  │  │  ms-eureka  (×2)   │ │
              │  ms-inventario:sha     │  │  │  ms-gateway (×2) ◄─┼─┼── LoadBalancer
              │  ms-pedidos:sha        │  │  │  ms-auth    (×2)   │ │     (Internet)
              └────────────────────────┘  │  │  ms-clientes(×2)   │ │
                                          │  │  ms-inventario(×2) │ │
                                          │  │  ms-pedidos (×2)   │ │
                                          │  └─────────┬──────────┘ │
                                          │            │ HPA (2–5)  │
                                          └────────────┼────────────┘
                                                       │
                              ┌────────────────────────▼──────────────────────┐
                              │              Infraestructura compartida        │
                              │                                                │
                              │  PostgreSQL (Supabase)   Redis   RabbitMQ     │
                              │  Prometheus + Grafana (monitoreo)              │
                              └────────────────────────────────────────────────┘


Flujo de tráfico en producción:

  Cliente HTTP
      │
      ▼
  ms-gateway (LoadBalancer :80)
      │  valida JWT en cada request
      ├──► ms-auth      (ClusterIP :8081)
      ├──► ms-clientes  (ClusterIP :8082) ──► ms-pedidos (Feign)
      ├──► ms-inventario(ClusterIP :8083)
      └──► ms-pedidos   (ClusterIP :8084)
               │
               └──► RabbitMQ (eventos de creación de pedido)
               └──► ms-inventario, ms-pagos, ms-envios, ms-notificaciones (Feign)

  Todos los servicios se registran en ms-eureka para descubrimiento dinámico.
```

---

## Pipeline CI/CD

El pipeline está definido en `.github/workflows/deploy.yml` y se dispara en cada `push` a `main` y en cada `pull_request` hacia `main`.

### Etapas

```
push / pull_request a main
        │
        ▼
┌───────────────────┐
│  1. build-and-test│  (siempre se ejecuta)
│                   │
│  • actions/checkout  fetch-depth=0 (historial completo para Sonar)
│  • setup-java 17 Temurin + caché Maven
│  • mvn verify    → compila, tests unitarios + integración, JaCoCo (≥60%)
│  • mvn sonar:sonar → analiza en SonarCloud + espera Quality Gate
│  • upload-artifact → surefire-reports (7 días)
└─────────┬─────────┘
          │ si OK
          ▼  (solo rama main)
┌───────────────────┐
│  2. docker-push   │
│                   │
│  • Configura credenciales AWS (secrets)
│  • Login a Amazon ECR
│  • Genera IMAGE_TAG = primeros 8 chars del SHA del commit
│  • docker/build-push-action con caché GHA por cada servicio:
│      ms-eureka · ms-gateway · ms-auth
│      ms-clientes · ms-inventario · ms-pedidos
│  • Publica tag :sha + :latest en ECR
└─────────┬─────────┘
          │ si OK
          ▼  (solo rama main, environment: production)
┌───────────────────┐
│  3. deploy        │
│                   │
│  • aws eks update-kubeconfig → apunta al cluster devops-ep3
│  • kubectl apply namespace + configmap
│  • envsubst < secrets.yml → inyecta secrets desde GitHub Secrets
│  • envsubst < k8s/${svc}.yml | kubectl apply (loop 6 servicios)
│  • kubectl rollout status --timeout=300s (espera convergencia)
│  • kubectl get deployments,services,hpa -n smartlogix (resumen final)
└───────────────────┘
```

### Secrets de GitHub Actions requeridos

| Secret | Uso |
|---|---|
| `AWS_ACCESS_KEY_ID` | Credenciales AWS |
| `AWS_SECRET_ACCESS_KEY` | Credenciales AWS |
| `AWS_SESSION_TOKEN` | Credenciales AWS (sesión temporal) |
| `SONAR_TOKEN` | Autenticación SonarCloud |
| `SONAR_PROJECT_KEY` | Clave del proyecto en SonarCloud |
| `SONAR_ORG` | Organización en SonarCloud |
| `SUPABASE_URL` | URL PostgreSQL Supabase |
| `SUPABASE_USERNAME` | Usuario Supabase |
| `SUPABASE_PASSWORD` | Contraseña Supabase |
| `JWT_SECRET` | Clave de firma JWT |
| `RABBITMQ_PASSWORD` | Contraseña RabbitMQ |

### Kubernetes en producción

Cada microservicio despliega con:
- **2 réplicas mínimas** (alta disponibilidad)
- **HorizontalPodAutoscaler** (2–5 pods, umbral CPU 50 %)
- **Readiness + Liveness probes** sobre `/actuator/health`
- **ConfigMap** para variables de entorno no sensibles
- **Secrets** para credenciales (inyectados vía `envsubst`)
- Recursos: `200m CPU / 256Mi RAM` request · `500m CPU / 512Mi RAM` limit

---

## Monitoreo con Prometheus y Grafana

Todos los microservicios exponen métricas de Spring Boot Actuator en `/actuator/prometheus`. Prometheus las recolecta periódicamente y Grafana las visualiza en dashboards.

### Métricas clave monitoreadas

| Métrica | Lo que indica | Decisión técnica que habilita |
|---|---|---|
| `http_server_requests_seconds` | Latencia y tasa de requests por endpoint | Detectar endpoints lentos → revisar queries o aumentar réplicas vía HPA |
| `jvm_memory_used_bytes` | Uso de heap y memoria no-heap por servicio | Ajustar límites de memoria en el Deployment K8s antes de OOMKill |
| `cache_gets_total` (Redis) | Hit/miss rate del caché en clientes e inventario | Si miss rate sube → investigar TTL o invalidación incorrecta |
| `rabbitmq_queue_messages` | Mensajes pendientes en cola de pedidos | Si crece → escalar ms-pedidos o consumidores RabbitMQ |
| `process_cpu_usage` | CPU por pod | Umbral del HPA (50 %) → escalado automático horizontal |
| `hikaricp_connections_active` | Pool de conexiones a PostgreSQL | Si saturado → ajustar `maximumPoolSize` o detectar conexiones filtradas |

### Cómo apoya decisiones técnicas

1. **Escalado reactivo vs. proactivo**: el HPA reacciona cuando `process_cpu_usage` supera 50 %. Grafana permite ver tendencias históricas y anticipar picos (ej. campañas comerciales) para configurar réplicas base adecuadas.

2. **Detección de regresiones de rendimiento**: tras cada deploy el pipeline ejecuta el rollout y Grafana muestra en tiempo real si la latencia P99 de algún endpoint empeora. Si `http_server_requests_seconds{quantile="0.99"}` supera el SLO definido, el equipo puede hacer rollback antes de que afecte a los usuarios.

3. **Validación de caché**: la ratio hit/miss de Redis en ms-clientes y ms-inventario confirma que `@Cacheable` funciona correctamente y reduce carga en Supabase. Una caída brusca del hit rate alerta sobre una invalidación masiva inesperada (`@CacheEvict`).

4. **Capacidad de la base de datos**: las métricas de HikariCP muestran si el pool de conexiones hacia Supabase está saturado, permitiendo decidir aumentar `maximumPoolSize` o habilitar PgBouncer.

5. **Monitoreo de mensajería**: la profundidad de la cola RabbitMQ indica si ms-pedidos procesa eventos al mismo ritmo que se generan, evitando retrasos en notificaciones y envíos.

---

## Calidad y seguridad con SonarQube y branch protection

### SonarCloud — Quality Gate

SonarCloud analiza el código en cada push y pull request. El pipeline **bloquea el merge** si el Quality Gate falla (`-Dsonar.qualitygate.wait=true`).

**Umbrales configurados:**

| Métrica | Umbral mínimo |
|---|---|
| Cobertura de instrucciones (JaCoCo) | ≥ 60 % |
| Nuevas vulnerabilidades bloqueantes/críticas | 0 |
| Security Hotspots sin revisar | 0 |
| Líneas duplicadas | < 3 % |

**Cómo se integra con el código:**

- JaCoCo genera `jacoco.xml` en la fase `verify` de Maven y SonarCloud lo consume.
- Los controladores REST están excluidos del cómputo de cobertura (no tienen lógica de negocio testeble de forma unitaria).
- Los warnings suprimidos con `// NOSONAR` (ej. `csrf.disable()`) están justificados: el CSRF es innecesario en APIs stateless con JWT.
- Las clases de configuración, DTOs y modelos se testean con tests unitarios específicos para mantener la cobertura.

### Branch Protection — `main`

La rama `main` tiene las siguientes reglas habilitadas en GitHub:

- **Pull request obligatorio**: ningún commit puede llegar directo a `main`.
- **1 review aprobatorio requerido** antes de hacer merge.
- **Status checks requeridos**: el job `build-and-test` (que incluye SonarQube) debe estar en verde.
- **Rama no deletable** ni forzable (`--force`).

**Por qué garantiza calidad y seguridad:**

1. Todo el código nuevo pasa por revisión humana → se detectan problemas de diseño que las herramientas no ven.
2. SonarCloud actúa como segunda línea de defensa automática: detecta code smells, duplicaciones, vulnerabilidades conocidas (OWASP) y malas prácticas de seguridad.
3. La combinación hace imposible que código con cobertura insuficiente o vulnerabilidades llegue a producción sin intervención deliberada del equipo.
4. Los secrets de AWS, base de datos y JWT nunca viajan en el código: están en GitHub Secrets y se inyectan en runtime vía Kubernetes Secrets.

### Seguridad de contenedores

- Cada Dockerfile usa **build multi-etapa** (imagen de builder separada del runtime).
- La imagen de runtime es `eclipse-temurin:17-jre-alpine` (superficie de ataque mínima).
- Los contenedores corren como usuario **no-root** (`appuser`) definido en el Dockerfile.
- Amazon ECR tiene **image scanning** habilitado en cada push.

---

## URLs públicas del proyecto

> Las URLs de AWS se asignan dinámicamente por el LoadBalancer de EKS. Consultar la URL actual con:
> ```bash
> kubectl get svc ms-gateway -n smartlogix -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'
> ```

| Recurso | URL |
|---|---|
| **API Gateway** (producción) | `http://<LB-HOSTNAME>/` |
| **Eureka Dashboard** | `http://<LB-HOSTNAME-EUREKA>/` *(solo red interna)* |
| **Swagger ms-auth** | `http://<LB-HOSTNAME>/api/auth/swagger-ui/index.html` |
| **Swagger ms-clientes** | `http://<LB-HOSTNAME>/api/clientes/swagger-ui/index.html` |
| **Swagger ms-inventario** | `http://<LB-HOSTNAME>/api/inventario/swagger-ui/index.html` |
| **Swagger ms-pedidos** | `http://<LB-HOSTNAME>/api/pedidos/swagger-ui/index.html` |
| **SonarCloud** | `https://sonarcloud.io/project/overview?id=<SONAR_PROJECT_KEY>` |
| **GitHub Actions** | `https://github.com/<ORG>/devops-ep3/actions` |

### Endpoints principales de la API

Todos los endpoints (excepto autenticación) requieren `Authorization: Bearer <token>`.

```
POST   /api/auth/login              → Obtiene token JWT
POST   /api/auth/registro           → Registra usuario
GET    /api/auth/validar            → Valida token

GET    /api/clientes                → Lista clientes
POST   /api/clientes                → Crea cliente
GET    /api/clientes/{id}           → Obtiene cliente
PUT    /api/clientes/{id}           → Actualiza cliente
GET    /api/clientes/{id}/pedidos   → Pedidos de un cliente

GET    /api/inventario/productos             → Lista productos
POST   /api/inventario/productos             → Crea producto
GET    /api/inventario/productos/{id}        → Obtiene producto
PUT    /api/inventario/productos/{id}        → Actualiza producto
PUT    /api/inventario/productos/{id}/stock  → Ajusta stock (±cantidad)
GET    /api/inventario/productos/bajo-stock  → Productos con stock ≤ 10
DELETE /api/inventario/productos/{id}        → Elimina producto

GET    /api/pedidos                 → Lista pedidos
POST   /api/pedidos                 → Crea pedido (orquesta inventario + pago + envío)
GET    /api/pedidos/{id}            → Obtiene pedido
PUT    /api/pedidos/{id}/estado     → Actualiza estado del pedido
```

---

## Ejecución local con Docker Compose

### Requisitos

- Docker Engine ≥ 24
- Docker Compose ≥ 2.20
- 4 GB de RAM disponibles (todos los servicios juntos)

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/<ORG>/devops-ep3.git
cd devops-ep3

# 2. (Opcional) Crear .env con credenciales propias
cat > .env <<'EOF'
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-sa-east-1.pooler.supabase.com:6543/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres.gkcyyzzaizzuglmyetpe
SPRING_DATASOURCE_PASSWORD=<TU_PASSWORD>
JWT_SECRET=smartlogix-secret-key-2024-super-segura-para-produccion-abc123
EOF

# 3. Levantar toda la pila
docker-compose up --build

# 4. (Alternativa) Levantar solo servicios de infraestructura primero
docker-compose up -d redis rabbitmq
docker-compose up --build ms-eureka
docker-compose up --build ms-gateway ms-auth ms-clientes ms-inventario ms-pedidos
```

### Orden de arranque

Docker Compose respeta las dependencias declaradas con `depends_on + condition`:

```
redis ─────────────────────────────────────────────┐
                                                   ├──► ms-clientes
rabbitmq ──────────────────────────────────────┐   ├──► ms-inventario
         (healthcheck: rabbitmq-diagnostics)   │   └──► ms-pedidos
                                               │
ms-eureka ─────────────────────────────────────┤
          (healthcheck: /actuator/health)      │
                                               └──► ms-gateway, ms-auth,
                                                    ms-clientes, ms-inventario, ms-pedidos
```

### Puertos locales

| Servicio | Puerto | URL de acceso |
|---|---|---|
| ms-gateway | 8080 | `http://localhost:8080` |
| ms-auth | 8081 | `http://localhost:8081/swagger-ui/index.html` |
| ms-clientes | 8082 | `http://localhost:8082/swagger-ui/index.html` |
| ms-inventario | 8083 | `http://localhost:8083/swagger-ui/index.html` |
| ms-pedidos | 8084 | `http://localhost:8084/swagger-ui/index.html` |
| ms-eureka | 8761 | `http://localhost:8761` |
| Redis | 6379 | — |
| RabbitMQ | 5672 / 15672 | `http://localhost:15672` (guest/guest) |

### Verificar que todo está corriendo

```bash
# Estado de todos los contenedores
docker-compose ps

# Health de un servicio
curl http://localhost:8081/actuator/health

# Login de prueba
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@smartlogix.com","password":"admin123"}' | jq .

# Detener todo
docker-compose down
```

### Ejecutar tests

```bash
# Todos los módulos (desde la raíz)
./ms-auth/mvnw verify --batch-mode -f pom.xml

# Módulo específico
cd ms-auth
./mvnw test

# Con reporte de cobertura
./mvnw verify
open target/site/jacoco/index.html
```

---

## Estructura del proyecto

```
devops-ep3/
│
├── pom.xml                          # POM padre (multi-módulo Maven)
├── docker-compose.yml               # Orquestación local completa
├── .dockerignore
├── SECURITY.md                      # Política de seguridad
│
├── .github/
│   └── workflows/
│       └── deploy.yml               # Pipeline CI/CD (Build → Sonar → ECR → EKS)
│
├── k8s/                             # Manifiestos Kubernetes
│   ├── namespace.yml                # Namespace: smartlogix
│   ├── configmap.yml                # Variables no sensibles (URLs internas)
│   ├── secrets.yml                  # Template de Secrets (rellenado con envsubst)
│   ├── ms-eureka.yml                # Deployment + Service + HPA
│   ├── ms-gateway.yml               # Deployment + Service (LoadBalancer) + HPA
│   ├── ms-auth.yml                  # Deployment + Service (ClusterIP) + HPA
│   ├── ms-clientes.yml              # Deployment + Service (ClusterIP) + HPA
│   ├── ms-inventario.yml            # Deployment + Service (ClusterIP) + HPA
│   └── ms-pedidos.yml               # Deployment + Service (ClusterIP) + HPA
│
├── ms-eureka/                       # Service Discovery
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/java/.../MsEurekaApplication.java
│
├── ms-gateway/                      # API Gateway + filtro JWT
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/java/.../
│           ├── config/
│           │   ├── GatewayConfig.java
│           │   └── SecurityConfig.java
│           └── security/
│               ├── JwtFilter.java
│               └── JwtUtil.java
│
├── ms-auth/                         # Autenticación y JWT
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/.../
│       │   ├── config/SecurityConfig.java
│       │   ├── controller/AuthController.java
│       │   ├── dto/{LoginRequest,LoginResponse}.java
│       │   ├── model/Usuario.java
│       │   ├── repository/UsuarioRepository.java
│       │   ├── security/JwtUtil.java
│       │   └── service/AuthService.java
│       └── test/java/.../           # Tests unitarios + repositorio (H2)
│
├── ms-clientes/                     # Gestión de clientes
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/.../
│       │   ├── client/PedidosClient.java     # Feign → ms-pedidos
│       │   ├── config/RedisConfig.java
│       │   ├── controller/ClienteController.java
│       │   ├── dto/{ClienteRequest,ClienteResponse,PedidoResponse}.java
│       │   ├── model/Cliente.java
│       │   ├── repository/ClienteRepository.java
│       │   └── service/ClienteService.java   # @Cacheable/@CacheEvict Redis
│       └── test/java/.../
│
├── ms-inventario/                   # Gestión de inventario
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/.../
│       │   ├── config/RedisConfig.java
│       │   ├── controller/ProductoController.java
│       │   ├── dto/{ProductoRequest,ProductoResponse}.java
│       │   ├── model/Producto.java
│       │   ├── repository/ProductoRepository.java
│       │   └── service/ProductoService.java  # @Cacheable/@CacheEvict Redis
│       └── test/java/.../
│
└── ms-pedidos/                      # Orquestación de pedidos
    ├── Dockerfile
    ├── pom.xml
    └── src/
        ├── main/java/.../
        │   ├── client/
        │   │   ├── InventarioClient.java     # Feign → ms-inventario
        │   │   ├── PagosClient.java          # Feign → ms-pagos (externo)
        │   │   ├── EnviosClient.java         # Feign → ms-envios (externo)
        │   │   └── NotificacionesClient.java # Feign → ms-notificaciones (externo)
        │   ├── config/RabbitMQConfig.java
        │   ├── controller/PedidoController.java
        │   ├── dto/{PedidoRequest,PedidoResponse,DetallePedidoRequest,ProductoDTO}.java
        │   ├── messaging/{PedidoEventoDTO,PedidoProducer}.java
        │   ├── model/{Pedido,DetallePedido}.java
        │   ├── repository/PedidoRepository.java
        │   └── service/PedidoService.java    # Orquestación tolerante a fallos
        └── test/java/.../
```

---

## Decisiones de diseño relevantes

| Decisión | Justificación |
|---|---|
| **API stateless (JWT)** | Elimina la necesidad de sesiones en servidor; compatible con múltiples réplicas sin sesión compartida. `csrf.disable()` es seguro en este contexto. |
| **Caché Redis en ms-clientes e ms-inventario** | Reduce la carga en Supabase para lecturas frecuentes. `@CacheEvict(allEntries=true)` en escrituras garantiza consistencia. |
| **RabbitMQ para eventos de pedidos** | Desacopla la creación del pedido de los servicios auxiliares (inventario, pagos, envíos). Los fallos de servicios externos no bloquean el flujo principal. |
| **Feign con tolerancia a fallos** | Las llamadas a ms-pagos, ms-envios y ms-notificaciones están dentro de bloques `try/catch` para no bloquear la creación del pedido. |
| **HPA con umbral de CPU al 50 %** | Permite margen para manejar picos antes de saturar los pods. Con un mínimo de 2 réplicas, hay alta disponibilidad inmediata. |
| **Multi-stage Dockerfile** | Imagen de build separada de la imagen de runtime. La imagen final solo contiene el JRE Alpine y el JAR, sin Maven ni código fuente. |
| **JaCoCo umbral ≥ 60 %** | Excluyendo controllers y Application.class, el umbral se aplica sobre la lógica de negocio real (servicios, repositorios, DTOs). |
# test pipeline
# test pipeline2
# test pipeline2

---

## Estrategia de Ramificación (Branching Strategy)

### Modelo adoptado: GitHub Flow

Se adoptó **GitHub Flow** como modelo de ramificación por su simplicidad y compatibilidad con despliegue continuo. A diferencia de Git Flow (que requiere ramas `develop`, `release` y `hotfix` permanentes), GitHub Flow usa únicamente `main` como rama estable y ramas temporales para cada cambio.

| Modelo | Cuándo usarlo | Ventaja |
|---|---|---|
| **GitHub Flow** ✅ | Proyectos con despliegue continuo | Simple, rápido, ideal para CD |
| **Git Flow** | Proyectos con releases programadas | Control de versiones robusto |
| **Trunk-based** | Equipos grandes con feature flags | Integración continua extrema |

### Estructura de ramas

| Rama | Propósito | Permanencia |
|---|---|---|
| `main` | Rama principal protegida. Solo recibe merges via PR aprobado + pipeline verde | Permanente |
| `feature/<descripcion>` | Nueva funcionalidad. Ej: `feature/cors-gateway` | Temporal |
| `hotfix/<descripcion>` | Corrección urgente en producción. Ej: `hotfix/redis-cache-fix` | Temporal |
| `test/<descripcion>` | Pruebas de pipeline o configuración. Ej: `test/trigger-pipeline` | Temporal |

### Justificación frente a otros enfoques

GitHub Flow fue elegido sobre Git Flow porque:
- El proyecto tiene despliegue continuo a AWS EKS en cada merge a `main`
- No hay versiones programadas ni releases con fecha fija
- El equipo es pequeño (2 personas), reduciendo la complejidad de múltiples ramas permanentes
- Cada PR activa automáticamente el pipeline completo (build → test → sonar → deploy)

---

## Convenciones y Buenas Prácticas del Repositorio

### Nomenclatura de ramas

Ejemplos válidos:
- `feature/cors-configuration`
- `hotfix/prepared-statement-fix`
- `test/trigger-sonarqube`

### Convenciones de commits (Conventional Commits)

Formato: `<tipo>: <descripción en imperativo>`

| Tipo | Uso |
|---|---|
| `feat:` | Nueva funcionalidad |
| `fix:` | Corrección de bug |
| `ci:` | Cambios en pipeline o configuración CI/CD |
| `docs:` | Cambios en documentación |
| `refactor:` | Refactorización sin cambio funcional |
| `test:` | Agregar o corregir tests |
| `chore:` | Tareas de mantenimiento |

Ejemplos válidos:
- `feat: agregar configuracion CORS en ms-gateway`
- `fix: corregir prepareThreshold en URL Supabase`
- `ci: actualizar secrets AWS en workflow deploy`
- `docs: agregar seccion branching en README`

### Reglas de Pull Request

1. Todo cambio a `main` debe ir por Pull Request — nunca push directo
2. El PR requiere al menos **1 aprobación** de un reviewer con acceso de escritura
3. El pipeline CI/CD debe pasar completamente (build + test + SonarQube)
4. El título del PR debe seguir el formato: `feat/fix/ci: descripción breve`
5. El PR debe incluir descripción del cambio y capturas si aplica

### Branch Protection Rules configuradas en `main`

- ✅ Require pull request before merging
- ✅ Required approvals: 1
- ✅ Require status checks to pass (Build & Test Maven)
- ✅ Block force pushes
- ✅ Restrict deletions

---

## Flujo de Trabajo Colaborativo — Ejemplo Práctico

### Agregar una nueva funcionalidad (feature)

```bash
# 1. Partir siempre desde main actualizado
git checkout main
git pull origin main

# 2. Crear rama feature
git checkout -b feature/nueva-funcionalidad

# 3. Desarrollar y commitear con convención
git add .
git commit -m "feat: implementar nueva funcionalidad"

# 4. Subir la rama
git push origin feature/nueva-funcionalidad

# 5. Crear Pull Request en GitHub hacia main
# → El pipeline corre automáticamente
# → Reviewer aprueba
# → Se hace merge
```

### Corregir un bug urgente en producción (hotfix)

```bash
# 1. Partir desde main (que refleja producción)
git checkout main
git pull origin main

# 2. Crear rama hotfix
git checkout -b hotfix/descripcion-del-bug

# 3. Aplicar la corrección
git add .
git commit -m "fix: corregir descripcion del bug"

# 4. Subir y crear PR urgente
git push origin hotfix/descripcion-del-bug
# → PR a main con label "hotfix"
# → Pipeline debe pasar igual
# → Merge inmediato tras aprobación
```

### Trazabilidad del código

Cada cambio en el repositorio tiene trazabilidad completa:
- **Commit**: quién hizo el cambio, cuándo y por qué
- **PR**: qué revisó el equipo antes de aprobar
- **Pipeline**: evidencia automática de que build, tests y calidad pasaron
- **Deploy**: la imagen Docker subida al ECR tiene tag con el SHA del commit

ENDOFREADME
