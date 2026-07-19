# Agent Guide - SUS-Connect APS MVP

This file is the entry point for any coding or documentation agent working in
this repository. Read it before proposing or making changes.

## Current Product

The active project is a small MVP for territorial prioritization of preventive
active outreach in Primary Health Care (APS). It helps a coordinator answer:

> Which territory should receive an active-outreach action first, and why?

The current vertical slice lives in `aps-prioritization-service`.

Read these sources of truth in this order:

1. `.agents/context/product-scope.md`
2. `docs/produto/especificacao-requisitos.md`
3. `.agents/context/architecture-and-runtime.md`
4. the relevant skill in `.agents/skills/`

The repository contains only the APS MVP. Do not reintroduce the former
triage-focused architecture, patient records, scheduling, or gateway services.

## Non-Negotiable Scope Boundaries

- Prioritize territories or UBS units, never individual people.
- Use only aggregate or simulated data in the APS MVP.
- Do not add names, CPF, addresses, medical records, diagnoses, clinical risk
  scores, appointments, prescriptions, or patient-level tracking.
- The priority is an explainable operational signal, not a clinical diagnosis,
  prediction, or proof of causal impact.
- Keep the primary flow demonstrable through the API in less than eight
  minutes.

## Architecture Rules

- Java 21 and Spring Boot 3.4.5.
- Preserve Clean Architecture in every new APS feature:
  `core/domain`, `core/usecase`, `core/gateway`, `core/dto`, and `infra`.
- `core` is plain Java. Do not import Spring, JPA, Jakarta validation, Jackson,
  or other infrastructure libraries there.
- HTTP, JPA, Flyway, configuration, and framework-specific validation belong
  in `infra`.
- Add a Flyway migration for persistent schema changes. Do not rely on Hibernate
  schema generation.
- Map HTTP errors in `infra/web` with Spring `ProblemDetail`; keep the domain
  unaware of HTTP and framework types.

## Quality Gate

- Add focused unit tests for domain rules and use cases.
- Add or update an HTTP integration flow for user-visible behavior.
- Keep line coverage at or above 90% for the APS service bundle and each
  production class.
- Update API documentation and both Bruno and Insomnia assets whenever an API
  contract changes.
- Do not alter unrelated working-tree changes.
- After every completed and validated change, create an atomic Git commit with a
  clear conventional message. Stage only files belonging to that change; never
  include unrelated working-tree changes.

## Quick Commands

```bash
# Unit and integration tests for the APS service
mvn -q -pl aps-prioritization-service -am test

# Enforce the JaCoCo threshold directly
mvn -q -pl aps-prioritization-service jacoco:check@coverage-check

# Start the APS stack for a live demo
docker compose up -d --build aps-prioritization-service

# Check the live API
curl http://localhost:8205/actuator/health

# Run the isolated HTTP E2E flow against Docker and PostgreSQL
powershell -ExecutionPolicy Bypass -File .\scripts\run-e2e.ps1
```

The full `verify` lifecycle also runs Spotless. On machines where Maven cannot
download Spotless dependencies because of certificate configuration, record the
environment problem and still run the two commands above. Docker builds are the
preferred fallback for a clean Maven environment.

## Handoff Checklist

- Explain behavior and limits in Portuguese when writing project docs.
- Keep test/demo data fictitious and aggregate.
- State which verification commands passed and which environment limitations
  remain.
- Preserve the distinction between evidence, hypothesis, and product claim.
- Report the commit hash created for the completed change.

For task-specific guidance, see `.agents/README.md`.
