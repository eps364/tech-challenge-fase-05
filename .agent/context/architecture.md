# Software Architecture - Tech Challenge Phase 05 (SUS-Connect)

## Distributed Architecture: Healthcare Microservices

### Overview
**SUS-Connect** implements a microservices architecture for healthcare clinical triage management in the Brazilian healthcare system (SUS), following **Manchester Protocol v3.0**. Seven specialized services coordinate the end-to-end flow: authentication → service registry → API gateway → clinical services (triage, appointment, medical-record) with centralized configuration.

### Core Domain Microservices

#### 1. **Triage Service** (Port 8201)
- **Responsibility**: Clinical classification and patient prioritization
- **Protocol**: Manchester v3.0 (5 risk colors)
- **Logic**: Symptom collection → risk calculation → automatic appointment scheduling
- **Database**: `triage_db` (PostgreSQL, port 5432)
- **Events**: Publishes `triage.risk-classification` (Kafka)
- **Status**: ✅ Basic endpoints implemented

#### 2. **Appointment Service** (Port 8202)
- **Responsibility**: Slot management and consultation confirmation
- **Integrations**: Validates availability via OpenFeign with Triage
- **Database**: `appointment_db` (PostgreSQL, port 5433)
- **Events**: Publishes `appointment.confirmed` (Kafka)
- **Status**: ⚠️ Entity layer only, endpoints pending

#### 3. **Medical Record Service** (Port 8203)
- **Responsibility**: Medical history and clinical documentation
- **Logic**: Consultation persistence, results, prescriptions
- **Database**: `medical_record_db` (PostgreSQL, port 5434)
- **Events**: Consumes `triage.risk-classification` and `appointment.confirmed`
- **Status**: ⚠️ Entity layer only, endpoints pending

### Security & Identity Services

#### 4. **Auth Service** (Port: Dynamic via Eureka)
- **Responsibility**: User authentication and token generation
- **Features**: OAuth2 integration with Keycloak, JWT generation, refresh tokens
- **Database**: `auth_db` (PostgreSQL, port 5431)
- **Caching**: Redis (port 6379) for token blacklist
- **Status**: ✅ Basic auth endpoints implemented (register, login, logout, refresh)

### Infrastructure & Discovery Services

#### 5. **API Gateway** (Port 8761)
- **Responsibility**: Single entry point for all external requests
- **Features**: Dynamic routing, JWT validation, rate limiting
- **Configuration**: 
  - Routes to all microservices via Eureka discovery
  - Path predicates: `/triage/**`, `/appointment/**`, `/medical-record/**`, `/auth/**`
  - Prefix stripping for clean service paths
- **Security**: OAuth2 JWT validation with Keycloak

#### 6. **Service Registry (Eureka)** (Port 8762)
- **Responsibility**: Service discovery and automatic health checks
- **Features**: Dynamic service registration, health monitoring
- **All services automatically register**: Triage, Appointment, Medical Record, Auth, Config Server
- **Gateway consults for dynamic routing**

#### 7. **Config Server** (Port 8888)
- **Responsibility**: Centralized configuration management
- **Source**: `/config-repo` directory with YAML files per service
- **Configuration files**: `application.yml`, `auth-service.yml`, `triage-service.yml`, etc.
- **Status**: ✅ Infrastructure setup complete



### Additional Infrastructure

### Data Flow

```
Patient Arrives
      │
      ▼
Triage Service
  - Manchester Assessment
  - Risk Level: RED|ORANGE|YELLOW|GREEN|BLUE
  - Event: triage.risk-classification
      │
      ▼
Appointment Service
  - Check Availability
  - Schedule Consultation
  - Event: appointment.confirmed
      │
      ▼
Medical Record Service
  - Store Consultation Data
  - Document Diagnosis/Prescription
  - Event: medical-record.created
```

### Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.4.5
- **Cloud**: Spring Cloud 2024.0.0 (Eureka, OpenFeign, Circuit Breaker)
- **Database**: PostgreSQL 15
- **Message Broker**: Apache Kafka 7.5
- **Identity**: Keycloak 26.5.4
- **Orchestration**: Docker Compose 3.9
- **Build**: Maven 3.9+
- **Testing**: JUnit 5, Mockito, TestContainers

### Deployment Strategy

#### Local Development
- Docker Compose with 8 containers
- All services run in same network
- Database auto-migration via Flyway

#### Production (Future)
- Kubernetes deployment
- Horizontal auto-scaling
- Distributed tracing (Jaeger)
- Centralized logging (ELK Stack)
- Prometheus + Grafana monitoring

---

## Design Patterns

### Clean Architecture
- **Core Layer** (Pure Java):
  - Domain entities with business logic
  - Use cases for application flows
  - Gateway interfaces (ports)
  - DTOs for I/O

- **Infra Layer** (Spring/JPA):
  - JPA entities for persistence
  - Repository implementations
  - REST controllers
  - Configuration and security

### Event-Driven Architecture
- **Asynchronous Communication**: Kafka topics
- **Event Sourcing**: Immutable event logs
- **CQRS**: Separate read/write models (future)

