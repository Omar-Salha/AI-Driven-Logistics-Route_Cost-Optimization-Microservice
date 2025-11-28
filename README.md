# Logistics Route Optimizer

Enterprise-grade Spring Boot microservice providing route optimization, cost estimation, and ETA predictions for logistics fleets. Designed to showcase Azure-ready architecture that integrates with .NET gateways and Python AI services.

## Features
- REST API for optimization, shipments, and admin operations (OpenAPI + Swagger UI).
- Heuristic optimizer with async job offload and Azure SQL persistence (Flyway migrations included).
- JWT-secured endpoints (Azure AD friendly) with role-guarded admin routes.
- Observability: health endpoint, Prometheus metrics, structured JSON logging.
- Docker + docker-compose for local dev using SQL Server container, CI via GitHub Actions.

## Quick start
```bash
docker compose up --build
```
App exposes `http://localhost:8080` with Swagger UI at `/swagger-ui.html`.

## Environment variables
- `AZURE_SQL_URL`, `AZURE_SQL_USER`, `AZURE_SQL_PASSWORD`
- `JWT_JWK_ENDPOINT`, `JWT_AUDIENCE`, `JWT_ISSUER`
- Optional: `EXTERNAL_ROUTING_URL`, `EXTERNAL_ROUTING_API_KEY`, `REDIS_URL`, `SENTRY_DSN`

## API Highlights
- `POST /api/v1/optimize` — synchronous (≤40 stops) or async planner.
- `POST /api/v1/estimate-cost` — heuristic cost/ETA.
- `POST /api/v1/shipments`, `GET /api/v1/shipments/{id}`
- `GET /api/v1/optimizations/{id}`
- `POST /api/v1/admin/recompute`
- `GET /api/v1/metrics`, `GET /api/v1/health`

OpenAPI spec lives in `openapi.yml`.

## Testing
Run `mvn verify` for unit tests. Extend with Testcontainers for MSSQL-backed integration testing (dependencies already in `pom.xml`).

## Deployment
- Build container via provided Dockerfile; push to Azure Container Registry.
- Deploy to Azure Container Apps/App Service/AKS. Configure secrets via Azure Key Vault.
- Attach Prometheus/Grafana or Azure Monitor for metrics, Sentry/Application Insights for tracing.

## Next steps
- Swap heuristic planner with OR-Tools or Python AI endpoint.
- Wire Redis cache, Azure Service Bus notifications, and real telemetry exporters.
- Expand admin tooling (bulk recompute, audit log export) and add performance/regression tests.

