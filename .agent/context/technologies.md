# Technology Stack - SUS-Connect

## Java & JVM Ecosystem

### Core Language
- **Java 17** (LTS - Long Term Support until September 2026)
- Features used: Records, Text Blocks, Sealed Classes
- Compilation target: Java 17 bytecode

### Build Management
- **Maven 3.9+** with multi-module structure
- Parent POM for dependency management
- Plugins:
  - **Spotless**: Google Java Format enforcement
  - **Jacoco**: Code coverage reporting
  - **Surefire**: Unit test execution
  - **Failsafe**: Integration test execution
  - **Assembly**: JAR packaging

### Dependency Management
```xml
<properties>
  <spring-boot.version>3.3.0</spring-boot.version>
  <spring-cloud.version>2024.0.0</spring-cloud.version>
  <maven.compiler.source>17</maven.compiler.source>
  <maven.compiler.target>17</maven.compiler.target>
</properties>
```

---

## Spring Ecosystem

### Spring Boot (3.3.0)
Core framework for microservices:
- Auto-configuration for common patterns
- Embedded Tomcat servlet container (port customizable)
- Actuator for health checks and metrics
- Spring Data JPA for database access
- Spring Kafka for message broker integration

### Spring Cloud (2024.0.0)
Distributed systems support:
- **Eureka**: Service registry and discovery
- **OpenFeign**: Declarative REST client
- **Hystrix/Resilience4j**: Circuit breaker pattern
- **Config Server**: Centralized configuration (future)
- **Spring Cloud Gateway**: API gateway

### Spring Security
OAuth2 and JWT integration:
- Keycloak as identity provider
- JWT token validation with RS256 algorithm
- Role-based access control (RBAC)
- Cross-Origin Resource Sharing (CORS) configuration

---

## Data Persistence

### PostgreSQL 15
- **Relational Database**: ACID transactions
- **Connection Pooling**: HikariCP (20 connections max)
- **Versions**: 3 instances (triage_db, appointment_db, medical_record_db)

### Flyway
Schema version control:
- Versioned migrations (V1.1__, V2.1__, V3.1__)
- Automatic execution on application startup
- Validation mode (no DDL changes at runtime)

### Spring Data JPA
ORM framework:
- **Hibernate** as JPA provider
- Entity mapping to database tables
- JPQL and native query support
- Repository pattern implementation

### HikariCP
Connection pool:
- 20 maximum connections
- 5 minimum idle connections
- 30-second connection timeout
- Connection validation on checkout

---

## Message Broker

### Apache Kafka 7.5.0
Event streaming platform:
- **Zookeeper 7.5.0**: Cluster coordination
- **Topics**:
  - `triage.risk-classification` (Triage Service → All)
  - `appointment.confirmed` (Appointment Service → All)
  - `medical-record.created` (Medical Record Service → All)

### Kafka Configuration
- **Bootstrap Servers**: `kafka:9092`
- **Consumer Groups**: Service-specific
- **Serializer**: Spring Kafka JSON serializer
- **Partitions**: Auto-created on topic publish
- **Replication Factor**: 1 (dev), 3 (prod future)

---

## Identity & Access Management

### Keycloak 26.5.4
OAuth2 and OpenID Connect provider:
- **Realm**: `sus-connect`
- **Authentication Type**: OAuth2 Authorization Code Flow
- **Token Type**: JWT (RS256 algorithm)
- **Token TTL**: 5 minutes access, 30 minutes refresh
- **Database**: PostgreSQL keycloak_db

### Keycloak Clients
1. **api-gateway** (Public client)
   - Initiates OAuth2 flow
   - No client secret required
   - Redirect URIs: `http://localhost:3000/*` (future SPA)

2. **triage-service** (Service Account)
   - Service-to-service authentication
   - Client credentials flow
   - Service account role assignments

3. **appointment-service** (Service Account)
   - Cross-service API calls
   - Client credentials flow
   - Service account role assignments

4. **medical-record-service** (Service Account)
   - Cross-service API calls
   - Client credentials flow
   - Service account role assignments

### User Roles
- **PATIENT**: Access own medical records, book appointments
- **PROFESSIONAL**: View triage, manage appointments, document records
- **ADMIN**: Full system access, user management
- **SYSTEM**: Service-to-service communication

---

## API & Documentation

### Spring Web (REST)
- **Servlet API**: HTTP request/response handling
- **Content Negotiation**: JSON (primary), XML (future)
- **HTTP Methods**: GET, POST, PUT, DELETE
- **Status Codes**: 200 OK, 201 Created, 204 No Content, 400 Bad Request, 404 Not Found, 500 Internal Error

### SpringDoc OpenAPI 3.0
API documentation:
- **Swagger UI**: Interactive API explorer
- **OpenAPI Specification**: Machine-readable API definition
- **Endpoint**: `GET /swagger-ui.html`
- **Spec URL**: `GET /v3/api-docs`

### SpringDoc Configuration
```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: method
```

---

## Testing Framework

### JUnit 5
Modern testing framework:
- **Jupiter API**: `@Test`, `@DisplayName`, `@ParameterizedTest`
- **Assertions**: `AssertThat` (AssertJ library)
- **Lifecycle**: `@BeforeEach`, `@AfterEach`

### Mockito
Mocking library:
- `@Mock`: Create mock objects
- `@InjectMocks`: Inject mocks into test subjects
- `when().thenReturn()`: Stub behavior
- `verify()`: Assert mock interactions

### TestContainers
Integration testing with containers:
- PostgreSQL container for database tests
- Automatic container lifecycle management
- Image: `postgres:15-alpine`

