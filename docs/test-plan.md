# Test Plan

## Unit Tests
- **Service heuristics:** `HeuristicPlannerTest` validates greedy assignment and metrics.
- **Cost estimation:** `CostEstimationServiceTest` ensures deterministic distance/cost outputs.
- Expand coverage with Mockito for `OptimizationService`, `ShipmentService`, and controllers.

## Integration Tests (future)
1. **Persistence:** Spin up MSSQL via Testcontainers, run Flyway migrations, verify CRUD on `shipments` and `optimization_runs`.
2. **Security:** Mock JWT via `NimbusJwtDecoder` bean override to assert role-based access (`/api/v1/admin/**` vs `/api/v1/health`).
3. **Async flow:** Submit large request (>40 stops), poll `/optimizations/{id}` until status transitions to `COMPLETED`.

## Contract Tests
- Use RestAssured to validate payloads vs `openapi.yml`.
- Mock downstream Python AI service to ensure graceful fallbacks.

## Performance
- JMeter/k6 scenario: 50 RPS on `/optimize` with 20-stop payload; verify <2s latency, no 5xx.
- Async stress: 5 concurrent 200-stop jobs, ensure queue drains and DB persistence remains healthy.

## Security
- JWT negative tests (wrong issuer/audience, expired).
- Verify admin endpoint rejects users lacking `ROLE_admin`/`SCOPE_admin`.

## Acceptance Criteria Mapping
1. Small optimize request returns routes <2s (unit/perf test).
2. Large request returns 202 with `optimization_run_id` and eventual `COMPLETED`.
3. Invalid JWT -> 401 (security tests).
4. Optimizer failure triggers heuristic fallback (simulate exception in solver).

