# Technical Decision History - Tech Challenge Phase 05

## 1. Microservices Architecture Decision

### Context
- Brazilian healthcare system (SUS) needs scalable platform
- Patient flow: Classification → Appointment → Medical Record
- Different teams may own different services
- Different scaling requirements per service

### Decision
**Choose microservices over monolith**

### Rationale
- **Independent Scaling**: Triage Service can scale for high classification volume without scaling Medical Record Service
- **Team Autonomy**: Each service owned by dedicated team with independent deployment
- **Technology Choice**: Java services can use same stack but different databases
- **Failure Isolation**: Appointment Service failure doesn't affect Triage classification

### Alternatives Considered
- **Monolithic**: Single Spring Boot application with all modules
  - Pros: Simpler deployment, easier debugging
  - Cons: Difficult to scale, shared database becomes bottleneck
  
- **Serverless (Lambda/Functions)**: Each service as serverless function
  - Pros: Pay-per-use, auto-scaling
  - Cons: Cold starts problematic for healthcare, vendor lock-in

### Decision Status
✅ **ACCEPTED** - Implemented across 3 microservices (Triage, Appointment, Medical Record)

---

## 2. Clean Architecture Selection

### Context
- Domain logic needs testability without Spring framework
- Separation of concerns important for healthcare domain
- Need clear boundaries between business logic and infrastructure

### Decision
**Implement clean architecture with 2 layers: Core (domain) and Infrastructure**

### Layer Definition

**Core Layer (Pure Java)**
- Domain entities with business logic
- Gateway interfaces (ports)
- Use cases (application services)
- DTOs for I/O contracts
- **Zero Spring dependencies**

**Infrastructure Layer (Spring + Persistence)**
- JPA entities mapped to database
- Spring Data repositories
- Adapter implementations
- REST controllers
- Configuration and security

### Rationale
- **Testability**: Core layer testable without Spring (faster unit tests)
- **Maintainability**: Clear separation makes code changes easier
- **Framework Independence**: Core logic can be reused in different frameworks
- **Healthcare Requirements**: Clear audit trail of domain decisions

### Alternatives Considered
- **Hexagonal Architecture (Full)**: Separate application, domain, infrastructure, presentation
  - Pros: More granular separation
  - Cons: Over-engineering for current scope
  
- **Layered Monolith**: Traditional 3-4 layer architecture
  - Pros: Familiar to most developers
  - Cons: Harder to extract microservices later

### Decision Status
✅ **ACCEPTED** - Applied to all 3 services consistently

---

## 3. Database per Service Pattern

### Context
- Each microservice needs independent data store
- Triage data cannot be tightly coupled to Appointment data
- Eventual consistency acceptable between services

### Decision
**Each service has isolated PostgreSQL database**

**Database Instances**
- `triage_db` (Triage Service)
- `appointment_db` (Appointment Service)
- `medical_record_db` (Medical Record Service)

### Rationale
- **Independence**: Services can deploy schema changes without coordination
- **Scalability**: Each database can be tuned for its workload
- **Backup**: Separate backup strategies per service
- **LGPD Compliance**: Data for different services segregated

### Data Consistency Strategy
- **Synchronous**: OpenFeign REST calls between services when needed
- **Asynchronous**: Kafka topics for event notification
- **Eventual Consistency**: Services converge to consistent state

### Alternatives Considered
- **Shared Database**: Single PostgreSQL for all services
  - Pros: Easier joins, ACID transactions across services
  - Cons: Tight coupling, scaling bottleneck, schema changes require coordination
  
- **NoSQL per Service**: Different database types per service
  - Pros: Optimized per use case
  - Cons: Operational complexity, LGPD compliance harder

### Decision Status
✅ **ACCEPTED** - 3 independent PostgreSQL 15 instances deployed

---

## 4. Spring Cloud Framework Selection

### Context
- Need service discovery for dynamic routing
- API Gateway needed to route external requests
- Services need to call each other

### Decision
**Use Spring Cloud 2024.0.0 with Eureka and OpenFeign**

