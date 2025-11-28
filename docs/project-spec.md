# Logistics Route Optimizer — Project Specification

This document restates the machine-consumable specification provided for the Logistics Route Optimizer microservice. It captures business goals, architecture, and acceptance criteria so the codebase can evolve without losing context.

## Purpose
Deliver a Java 17+ microservice that optimizes multi-vehicle routes, estimates cost/ETA, persists operational data in Azure SQL, and integrates with a Python AI service. The service complements a .NET API gateway and demonstrates Azure-ready, enterprise-grade craftsmanship for Dubai recruiters.

## Functional Requirements
1. `POST /api/v1/optimize` — sync optimizer for ≤20 stops (under 2s); async job for larger payloads.
2. `POST /api/v1/estimate-cost` — fast heuristic cost/ETA.
3. `POST /api/v1/shipments` and `GET /api/v1/shipments/{id}` — manage shipment records.
4. `GET /api/v1/optimizations/{id}` — retrieve run details.
5. `GET /api/v1/health` and `GET /api/v1/metrics`.
6. `POST /api/v1/admin/recompute` — role-guarded bulk re-optimization trigger.

## Domain Concepts
- **Shipment** with multiple **Stops** (time windows, weights).
- **Vehicle** (capacity, cost, availability) starting from **Depots**.
- **OptimizationRun** storing serialized request/result and derived **Routes**.

## Architecture and Tech
- Spring Boot 3.x (Web, Data JPA, Security, Validation, Actuator).
- Azure SQL (SQL Server dialect) + Flyway migrations.
- Heuristic planner with future swap for OR-Tools or Python AI adapter.
- JWT security (Azure AD), role-based admin endpoints.
- Observability via Micrometer/Prometheus, JSON logging, optional OpenTelemetry.
- Dockerized deployment, GitHub Actions CI, optional Redis cache.

## Data Schema
T-SQL DDL for `users`, `vehicles`, `depots`, `shipments`, `stops`, `optimization_runs`, and `routes` tables with recommended indexes (see `src/main/resources/db/migration/V1__init_schema.sql`).

## API + OpenAPI
OpenAPI 3.0 spec (see `openapi.yml`) models request/response DTOs including `OptimizeRequest`, `OptimizeResponse`, `Stop`, `Vehicle`, and metrics payloads. Authentication uses `Authorization: Bearer <JWT>`.

## Non-Functional Goals
- Latency: heuristic <500 ms; small optimization <2 s; batch async.
- Security: TLS-only, Azure AD JWT validation, RBAC.
- Availability: 99% with container scaling, stateless workers.
- Observability: structured logs, Prometheus metrics, traces.
- Reliability: retries/backoff for cross-service calls, persisted audit payloads.

## Testing Strategy
- JUnit + Mockito unit tests.
- Integration tests with Testcontainers (MSSQL) for Flyway + JPA.
- Contract/load/security testing (JMeter/k6, JWT validation).

## CI/CD + Deployment
- GitHub Actions: build, test, docker image build, push to ACR, deploy to Azure App Service/Container Apps/AKS.
- Dockerfile (multi-stage) + docker-compose for local dev with SQL Server container.

## Acceptance Criteria (examples)
1. Small optimize requests return valid routes in <2s.
2. Large optimize requests return 202 + `optimization_run_id`, later retrievable.
3. Invalid JWT → 401.
4. Optimizer failures fall back to heuristic result with warning.

## Next Steps
- Integrate OR-Tools/Python AI service, add webhooks, Redis caching, Azure Monitor exporters, and elaborate admin tooling.

