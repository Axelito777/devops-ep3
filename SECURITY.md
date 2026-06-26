# Security Policy

## Supported Versions

| Version | Supported |
|---------|-----------|
| main    | Yes       |

## Security Strategy

### Static Analysis & Quality Gate (IE6)

All code merged to `main` is analyzed by **SonarCloud** on every push and pull request.
The pipeline fails automatically if the Quality Gate is not met. Key thresholds enforced:

- Code coverage ≥ 60 % (JaCoCo + Sonar)
- No new blocker or critical vulnerabilities
- No new security hotspots left unreviewed
- Duplicated lines < 3 %

### Branch Protection (IE5)

The `main` branch requires:

- At least **1 approving review** before merging
- All **status checks must pass** (build-and-test + SonarQube Quality Gate)
- No direct pushes — all changes go through pull requests
- Branch is **not deletable**

### Secret Management

- AWS credentials (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_SESSION_TOKEN`) are stored as **GitHub Actions secrets** and never hardcoded.
- Database credentials (`SUPABASE_*`) and `JWT_SECRET` are injected at runtime via Kubernetes Secrets.
- SonarCloud token (`SONAR_TOKEN`) is stored as a GitHub Actions secret.

### Container Security

- All images run as a **non-root user** (`appuser`) defined in each Dockerfile.
- Base images use `eclipse-temurin:17-jre-alpine` (minimal attack surface).
- Images are scanned on push via ECR's built-in scan configuration.

### Network Security

- Services communicate internally via Kubernetes ClusterIP.
- Only `ms-gateway` and `ms-eureka` are exposed via LoadBalancer.
- JWT validation is enforced at the gateway layer for all non-public routes.
- Actuator endpoints are restricted to `health` and `info` only.

## Reporting a Vulnerability

To report a security vulnerability, please open a **private** GitHub Security Advisory
or contact the maintainers directly. Do not open a public issue for security vulnerabilities.

Response SLA: acknowledgement within 48 hours, patch within 7 business days for critical issues.
