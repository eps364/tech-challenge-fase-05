# SUS-Connect Intelligent Triage - Tech Challenge Phase 05

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-green)](https://spring.io/projects/spring-boot)
[![Docker Compose](https://img.shields.io/badge/Docker%20Compose-3.9-blue)](https://docs.docker.com/compose/)

## 📋 Overview

**SUS-Connect** is a decentralized healthcare platform that implements **intelligent clinical triage** based on **Manchester Protocol v3.0**. The system coordinates the end-to-end flow: risk classification → automatic appointment → medical documentation, with full support for **Keycloak authentication**, **scalable microservices**, and **clean architecture**.

### Key Features

✅ **Intelligent Triage**: Automatic patient classification into 5 risk levels (RED, ORANGE, YELLOW, GREEN, BLUE)  
✅ **Automatic Appointment**: Dynamic consultation reservation respecting service deadlines  
✅ **Unified Medical Record**: Centralized and audited medical history  
✅ **OAuth2/JWT Authentication**: Keycloak with centralized identity management  
✅ **Microservices**: 3 independent services with isolated databases  
✅ **Clean Architecture**: Pure core (Java) + Infra (Spring/JPA) separated  
✅ **API Gateway**: Dynamic routing via Eureka  
✅ **Event-Driven**: Kafka for asynchronous inter-service communication  

---

## 🚀 Quick Start

### Prerequisites

- **Docker & Docker Compose** 3.9+
- **Java 21+** (for local build)
- **Maven 3.9+** (included via mvnw)

### 1️⃣ Clone and Build

```bash
git clone https://github.com/eps364/tech-challenge-fase-05.git
cd tech-challenge-fase-05

# Build all services
mvn clean package
```

### 2️⃣ Start Infrastructure

```bash
# Start all services
docker-compose up -d

# For development (with debug logging):
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

### 3️⃣ Verify Health

```bash
# Check Eureka (Service Registry)
curl http://localhost:8762

# Check API Gateway
curl http://localhost:8761/actuator/health

# Check Triage Service
curl http://localhost:8201/actuator/health

# Check Appointment Service
curl http://localhost:8202/actuator/health

# Check Medical Record Service
curl http://localhost:8203/actuator/health
```

### 4️⃣ Access Web Consoles

| Service | URL | Credentials |
|---------|-----|-------------|
| **Eureka** | http://localhost:8762 | - |
| **Keycloak Admin** | http://localhost:8080 | admin / admin |
| **Swagger API** | http://localhost:8761/swagger-ui.html | - |

### 5️⃣ Stop Services

```bash
docker-compose down
```

---

## 📚 Documentation

Complete technical documentation:

- [`.agent/context/architecture.md`](.agent/context/architecture.md) - Distributed architecture
- [`.agent/context/business_rules.md`](.agent/context/business_rules.md) - Manchester Protocol
- [`.agent/context/best_practices.md`](.agent/context/best_practices.md) - Code patterns
- [`.agent/context/technologies.md`](.agent/context/technologies.md) - Technology stack
- [`.agent/context/tech_history.md`](.agent/context/tech_history.md) - Architectural decisions
- [`docs/event-storm.md`](docs/event-storm.md) - Event map
- [`docs/protocol_manchester.md`](docs/protocol_manchester.md) - Manchester Protocol

---

## 🏗️ Architecture

```
                    Patient/Professional
                           │
                           ▼
                    Keycloak (8080)
                 OAuth2/JWT/OpenID Connect
                           │
                           ▼
                    API Gateway (8761)
           ┌───────────────┬──────────────┐
           │               │              │
           ▼               ▼              ▼
    Auth Service    Triage Service   Appointment      Medical Record
      (8080)          (8201)         Service (8202)    Service (8203)
           │               │              │              │
           ▼               ▼              ▼              ▼
      auth_db         triage_db       appointment_db   medical_record_db
   (PostgreSQL)    (PostgreSQL)      (PostgreSQL)      (PostgreSQL)
           │               │              │              │
           └───┬───────────┼──────────────┼──────────────┘
               ▼           │              │
          Redis Cache      │              │
            (Token         │              │
          Blacklist)       ▼              ▼
                      Kafka Topics
              - triage.risk-classification
              - appointment.confirmed
              - medical-record.created
```

---

## 📦 Modules

### 1. **Service Registry** (Port 8762)
- Eureka server for service discovery
- Health checks and load balancing
- Automatic service registration/deregistration

### 2. **API Gateway** (Port 8761)
- Entry point for all external requests
- JWT validation via Keycloak tokens
- Dynamic routing to microservices
- Rate limiting and security filters

### 3. **Auth Service** (Port 8080)
- User registration and authentication
- Keycloak OAuth2/OpenID Connect integration
- JWT token generation and validation
- Token refresh and logout functionality
- Redis-backed token blacklist

**Database**: `auth_db` (PostgreSQL)
**Endpoints**:
```bash
POST   /auth/register          # User registration
POST   /auth/login             # User authentication
POST   /auth/refresh           # Token refresh
POST   /auth/logout            # User logout (authenticated)
GET    /auth/test/public       # Public test endpoint
GET    /auth/test/private      # Private test endpoint (ROLE_user)
```

### 4. **Triage Service** (Port 8201)
- Manchester Protocol v3.0 implementation
- Patient risk classification
- Automatic appointment scheduling
- Produces `triage.risk-classification` events

**Database**: `triage_db` (PostgreSQL)
**Endpoints**:
```bash
POST   /api/v1/triage              # Create new triage
GET    /api/v1/triage/{id}         # Get triage by ID
```

### 5. **Appointment Service** (Port 8202)
- Appointment slot management
- Consultation reservation
- Status tracking
- Produces `appointment.confirmed` events

**Database**: `appointment_db` (PostgreSQL)
**Endpoints**:
```bash
POST   /api/v1/appointment         # Create appointment
GET    /api/v1/appointment/{id}    # Get appointment
```

### 6. **Medical Record Service** (Port 8203)
- Patient medical history
- Consultation documentation
- Diagnosis and prescriptions
- Consumes Kafka events

**Database**: `medical_record_db` (PostgreSQL)
**Endpoints**:
```bash
POST   /api/v1/medical-record      # Create medical record
GET    /api/v1/medical-record/{id} # Get medical record
```

---

## 🗄️ Database Schema

### Triage Table
```sql
CREATE TABLE triage (
  id UUID PRIMARY KEY,
  patient_id UUID NOT NULL,
  risk_level VARCHAR(10) NOT NULL,  -- RED, ORANGE, YELLOW, GREEN, BLUE
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);
```

### Appointment Table
```sql
CREATE TABLE appointment (
  id UUID PRIMARY KEY,
  triage_id UUID NOT NULL,
  patient_id UUID NOT NULL,
  professional_id UUID,
  date_time TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL,      -- CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);
```

### Medical Record Table
```sql
CREATE TABLE medical_record (
  id UUID PRIMARY KEY,
  appointment_id UUID NOT NULL,
  patient_id UUID NOT NULL,
  diagnosis TEXT,
  prescription TEXT,
  consultation_date TIMESTAMP,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);
```

---

## 🔐 Security

### Authentication
- **Provider**: Keycloak (OAuth2 + OpenID Connect)
- **Realm**: `sus-connect`
- **Token Type**: JWT (RS256)
- **Default Credentials**: admin / admin

### Authorization
- **Roles**: PATIENT, PROFESSIONAL, ADMIN
- **Token Validation**: API Gateway validates all requests
- **Scope-based Access**: Services validate token scopes

### Keycloak Setup

**Keycloak** is the centralized identity provider for all services. It handles OAuth2, OpenID Connect, and JWT authentication.

#### Access Keycloak Admin Console
- **URL**: http://localhost:8080/admin
- **Username**: admin
- **Password**: admin
- **Realm**: sus-connect

#### Configured Clients
1. **api-gateway** (Public) - For external API requests
2. **auth-service** (Confidential) - Auth Service client
3. **triage-service** (Confidential) - Triage Service client
4. **appointment-service** (Confidential) - Appointment Service client
5. **medical-record-service** (Confidential) - Medical Record Service client
6. **frontend-client** (Public) - For frontend applications

#### Default Test Users
- **admin** / admin (Admin role)
- **patient01** / patient123 (Patient role)
- **doctor01** / doctor123 (Professional role)

#### Token Lifespan
- Access Token: 15 minutes (900 seconds)
- Refresh Token: 90 days

#### JWT Token Validation
- Issuer URI: `http://keycloak:8080/realms/sus-connect`
- JWK Set: `http://keycloak:8080/realms/sus-connect/protocol/openid-connect/certs`
- Token Type: Bearer

---

## 📡 Event-Driven Architecture

### Kafka Topics

#### 1. `triage.risk-classification`
**Producer**: Triage Service
**Consumers**: Appointment Service, Medical Record Service
**Payload**:
```json
{
  "triagedId": "uuid",
  "patientId": "uuid",
  "riskLevel": "RED|ORANGE|YELLOW|GREEN|BLUE",
  "timestamp": "2024-05-31T10:00:00"
}
```

#### 2. `appointment.confirmed`
**Producer**: Appointment Service
**Consumers**: Medical Record Service
**Payload**:
```json
{
  "appointmentId": "uuid",
  "patientId": "uuid",
  "professionalId": "uuid",
  "dateTime": "2024-05-31T14:30:00",
  "status": "CONFIRMED"
}
```

#### 3. `medical-record.created`
**Producer**: Medical Record Service
**Payload**:
```json
{
  "recordId": "uuid",
  "appointmentId": "uuid",
  "patientId": "uuid",
  "diagnosis": "...",
  "timestamp": "2024-05-31T15:00:00"
}
```

---

## 🧪 Testing

### Unit Tests
```bash
# Run unit tests (core domain)
mvn test
```

### Integration Tests
```bash
# Run integration tests with TestContainers
mvn verify
```

### Test Coverage
```bash
# Generate coverage report
mvn jacoco:report
open target/site/jacoco/index.html
```

---

## 🛠️ Development

### Local Development Setup

```bash
# 1. Start infrastructure only
docker-compose up -d keycloak zookeeper kafka triage-postgres appointment-postgres medical-record-postgres

# 2. Run services directly from IDE
# In VS Code: F5 or debug task

# 3. Or use Spring Boot dev tools
mvn spring-boot:run -pl triagem-service -Dspring-boot.devtools.restart.enabled=true
```

### Code Formatting
```bash
# Apply Google Java Format
mvn spotless:apply

# Check formatting
mvn spotless:check
```

### Environment Variables
```bash
# Triage Service
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/triage_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8762/eureka/

# Appointment Service
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/appointment_db
# ... same pattern as above

# Medical Record Service
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5434/medical_record_db
# ... same pattern as above
```

---

## 🔄 API Examples

### Authentication Workflow

**1. Register New User**
```bash
curl -X POST http://localhost:8761/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@sus-connect.local",
    "firstName": "New",
    "lastName": "User",
    "password": "password123"
  }'
```

**2. Login (Get Tokens)**
```bash
curl -X POST http://localhost:8761/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "patient01",
    "password": "patient123"
  }'
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cC...",
  "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cC...",
  "expiresIn": 900,
  "tokenType": "Bearer"
}
```

**3. Use Token for Protected Endpoints**
```bash
curl -X GET http://localhost:8761/triage \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

**4. Refresh Token**
```bash
curl -X POST http://localhost:8761/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<REFRESH_TOKEN>"
  }'
```

**5. Logout**
```bash
curl -X POST http://localhost:8761/auth/logout \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### Triage Operations

### Create Triage
```bash
curl -X POST http://localhost:8761/triage \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{}'
```

### Get Triage
```bash
curl -X GET http://localhost:8761/triage/{id} \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Appointment Operations
```bash
curl -X POST http://localhost:8761/appointment \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "triageId": "uuid",
    "patientId": "uuid",
    "dateTime": "2024-06-15T14:30:00"
  }'
```

### Get Medical Record
```bash
curl -X GET http://localhost:8761/medical-record/{id} \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 📊 Monitoring

### Health Endpoints
```bash
# Gateway health
curl http://localhost:8761/actuator/health

# Detailed health
curl http://localhost:8761/actuator/health/details

# Metrics
curl http://localhost:8761/actuator/metrics

# Gateway routes
curl http://localhost:8761/actuator/gateway/routes
```

### Logging
- **Default Level**: INFO
- **Debug Services**: Set `LOGGING_LEVEL_BR_COM_FIAP_SUSCONNECT=DEBUG`
- **SQL Logging**: Set `SPRING_JPA_SHOW_SQL=true`

---

## 🚨 Troubleshooting

### Services won't start
```bash
# Check logs
docker-compose logs -f triage-service

# Verify all services are up
docker-compose ps

# Check port conflicts
lsof -i :8761  # API Gateway
lsof -i :8762  # Eureka
lsof -i :8080  # Keycloak
```

### Database connection errors
```bash
# Verify PostgreSQL is running
docker-compose logs -f triage-postgres

# Check credentials in application.yml
cat triagem-service/src/main/resources/application.yml

# Reset databases
docker-compose down -v
docker-compose up -d
```

### Keycloak not accessible
```bash
# Restart Keycloak
docker-compose restart keycloak

# Check logs
docker-compose logs -f keycloak

# Access admin console
http://localhost:8080/admin (admin/admin)
```

---

## 📋 Project Structure

```
tech-challenge-fase-05/
├── .agent/
│   ├── context/
│   │   ├── architecture.md
│   │   ├── business_rules.md
│   │   ├── best_practices.md
│   │   ├── technologies.md
│   │   └── tech_history.md
│   └── instructions.md
├── docs/
│   ├── event-storm.md
│   ├── protocol_manchester.md
│   └── Hackaton-9ADJT.md
├── service-registry/
│   ├── src/main/java
│   ├── src/test/java
│   ├── pom.xml
│   └── Dockerfile
├── api-gateway/
│   ├── src/main/java
│   ├── src/test/java
│   ├── pom.xml
│   └── Dockerfile
├── triage-service/
│   ├── src/main/java
│   │   └── br/com/fiap/susconnect/triage/
│   │       ├── core/
│   │       │   ├── domain/entity/
│   │       │   ├── gateway/
│   │       │   ├── usecase/
│   │       │   └── dto/
│   │       └── infra/
│   │           ├── entity/
│   │           ├── repository/
│   │           ├── gateway/
│   │           ├── web/
│   │           ├── config/
│   │           └── exception/
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/
│   ├── src/test/java
│   ├── pom.xml
│   └── Dockerfile
├── appointment-service/ (similar to triage-service)
├── medical-record-service/ (similar to triage-service)
├── docker-compose.yml
├── docker-compose.dev.yml
├── pom.xml (parent)
├── README.md
├── LICENSE
└── .gitignore
```

---

## 🤝 Contributing

### Code Style
- Google Java Format (enforced via Spotless)
- Maximum line length: 100 characters
- 2-space indentation

### Git Workflow
```bash
# Create feature branch
git checkout -b feature/your-feature

# Commit with conventional commits
git commit -m "feat: add new feature"

# Push and create pull request
git push origin feature/your-feature
```

### Conventional Commits
- `feat:` New feature
- `fix:` Bug fix
- `refactor:` Code refactoring
- `test:` Test additions
- `docs:` Documentation
- `chore:` Build/dependency updates

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📞 Support

For issues and questions:
- Create a GitHub Issue: https://github.com/eps364/tech-challenge-fase-05/issues
- Contact: tech-challenge@fiap.com.br

---

## 🎓 References

- [Manchester Protocol v3.0](https://pt.wikipedia.org/wiki/Protocolo_de_Manchester)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)

---

**Last Updated**: May 31, 2026  
**Version**: 1.0.0  
**Maintainers**: FIAP Tech Challenge Team

---

**Status**: Development Phase  
**Version**: 1.0.0  
**Last Updated**: May 31, 2024