### Technology Stack
- **Eureka**: Service registry for service discovery
- **OpenFeign**: Declarative REST client (future)
- **API Gateway**: Spring Cloud Gateway for routing
- **Circuit Breaker**: Resilience4j pattern (future)

### Rationale
- **Native Spring Integration**: Seamless with Spring Boot
- **Cloud-Agnostic**: Not locked to AWS, Google Cloud, or Azure
- **Mature Ecosystem**: Battle-tested in production systems
- **Developer Friendly**: Declarative APIs, minimal configuration

### Service Discovery Flow
```
Service Startup
    ↓
Register with Eureka
    ↓
Periodic Heartbeats
    ↓
API Gateway queries Eureka for routes
    ↓
Dynamic Load Balancing
```

### Alternatives Considered
- **Consul**: HashiCorp service mesh
  - Pros: Multi-language support, built-in service mesh
  - Cons: Additional operational overhead
  
- **Kubernetes Service Discovery**: Native K8s DNS
  - Pros: Scalable, cloud-native
  - Cons: Requires Kubernetes deployment

### Decision Status
✅ **ACCEPTED** - Eureka 8762, API Gateway 8761 in use

---

## 5. Keycloak for Identity Management

### Context
- Brazilian healthcare needs centralized authentication
- OAuth2 standard required for future integrations
- LGPD compliance needs audit trails for access

### Decision
**Use Keycloak 26.5.4 as OAuth2 + OpenID Connect provider**

### Authentication Flow
```
Client Request
    ↓
API Gateway JWT Validation
    ↓
Token from Keycloak with RS256 signature
    ↓
Service-level authorization via Spring Security
    ↓
Audit logs for access
```

### Realm Structure
- **Realm**: `sus-connect`
- **Clients**: api-gateway, triage-service, appointment-service, medical-record-service
- **Roles**: PATIENT, PROFESSIONAL, ADMIN, SYSTEM
- **Token TTL**: 5 minutes access token, 30 minutes refresh

### Rationale
- **Standard Compliance**: OAuth2 and OpenID Connect industry standards
- **Centralized**: Single identity source for all services
- **Audit Trail**: Full access logging for LGPD compliance
- **Future-Proof**: Integrations with external identity providers possible

### Alternatives Considered
- **Auth0**: Third-party identity provider
  - Pros: Managed service, no ops overhead
  - Cons: Monthly costs, vendor lock-in, LGPD data residency concerns
  
- **Custom JWT**: In-house token generation
  - Pros: Full control, no external dependencies
  - Cons: Security risk, operational burden, no standardization

### Decision Status
✅ **ACCEPTED** - Keycloak 26.5.4 with sus-connect realm deployed

---

## 6. Apache Kafka for Event Streaming

### Context
- Services need to communicate asynchronously
- Triage events need to trigger appointment creation
- Appointment confirmation needs to notify medical record service
- Audit trail requirements for LGPD

### Decision
**Implement event-driven architecture with Apache Kafka 7.5**

### Event Topics
1. **triage.risk-classification**
   - Producer: Triage Service
   - Consumers: Appointment Service, Medical Record Service
   - Payload: triageId, patientId, riskLevel, timestamp

2. **appointment.confirmed**
   - Producer: Appointment Service
   - Consumers: Medical Record Service
   - Payload: appointmentId, patientId, dateTime, status

3. **medical-record.created**
   - Producer: Medical Record Service
   - Payload: recordId, appointmentId, patientId, diagnosis

### Rationale
- **Decoupling**: Services don't need to know about each other's implementation
- **Scalability**: Kafka handles millions of messages
- **Audit Trail**: Immutable event log for LGPD compliance
- **Fault Tolerance**: Messages persisted, can be replayed if service fails

### Alternatives Considered
- **RabbitMQ**: Traditional message broker
  - Pros: Simpler model, good for point-to-point
  - Cons: Not ideal for event streaming, poor scalability

- **AWS SQS/SNS**: Cloud-native queuing
  - Pros: Managed service, auto-scaling
  - Cons: Vendor lock-in, LGPD data residency issues

