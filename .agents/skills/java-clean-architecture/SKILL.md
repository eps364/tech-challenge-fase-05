---
name: java-clean-architecture
description: Implement Java 21 APS MVP features without breaking Clean Architecture.
---

# Java Clean Architecture Skill

Use this skill for Java, Spring, persistence, REST, and migration changes in
`aps-prioritization-service`.

## Placement Rules

| Change | Correct Layer |
| --- | --- |
| Invariant, priority rule, state transition | `core/domain` |
| Application orchestration | `core/usecase` |
| External storage contract | `core/gateway` |
| Use-case I/O | `core/dto` |
| JPA entity/repository/adapter | `infra/entity`, `infra/repository`, `infra/gateway` |
| HTTP request/controller | `infra/web` |
| Spring beans and configuration | `infra/config` |

## Implementation Steps

1. Write or adjust the domain rule first.
2. Express required persistence through a gateway interface.
3. Add or modify a use case that returns a core DTO.
4. Implement the JPA adapter and migration only after the core shape is clear.
5. Add request validation at the HTTP boundary.
6. Wire the use case in `UseCaseConfig`.
7. Map known errors in `infra/web` with Spring `ProblemDetail`; do not leak
   HTTP or framework types into the core.

## Tests

- Test domain state transitions and calculations without Spring.
- Test use cases with fakes/mocks for gateway behavior.
- Test request-to-response behavior with Spring MockMvc and H2/Flyway.
- Cover unhappy paths: missing territory/action, invalid counts, duplicate data,
  and invalid status transitions when applicable.

## Avoid

- Framework annotations in `core`.
- Repository calls directly from controllers.
- Business rule branching in JPA adapters or controllers.
- Schema changes without a Flyway migration.
- Broad refactors unrelated to the requested feature.