### Microservices Patterns
- **API Gateway**: Central routing and security
- **Service Discovery**: Eureka registry
- **Circuit Breaker**: Resilience4j for fault tolerance
- **Load Balancing**: Client-side via Eureka

---

## Security Architecture

### Authentication Flow
1. User sends credentials to Keycloak
2. Keycloak issues JWT token (RS256)
3. Client includes JWT in Authorization header
4. API Gateway validates JWT signature
5. Gateway forwards request to service
6. Service validates token claims

### Authorization Levels
- **Realm Level**: Keycloak realm (`sus-connect`)
- **Client Level**: OAuth2 client roles
- **Service Level**: Spring Security `@PreAuthorize`
- **Resource Level**: Token claims validation

---

## Network Topology

```
┌─────────────────────────────────────────────────┐
│           Docker Network: susconnect-network     │
│                                                  │
│  ┌──────────────────────────────────────────┐  │
│  │      External Clients (HTTP/REST)        │  │
│  └──────────┬───────────────────────────────┘  │
│             │                                   │
│             ▼                                   │
│  ┌──────────────────────────────────────────┐  │
│  │  API Gateway (8761)                      │  │
│  │  - JWT Validation                        │  │
│  │  - Dynamic Routing                       │  │
│  └─┬─────────────────────┬────────────┬────┘  │
│    │                     │            │        │
│    ▼                     ▼            ▼        │
│  ┌────────┐         ┌──────────┐  ┌────────┐ │
│  │Triage  │         │Appointment│ │Medical │ │
│  │Service │         │Service    │ │Record  │ │
│  │(8201)  │         │(8202)     │ │Service │ │
│  └─┬──────┘         └─┬────────┘  │(8203)  │ │
│    │                 │            └──┬────┘ │
│    ▼                 ▼               ▼       │
│  ┌────────┐         ┌──────────┐  ┌────────┐ │
│  │triage  │         │appointment│ │medical │ │
│  │_db     │         │_db        │ │_record │ │
│  │        │         │           │ │_db     │ │
│  └────────┘         └──────────┘  └────────┘ │
│                                                │
│  ┌──────────────────────────────────────────┐  │
│  │  Kafka (9092)                            │  │
│  │  - Event Streaming                       │  │
│  │  - Topic Partitioning                    │  │
│  └──────────────────────────────────────────┘  │
│                                                │
│  ┌──────────────────────────────────────────┐  │
│  │  Keycloak (8080)                         │  │
│  │  - Identity Provider                     │  │
│  │  - JWT Issuer                            │  │
│  └──────────────────────────────────────────┘  │
│                                                │
│  ┌──────────────────────────────────────────┐  │
│  │  Eureka (8762)                           │  │
│  │  - Service Registry                      │  │
│  │  - Health Checks                         │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

---

## Database Architecture

### Tri-Database Strategy
- **Isolation**: Each service has independent database
- **Scalability**: Services scale independently
- **Consistency**: Eventual consistency via Kafka events
- **Backup**: Each database backed up independently

### Schema Evolution
- **Flyway**: Version control for schemas
- **Migrations**: Automatic on service startup
- **Zero-downtime**: Blue-green deployments (future)

---

## Monitoring & Observability

### Health Checks
- **Liveness**: Service is running
- **Readiness**: Service can handle requests
- **Startup**: Service initialization complete

### Metrics
- **JVM**: Memory, GC, thread count
- **Spring**: HTTP requests, data source pool
- **Business**: Risk classifications, appointments created

### Logging
- **Centralized**: All services log to standard output
- **Structured**: JSON format for parsing
- **Levels**: DEBUG (dev), INFO (prod)

---

## Scalability Considerations

### Horizontal Scaling
- **Stateless Services**: All services are stateless
- **Session Sharing**: Redis for distributed sessions (future)
- **Load Balancing**: Spring Cloud Load Balancer

### Caching Strategy
- **API Gateway**: Request/response caching
- **Services**: Application-level caching with Spring Cache
- **Database**: PostgreSQL query result cache

### Rate Limiting
- **Per IP**: API Gateway filters
- **Per Client**: OAuth2 client quotas (future)
- **Per Endpoint**: Bucket4j rate limiting (future)

---

## Disaster Recovery

### Backup Strategy
- **Database**: Daily automated PostgreSQL backups
- **Kafka**: Topic replication factor = 3 (future)
- **Configuration**: Git version control

### Failover Plan
- **Service Failure**: Eureka detects, routes to healthy instance
- **Database Failure**: Connection pooling with automatic retry
- **Network Partition**: Circuit breaker prevents cascading failures

---

## Future Enhancements

### Phase 2
- OpenFeign clients with Resilience4j
- Redis caching and sessions
- Advanced Kafka patterns (CQRS)
- Integration tests with TestContainers

### Phase 3
- Kubernetes deployment
- Distributed tracing (Jaeger)
- Centralized logging (ELK)
- Service mesh (Istio)

### Phase 4
- Event sourcing
- FHIR API compliance
- Encryption at rest (AES-256)
- LGPD audit logs
