# Architecture And Runtime - APS MVP

## Active Modules

| Module | Role |
| --- | --- |
| `aps-prioritization-service` | Current APS MVP vertical slice. |

The repository intentionally contains only this active module and its support
artifacts for data analysis, API demonstration, and E2E verification.

## APS Service Structure

```text
aps-prioritization-service/
  src/main/java/br/com/fiap/susconnect/aps/
    core/
      domain/     # Plain Java entities, rules, value-like enums, exceptions
      dto/        # Input/output records for use cases
      gateway/    # Output-port interfaces
      usecase/    # Application orchestration
    infra/
      config/     # Spring configuration and demo data
      entity/     # JPA mappings
      gateway/    # Output-port adapters
      repository/ # Spring Data repositories
      web/        # REST controllers and request DTOs
```

Dependencies point inward. A `core` class must never import framework-specific
types. New dependency injection wiring belongs in `infra/config/UseCaseConfig`.

## Persistence And Data

- PostgreSQL 15 is used at runtime; H2 is used by tests.
- Flyway migration files live in
  `aps-prioritization-service/src/main/resources/db/migration/`.
- Schema validation is enabled. Add a migration with any entity/schema change.
- `DemoDataConfig` supplies four fictitious aggregate territories and initial
  actions when `APS_DEMO_DATA_ENABLED=true`.
- Preserve stable demo IDs when possible because Bruno and Insomnia collections
  use them.

## API And Runtime

| Item | Value |
| --- | --- |
| API base | `http://localhost:8205/api/v1` |
| Swagger | `http://localhost:8205/swagger-ui/index.html` |
| Health | `http://localhost:8205/actuator/health` |
| PostgreSQL host port | `5434` |
| Main API documentation | `docs/tecnico/api/aps-prioritization-service.md` |
| Bruno collection | `docs/tecnico/api/Aps-Prioritization/` |
| Insomnia export | `docs/tecnico/api/aps-prioritization-insomnia.json` |

## Verification Commands

```bash
# Fast automated suite, including Spring MVC integration tests
mvn -q -pl aps-prioritization-service -am test

# Explicit 90% JaCoCo gate for module and classes
mvn -q -pl aps-prioritization-service jacoco:check@coverage-check

# Docker-backed live environment
docker compose up -d --build aps-prioritization-service
docker compose ps aps-prioritization-service aps-prioritization-postgres
```

Before sharing an API change, run the Bruno collection or its equivalent live
HTTP sequence: health, dashboard, priority listing, territory details, action
creation, and action-progress update.

## Review Hotspots

- Priority rule stays explicit and deterministic.
- Indicator replacement must preserve the unique territory/focus constraint.
- A completed action requires a non-negative performed count.
- Error responses use Spring `ProblemDetail` in `infra/web`; the core stays
  framework-free.
- Any new endpoint needs request validation, tests, API documentation, Bruno,
  and Insomnia coverage.