### Test Organization
```
src/test/java/
├── unit/
│   └── domain/        # Domain entity tests
├── integration/
│   ├── repository/    # Repository adapter tests
│   └── controller/    # REST controller tests
└── acceptance/        # End-to-end tests
```

---

## Containerization & Orchestration

### Docker
Container images:
- **Base**: `openjdk:17-slim` for applications
- **PostgreSQL**: `postgres:15-alpine`
- **Keycloak**: `quay.io/keycloak/keycloak:26.5.4`
- **Kafka**: `confluentinc/cp-kafka:7.5.0`
- **Zookeeper**: `confluentinc/cp-zookeeper:7.5.0`
- **Redis**: `redis:7-alpine`

### Docker Compose 3.9
Orchestration:
- `docker-compose.yml`: Production-like configuration
- `docker-compose.dev.yml`: Development with debug logging
- Network: `susconnect-network` (custom bridge)
- Volumes: Persistent data for databases

### Multi-stage Build
```dockerfile
# Stage 1: Build
FROM maven:3.9-openjdk-17 AS builder
COPY . /build
RUN cd /build && mvn clean package

# Stage 2: Runtime
FROM openjdk:17-slim
COPY --from=builder /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Monitoring & Observability

### Spring Boot Actuator
Health and metrics endpoints:
- `GET /actuator/health`: Service health status
- `GET /actuator/health/details`: Detailed health (DB, Kafka, etc.)
- `GET /actuator/info`: Application information
- `GET /actuator/metrics`: Metrics list
- `GET /actuator/metrics/{name}`: Specific metric
- `GET /actuator/gateway/routes`: API Gateway routes

### Micrometer
Metrics collection:
- **JVM Metrics**: Memory, GC, threads
- **HTTP Metrics**: Request count, latency, status codes
- **Database Metrics**: Connection pool usage, query times
- **Kafka Metrics**: Message count, lag, throughput

### Spring Cloud Gateway Monitoring
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "health,info,metrics,gateway"
```

---

## Logging

### SLF4J & Logback
Logging framework:
- **Facade**: SLF4J (Simple Logging Facade)
- **Implementation**: Logback (default in Spring Boot)
- **Format**: Pattern with timestamp, level, logger, message
- **Appenders**: Console (stdout), File (future)

### Log Configuration
```yaml
logging:
  level:
    root: INFO
    br.com.fiap.susconnect: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n"
```

### Log Levels
- **ERROR**: Application errors, exceptions
- **WARN**: Recoverable issues, deprecated usage
- **INFO**: Important lifecycle events
- **DEBUG**: Detailed diagnostic information
- **TRACE**: Very detailed flow information (rarely used)

---

## Package Manager

### Maven Central Repository
Dependency resolution:
- **Plugins**: Spotless, Jacoco, Surefire, Maven Compiler
- **Dependencies**: Spring Boot Starter dependencies
- **Management**: Version control via parent POM
- **Offline Mode**: Requires pre-cached dependencies

---

## Code Quality

### Google Java Format
Code formatting:
- **Style Guide**: Google Java Style Guide
- **Line Length**: 100 characters maximum
- **Indentation**: 2 spaces
- **Tool**: Spotless Maven plugin

### SonarQube Integration (Future)
Code quality analysis:
- Automatic code review
- Security vulnerability scanning
- Technical debt reporting
- Code coverage metrics

---

## Development Tools

### IDE Support
- **VS Code**: With Extension Pack for Java
- **IntelliJ IDEA**: Native Spring Boot support
- **Eclipse**: Spring Tools Suite (STS)

### Version Control
- **Git**: Distributed version control
- **GitHub**: Remote repository and collaboration
- **Conventional Commits**: Standardized commit messages

### Environment Variables
```bash
# Keycloak
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin

# PostgreSQL
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password

# Application
SERVER_PORT=8201
SPRING_APPLICATION_NAME=triage-service
```

---

## Performance Characteristics

### Latency Targets
- **Triage Creation**: < 500ms (p95)
- **Appointment Retrieval**: < 200ms (p95)
- **Medical Record Query**: < 300ms (p95)
- **API Gateway Routing**: < 100ms (p95)

### Throughput Targets
- **Triage Service**: 1,000 requests/second
- **Appointment Service**: 500 requests/second
- **Medical Record Service**: 500 requests/second
- **Kafka**: 10,000 messages/second per topic

### Resource Usage
- **JVM Memory**: 512MB - 1GB per service
- **PostgreSQL**: 2GB RAM, 10GB storage
- **Kafka**: 1GB RAM, 50GB storage (topics)
- **Keycloak**: 1GB RAM, 1GB storage

---

## Security Technologies

### Encryption
- **In Transit**: TLS 1.3 (HTTPS)
- **At Rest**: PostgreSQL with AES-256 (future)
- **JWT Signing**: RS256 (RSA Signature with SHA-256)

### Authentication Protocols
- **OAuth2**: Authorization Code Flow
- **OpenID Connect**: Identity verification layer
- **JWT**: Token-based authentication

### API Security
- **CORS**: Configurable allowed origins
- **Rate Limiting**: Per-IP and per-client limits (future)
- **API Key Management**: Keycloak client credentials

---

## Compliance & Standards

### Healthcare Standards
- **LGPD** (Brazil): Data protection and privacy
- **HIPAA** (US): Health information privacy
- **FHIR**: Fast Healthcare Interoperability Resources (future)

### Code Standards
- **Google Java Style Guide**: Enforced via Spotless
- **Twelve-Factor App**: Application configuration
- **REST API Design**: RESTful principles

---

**Version**: 1.0.0  
**Last Updated**: May 31, 2024  
**Java LTS Support Until**: September 17, 2026