- **Synchronous REST Calls Only**: No async
  - Pros: Simpler debugging
  - Cons: Tight coupling, cascading failures

### Decision Status
✅ **ACCEPTED** - Kafka 7.5 with Zookeeper 7.5 deployed

---

## 7. PostgreSQL as Primary Database

### Context
- Healthcare data needs ACID transaction guarantees
- LGPD requires full audit trail of data changes
- Relationships between entities (Patient → Triage → Appointment → Medical Record)
- Cost-effective for SUS budget constraints

### Decision
**Use PostgreSQL 15 as relational database**

### Schema Design
- **UUID Primary Keys**: Avoid centralized ID generation
- **Timestamps**: created_at (immutable), updated_at (triggers for LGPD audit)
- **Naming**: snake_case columns for database portability
- **Migrations**: Flyway for version control

### Rationale
- **ACID Compliance**: Data integrity guaranteed
- **Open Source**: No licensing costs
- **Mature**: 25+ years in production
- **LGPD Audit Trail**: Can implement triggers for all changes

### Alternatives Considered
- **MySQL/MariaDB**: Similar relational databases
  - Pros: Slightly lighter weight
  - Cons: PostgreSQL has better JSON support, JSONB for future

- **NoSQL (MongoDB/DynamoDB)**: Document databases
  - Pros: Flexible schema
  - Cons: No ACID across documents, harder LGPD compliance

- **Data Lake (Spark/Parquet)**: Analytics-first
  - Pros: Massive scale
  - Cons: Not suitable for operational database

### Decision Status
✅ **ACCEPTED** - PostgreSQL 15 with 3 isolated instances deployed

---

## 8. Flyway for Database Migrations

### Context
- Schema changes need to be version controlled
- Deployments need deterministic database state
- Multiple environments (dev, test, prod) need synchronized schema

### Decision
**Use Flyway for database migration management**

### Migration Versioning
- Format: `V{SERVICE_VERSION}__{description}.sql`
- Example: `V1.1__create_triage_table.sql`
- Automatic execution on application startup

### Rationale
- **Version Control**: SQL migrations in Git
- **Reproducibility**: Same schema across all environments
- **Validation Mode**: Production uses validation-only (DDL changes prevented)
- **Rollback Support**: Can rollback to previous versions

### Alternatives Considered
- **Liquibase**: More powerful, XML/YAML-based
  - Pros: YAML/JSON support, more features
  - Cons: Overkill for current needs, complexity

- **Manual SQL Scripts**: DBA-executed changes
  - Pros: Full control
  - Cons: Error-prone, hard to track, difficult to automate

### Decision Status
✅ **ACCEPTED** - Flyway configured for all 3 services

---

## 9. Manchester Protocol Implementation

### Context
- SUS requires standardized triage system
- Manchester Protocol v3.0 widely used in emergency care
- Risk classification into 5 color levels

### Decision
**Implement Manchester Protocol v3.0 as core business logic**

### Risk Levels
| Level | Color | Service Time |
|-------|-------|--------------|
| 1 | RED | ≤ 0 min (immediate) |
| 2 | ORANGE | ≤ 10 min |
| 3 | YELLOW | ≤ 60 min |
| 4 | GREEN | ≤ 120 min |
| 5 | BLUE | ≤ 240 min |

### Rationale
- **Standardized**: Used by major hospitals worldwide
- **Evidence-Based**: Reduces mortality and improves outcomes
- **Patient-Centric**: Fair prioritization system
- **Regulatory**: Meets SUS requirements

### Implementation
- Core domain logic in `Triage` entity
- No Spring dependencies in classification
- Use case orchestrates triage creation

### Alternatives Considered
- **ACUITY SCALE**: American triage system
  - Pros: Similar accuracy
  - Cons: Different from Brazilian standard

- **Custom Algorithm**: SUS-specific triage
  - Pros: Perfect fit
  - Cons: No validation, unproven

### Decision Status
✅ **ACCEPTED** - v3.0 implemented in domain layer

---

## 10. Docker Compose for Local Development

