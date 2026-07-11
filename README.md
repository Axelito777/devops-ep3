SmartLogix — DevOps EP3

Sistema de 6 microservicios Spring Boot desplegados en AWS EKS, con observabilidad (Prometheus + Grafana), análisis de calidad de código (SonarCloud) y un pipeline CI/CD que se detiene automáticamente ante fallas.

Asignatura: ISY1101 — Introducción a Herramientas DevOps
Repositorio: github.com/Axelito777/devops-ep3
Integrantes: Axel Subiabre y Matías Molina


Arquitectura

6 microservicios desplegados en el namespace smartlogix, con Redis como caché:

MicroservicioPuertoTipo ServiceFunciónms-eureka8761LoadBalancerService Discoveryms-gateway8080LoadBalancerAPI Gateway / enrutamientoms-auth8081ClusterIPAutenticación JWTms-clientes8082ClusterIPCRUD clientes + Redis cachems-inventario8083ClusterIPCRUD productos + Redis cachems-pedidos8084ClusterIPGestión de pedidos

Infraestructura (AWS EKS)

ComponenteValorClusterdevops-ep3 — Kubernetes 1.34Regiónus-east-1 (N. Virginia)Node Group2-4 nodos t3.medium, Amazon Linux 2023VPC192.168.0.0/16, creada por eksctlSubredes públicasus-east-1a/1b — para LoadBalancerSubredes privadasus-east-1a/1b — para nodos EC2

Monitoreo con Prometheus y Grafana

Instalado vía Helm en el namespace monitoring con el stack kube-prometheus-stack:


Prometheus: recolecta métricas de todos los pods, nodos y servicios del cluster.
Grafana: dashboard personalizado "SmartLogix - DevOps EP3" con 3 paneles (CPU por microservicio, memoria por microservicio, pods disponibles).
kube-state-metrics y node-exporter: métricas de estado de objetos K8s y de los nodos EC2.
AlertManager: alertas configurables por umbral.


bashhelm install prometheus prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --set grafana.service.type=LoadBalancer \
  --set prometheus.service.type=LoadBalancer

Autoscaling (HPA)

Los 6 microservicios tienen HorizontalPodAutoscaler configurado con umbral de 50% CPU (min 2 / max 5 réplicas), escalando automáticamente ante aumentos de carga.

Pipeline CI/CD

El pipeline (.github/workflows/deploy.yml) integra las herramientas de forma secuencial, donde cada etapa depende de que la anterior pase:

PasoAcciónResultado1. TriggerPush a main (vía PR)GitHub Actions activado2. Build & Testmvn verify (tests + JaCoCo)Tests ejecutados, cobertura medida3. SonarQubemvn sonar:sonar + Quality GateAnálisis de calidad y seguridad4. Push ECRdocker build + push (x6 imágenes)Imágenes en ECR5. Deploy EKSkubectl apply -f k8s/ (x6 servicios)Pods actualizados en el cluster

Quality Gate obligatorio

bashmvn sonar:sonar -Dsonar.qualitygate.wait=true

Si el Quality Gate falla, Maven retorna exit code 1 y GitHub Actions detiene el pipeline completo, impidiendo que se construyan/suban imágenes o se despliegue en EKS.

Calidad de código (SonarCloud)

Análisis automático en cada push sobre los 6 microservicios: cobertura con JaCoCo, detección de vulnerabilidades, code smells y Quality Gate como umbral mínimo para permitir el deploy.

Branch Protection (rama main)


Require pull request before merging
Required approvals: 1
Require status checks to pass
Block force pushes
Restrict deletions


Estructura del repositorio

.
├── .github/workflows/   # Pipeline CI/CD completo (deploy.yml)
├── k8s/                 # Manifiestos Kubernetes (Deployment, Service, HPA por microservicio)
├── ms-auth/              # Autenticación JWT
├── ms-clientes/          # CRUD clientes + Redis cache
├── ms-eureka/            # Service Discovery
├── ms-gateway/           # API Gateway
├── ms-inventario/        # CRUD productos + Redis cache
├── ms-pedidos/           # Gestión de pedidos
├── .dockerignore
├── .gitignore
├── SECURITY.md           # Política de seguridad del proyecto
└── README.md

Cada microservicio (ms-*) incluye su propio Dockerfile multi-stage.

Stack tecnológico

Spring Boot · Docker · Kubernetes (AWS EKS) · Helm · Prometheus · Grafana · SonarCloud · GitHub Actions · Redis
