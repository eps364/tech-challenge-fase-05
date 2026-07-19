# Copilot Instructions - SUS-Connect APS MVP

Read [`AGENTS.md`](../AGENTS.md) first. The active product is a small MVP that
helps an APS coordinator prioritize a territory or UBS for preventive active
outreach and track the aggregate execution of that action.

## Scope

- The only application module is `aps-prioritization-service`.
- Prioritize territories or UBS units, never individual people.
- Use aggregate or fictitious demonstration data only.
- Do not add patient records, diagnoses, clinical risk scores, appointments,
  prescriptions, referrals, messaging, or real-time SUS integrations.
- A priority is an explainable operational signal, not a clinical diagnosis or
  prediction.

## Architecture

Java 21 and Spring Boot 3.4.5 are mandatory. Preserve Clean Architecture:

```text
core/domain
core/dto
core/gateway
core/usecase
infra
```

`core` is plain Java: no Spring, JPA, Jakarta validation, Jackson, or HTTP
types. Keep HTTP, JPA, Flyway, validation annotations, and `ProblemDetail`
mapping in `infra`. Persistent schema changes require a Flyway migration.

## Build, Test, And Demo

```bash
# Unit and HTTP integration tests
mvn -q -pl aps-prioritization-service -am test

# Enforce the 90% JaCoCo threshold
mvn -q -pl aps-prioritization-service jacoco:check@coverage-check

# Start the API and PostgreSQL
docker compose up -d --build aps-prioritization-service

# Run real HTTP E2E against an isolated Docker/PostgreSQL stack
powershell -ExecutionPolicy Bypass -File .\scripts\run-e2e.ps1
```

The active API is `http://localhost:8205/api/v1`; Swagger is available at
`http://localhost:8205/swagger-ui/index.html`.

## Contracts And Documentation

When an API contract changes, update all of the following in the same change:

1. `docs/tecnico/api/aps-prioritization-service.md`
2. `docs/tecnico/api/Aps-Prioritization/` (Bruno)
3. `docs/tecnico/api/aps-prioritization-insomnia.json`
4. Relevant unit and HTTP integration tests

Use the documentation index at [`docs/README.md`](../docs/README.md) for
product, data, technical, presentation, and hackathon context. Keep claims
about the solution factual: evidence is aggregate, outcomes remain hypotheses
to validate locally.

## Versioning

After each completed and validated change, create an atomic conventional Git
commit. Stage only files related to that change and do not include unrelated
working-tree changes.