### Context
- Multiple services, databases, and infrastructure components
- Need consistent development environment across team
- CI/CD pipeline needs same dependencies

### Decision
**Use Docker Compose 3.9 for orchestration**

### Services
- 4 PostgreSQL databases (keycloak, auth, triage, appointment, medical-record)
- Apache Kafka + Zookeeper
- Keycloak
- Service Registry (Eureka)
- API Gateway
- 3 Microservices (Triage, Appointment, Medical Record)
- Redis 7 (Auth Service — token blacklist)

### Rationale
- **Consistency**: Dev environment mirrors production
- **Ease**: Single `docker-compose up` command
- **Reproducibility**: All team members have identical setup
- **CI/CD Integration**: Same compose file for automated tests

### File Structure
- `docker-compose.yml`: Base configuration
- `docker-compose.dev.yml`: Development overrides (debug logging)
- `.dockerignore`: Optimize build context

### Alternatives Considered
- **Kubernetes**: Production-grade orchestration
  - Pros: Scalable, cloud-ready
  - Cons: Overkill for local dev, steep learning curve

- **Manual Container Management**: Docker run commands
  - Pros: Direct control
  - Cons: Error-prone, hard to manage dependencies

### Decision Status
✅ **ACCEPTED** - compose files deployed and working

---

## 11. Google Java Format via Spotless

### Context
- Team needs consistent code style
- Manual enforcement error-prone
- CI/CD needs to validate formatting

### Decision
**Enforce Google Java Format via Spotless Maven plugin**

### Configuration
- 100 character line length
- 2-space indentation
- Automatic formatting in pre-commit hooks (future)
- CI/CD validation via `spotless:check`

### Rationale
- **Consistency**: Eliminates style debates
- **Readability**: Industry-standard format
- **Automation**: Machine-enforced, not manual review
- **CI/CD Integration**: Prevents merging unformatted code

### Alternatives Considered
- **Checkstyle**: More rules and configuration
  - Pros: Highly customizable
  - Cons: Complex configuration

- **EditorConfig**: .editorconfig files
  - Pros: IDE-agnostic
  - Cons: Manual enforcement

### Decision Status
✅ **ACCEPTED** - Spotless configured in parent pom.xml

---

## 12. JUnit 5 + Mockito + TestContainers

### Context
- Healthcare code requires high test coverage
- Tests need to verify actual database behavior
- Mock objects needed for unit tests

### Decision
**Use JUnit 5 for test framework, Mockito for mocks, TestContainers for integration tests**

### Test Organization
- **Unit Tests**: Domain entities (no dependencies)
- **Integration Tests**: Repository adapters (with TestContainers PostgreSQL)
- **Controller Tests**: REST endpoints (MockMvc)

### Rationale
- **JUnit 5**: Modern API, parameterized tests, better lifecycle
- **Mockito**: Simple mock creation and verification
- **TestContainers**: Real PostgreSQL in tests, no test database maintenance

### Alternatives Considered
- **Spock**: Groovy-based testing
  - Pros: Powerful DSL
  - Cons: Team not familiar with Groovy

- **Rest Assured**: HTTP client testing
  - Pros: Fluent API for REST
  - Cons: Additional dependency

### Decision Status
✅ **ACCEPTED** - Framework configured and tests written

---

## Key Architectural Decisions Summary

| # | Decision | Status | Trade-off |
|---|----------|--------|-----------|
| 1 | Microservices | ✅ Accepted | Operational complexity vs independence |
| 2 | Clean Architecture | ✅ Accepted | More layers vs better testability |
| 3 | Database per Service | ✅ Accepted | Join difficulty vs service autonomy |
| 4 | Spring Cloud | ✅ Accepted | Vendor specificity vs simplicity |
| 5 | Keycloak | ✅ Accepted | Operational overhead vs security |
| 6 | Kafka | ✅ Accepted | Complexity vs decoupling |
| 7 | PostgreSQL | ✅ Accepted | Rigid schema vs ACID guarantees |
| 8 | Flyway | ✅ Accepted | Learning curve vs reproducibility |
| 9 | Manchester | ✅ Accepted | Customization vs standardization |
| 10 | Docker Compose | ✅ Accepted | Container knowledge vs consistency |
| 11 | Google Format | ✅ Accepted | Initial strictness vs long-term consistency |
| 12 | JUnit 5 | ✅ Accepted | Complexity vs coverage |

