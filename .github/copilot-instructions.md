# Copilot Instructions — SUS-Connect Healthcare Platform

## Build, Test & Lint

```bash
# Build all modules
./mvnw clean package -DskipTests

# Build and run a single service
./mvnw clean package -DskipTests -pl triage-service

# Run all tests
./mvnw test

# Run tests for a single module
./mvnw test -pl triage-service

# Run a single test class
./mvnw test -pl triage-service -Dtest=TriageServiceTest

# Format code (Google Java Format)
./mvnw spotless:apply

# Check formatting without applying
./mvnw spotless:check

# Run integration tests (requires Docker)
./mvnw verify -P test

# Generate coverage report
./mvnw test jacoco:report
```

Start the full stack (infrastructure + all services):
```bash
docker compose up --build -d
docker compose down        # stop
docker compose down -v     # stop and remove volumes
```

Config files per service live in `/config-repo/*.yml` and are served by Config Server at startup.

> **Note on ports**: `api-gateway` runs on **8761** (fixed). All domain services (`auth-service`, `triage-service`, `appointment-service`, `medicalrecord-service`) use `SERVER_PORT=0` (dynamic). Find their assigned ports via Eureka (`http://localhost:8762/eureka/apps`) or `docker compose ps`.

## Architecture

This is a **Maven multi-module project** with 7 Spring Boot microservices:

| Module | Port | Status |
|--------|------|--------|
| `config-server` | 8888 | ✅ Infrastructure |
| `registry-service` (Eureka) | 8762 | ✅ Infrastructure |
| `api-gateway` | 8761 | ✅ Routes all external traffic |
| `auth-service` | dynamic | ✅ Keycloak + Redis token blacklist |
| `triage-service` | dynamic | ✅ Core domain — Manchester Protocol (POST funcional, GET placeholder) |
| `appointment-service` | dynamic | ⚠️ Entity layer only |
| `medicalrecord-service` | dynamic | ⚠️ Entity layer only |

`common-lib` is a shared library (no port) providing RFC 9457 error handling (`ProblemDetail`, `GlobalExceptionHandler`, `DomainException`, `ProblemDetailBuilder`). All services depend on it.

**Request flow**: Client → Keycloak (auth) → API Gateway (JWT validation + routing via Eureka) → Service → PostgreSQL / Kafka

**Async flow**: `triage-service` publicará `triage.risk-classification` → `appointment-service` + `medicalrecord-service` consumirão. ⚠️ Kafka event production ainda não implementado — infraestrutura está pronta.

Each service has its own isolated PostgreSQL database. Schema migrations are managed with **Flyway** only — never use `ddl-auto: create` or `update`.

## Clean Architecture — Mandatory Package Structure

Every service (except `common-lib`) follows this layout under `src/main/java/br/com/fiap/<service>/`:

```
core/
  domain/
    entity/        ← Rich domain entities (pure Java, no framework imports)
    valueobject/   ← Immutable value objects
  usecase/         ← One class per use case, main method named execute()
  gateway/         ← Output port interfaces (e.g. TriageGateway)
  dto/             ← Java records: Input/Output for use-case I/O; Request for web input
infra/
  entity/          ← JPA entities (@Entity), suffixed with Jpa (e.g. TriageJpa)
  repository/      ← Spring Data JPA interfaces
  gateway/         ← Adapters implementing core.gateway (e.g. TriageRepositoryAdapter)
  web/controller/  ← Spring @RestController classes (e.g. TriageController, AuthController)
  messaging/       ← Kafka producers/consumers
  config/          ← Spring @Configuration, Security, Bean wiring
```

**Core is pure Java** — never import `org.springframework.*`, `jakarta.persistence.*`, or `com.fasterxml.jackson.*` inside the `core` package. `jakarta.validation.*` annotations are allowed on core DTOs used as request bodies. Core domain exceptions must also be free of Spring imports — if a core exception needs a specific HTTP status, create a `@RestControllerAdvice` in `infra/exception/` with `@ExceptionHandler` for that exception (takes precedence over the generic `DomainException` handler in `GlobalExceptionHandler`). Beans wiring use cases with gateways must be declared in an `infra/config/UseCaseConfig.java` `@Configuration` class.

**Domain entities use static factory methods** — e.g. `Triage.create(patientId)` sets default state (`RiskLevel.BLUE`); never expose public constructors. Enums may carry domain logic (e.g. `RiskLevel.isMoreUrgentThan(other)`).

## Key Conventions

**Naming**
- Use cases: `<Verb><Entity>UseCase` (e.g. `CreateTriageUseCase`, `ScheduleAppointmentUseCase`)
- Core DTOs: Java `record` — use `Input`/`Output` suffix for use-case I/O; `<Entity>Request` for web request bodies (placed in `core.dto`, not infra)
- JPA entities: `<Entity>Jpa` suffix
- Adapters: `<Entity>RepositoryAdapter` or `<Resource>GatewayImpl`

**Docker images**: Builder uses `maven:3.9-eclipse-temurin-21`; runtime uses `eclipse-temurin:21-jre-noble`. Each service Dockerfile does a selective build (`mvn -pl <service> -am`) to avoid rebuilding sibling modules.

**Domain-to-JPA mapping**: Done manually in adapters using factory methods (e.g. `toJpa()`, `toDomain()`). Do not use MapStruct in the `core` layer.

**Error handling**: All exceptions extend `DomainException` from `common-lib`. They are mapped to RFC 9457 `ProblemDetail` responses (content-type `application/problem+json`) by `GlobalExceptionHandler`. Custom problem type URIs follow the pattern `/problems/<service>/<error-slug>`.

**Validation messages** must be in English, referencing the problematic field, e.g. `"The patient ID cannot be null"`.

**Flyway migrations**: All schema changes via `V{major}.{minor}__{description}.sql` only.

**Controllers** must use `@Operation`, `@Tag`, and `@ApiResponse` from SpringDoc OpenAPI. Inject use cases (not repositories) via constructor.

**Transactions**: `@Transactional` belongs on adapter methods in `infra.gateway`, not on use cases.

**Logging**: Use Lombok `@Slf4j`; log entry/exit of critical use case operations.

**Tests**:
- Unit tests (core layer): JUnit 5 + Mockito, no Spring context, pattern `*Test.java`
- Integration tests (infra layer): `@SpringBootTest` + TestContainers PostgreSQL, pattern `*IT.java`; activate with `-P test`
- Minimum unit test coverage: 80%

**Commits**: Conventional Commits — `feat`, `fix`, `test`, `docs`, `refactor` with service scope, e.g. `feat(triage): implement manchester scoring`.

**Manchester wait times** (v3.0, conforme `RiskLevel` enum): RED ≤ 0 min, ORANGE ≤ 10 min, YELLOW ≤ 60 min, GREEN ≤ 120 min, BLUE ≤ 240 min.

**Manchester Risk Levels** (canonical enum values): `RED`, `ORANGE`, `YELLOW`, `GREEN`, `BLUE`.

**Keycloak realm**: `sus-connect`. Local credentials: `admin / admin` at `http://localhost:8080`.