---

## Phase 05 Bootstrap - Current Implementation Status

### Project Structure
✅ **Complete**
- 7 microservices with Maven multi-module structure
- Parent POM with dependency management
- Dockerfiles for each service (multi-stage builds)
- Docker Compose orchestration with 13 containers

### Infrastructure Setup
✅ **Complete**
- PostgreSQL 15: 4 instances (auth, triage, appointment, medical-record)
- Keycloak 26.5.4: OAuth2/OpenID Connect provider
- Eureka: Service registry on port 8762
- API Gateway: Spring Cloud Gateway on port 8761
- Config Server: Centralized configuration server on port 8888
- Kafka 7.5 + Zookeeper 7.5: Event streaming
- Redis 7: Token blacklist caching

### Authentication & Security
✅ **Operational**
- Auth Service: Register, login, logout, refresh endpoints
- Keycloak realm: `sus-connect` with default users and clients
- JWT validation: RS256 signature — access token 5 min, refresh token 30 min
- Spring Security: OAuth2 resource server configuration
- Basic rate limiting and CORS in API Gateway

### Domain Services
✅ **Partial Implementation**
- **Triage Service** (Port: dynamic via Eureka): POST /api/v1/triage funcional com CreateTriageUseCase; GET /api/v1/triage/{id} placeholder
- **Appointment Service** (Port: dynamic via Eureka): Entity layer only
- **Medical Record Service** (Port: dynamic via Eureka): Entity layer only
- Clean Architecture: Core/Infra separation in all services
- Flyway migrations: Infrastructure ready, initial schema created

### Event-Driven Architecture
⚠️ **Infrastructure Ready, Business Logic Pending**
- Kafka topics created: `triage.risk-classification`, `appointment.confirmed`, `medical-record.created`
- Producers/Consumers: Framework configured, implementation pending
- Zookeeper: Cluster coordination ready

### Developer Experience
✅ **Complete**
- Spotless: Code formatting enforcement (Google Java Format)
- OpenAPI/Swagger: Documented endpoints per service
- Bruno API Collection: Auth and Triage endpoints documented
- Health checks: All services expose `/actuator/health`
- Centralized logging configuration

### Testing Infrastructure
✅ **Ready**
- JUnit 5: Test framework configured
- Mockito: Mock objects setup
- TestContainers: PostgreSQL 15 test containers available
- Code coverage: Jacoco configured for `mvn test jacoco:report`
- Target: 80% minimum coverage enforcement

### Next Steps (Phase 2)
🔲 **To Be Implemented**
- Appointment Service endpoints and business logic
- Medical Record Service endpoints and business logic
- Kafka event publishers/consumers
- Service-to-service communication (OpenFeign)
- Circuit breaker patterns (Resilience4j)
- Advanced error handling and validation
- Email notifications
- Dashboard and reporting features

---

## Future Architectural Decisions

### Phase 2 (Next Iteration)
- [ ] Decision: Resilience4j circuit breaker for service calls
- [ ] Decision: Redis caching for performance
- [ ] Decision: OpenFeign for service-to-service communication
- [ ] Decision: Advanced Kafka patterns (CQRS)

### Phase 3 (Long-term)
- [ ] Decision: Kubernetes migration
- [ ] Decision: Distributed tracing (Jaeger)
- [ ] Decision: Centralized logging (ELK)
- [ ] Decision: Service mesh (Istio)

### Phase 4 (Strategic)
- [ ] Decision: Event sourcing
- [ ] Decision: Machine learning for risk prediction
- [ ] Decision: FHIR API compliance
- [ ] Decision: Blockchain for audit trail (if LGPD allows)

---

**Document Version**: 1.0.0  
**Last Updated**: May 31, 2024  
**Author**: Tech Challenge Team  
**Review Frequency**: Quarterly
