# URLs dos Serviços - Desenvolvimento

Documentação das URLs de acesso aos serviços da aplicação **SUS-Connect Healthcare Platform** em ambiente de desenvolvimento.

---

## 📋 Índice

- [Serviços de Infraestrutura](#serviços-de-infraestrutura)
- [Microserviços da Aplicação](#microserviços-da-aplicação)
- [Bancos de Dados](#bancos-de-dados)
- [Acesso via Docker Compose](#acesso-via-docker-compose)
- [Acesso para Desenvolvimento Local](#acesso-para-desenvolvimento-local)
- [Health Checks](#health-checks)

---

## 🔧 Serviços de Infraestrutura

### Keycloak (Authentication Server)
- **URL**: http://localhost:8080
- **Porta**: 8080
- **Admin Console**: http://localhost:8080/admin
- **Credentials**: 
  - Usuário: `admin`
  - Senha: `admin`
- **Realm**: `sus-connect`
- **OpenID Configuration**: http://localhost:8080/realms/sus-connect/.well-known/openid-configuration
- **JWK Set URI**: http://localhost:8080/realms/sus-connect/protocol/openid-connect/certs
- **Descrição**: Serviço de autenticação e autorização centralizado

### Eureka Service Registry
- **URL**: http://localhost:8762
- **Porta**: 8762
- **Dashboard**: http://localhost:8762/eureka/web
- **Descrição**: Registro centralizado de serviços (Service Discovery)
- **Container**: `service-registry`

### Config Server
- **URL**: http://localhost:8888
- **Porta**: 8888
- **Health Check**: http://localhost:8888/actuator/health
- **Config Repo Path**: `/config-repo`
- **Descrição**: Servidor centralizado de configurações (Spring Cloud Config)
- **Container**: `config-server`

### Kafka (Message Broker)
- **Broker Address**: localhost:9092
- **Porta**: 9092
- **Zookeeper**: localhost:2181
- **Container**: `kafka`
- **Descrição**: Broker de mensagens para comunicação assíncrona entre serviços

### Redis (Cache)
- **URL**: redis://localhost:6379
- **Porta**: 6379
- **Container**: `redis`
- **Descrição**: Cache em memória para sessões e dados frequentemente acessados

---

## 🚀 Microserviços da Aplicação

> **Nota**: Os microserviços utilizam **portas dinâmicas** (SERVER_PORT: 0) e são registrados no Eureka.
> Para acesso direto em desenvolvimento, você pode:
> 1. Verificar a porta atribuída no dashboard do Eureka (http://localhost:8762)
> 2. Usar a API Gateway como ponto de entrada único

### API Gateway
- **Service ID**: `api-gateway`
- **Base URL**: http://localhost:8761
- **Porta**: 8761 (padrão)
- **Descrição**: Ponto de entrada centralizado para todos os microserviços
- **Container**: `api-gateway`
- **Rotas Disponíveis**:
  - `/triage/**` → `triage-service`
  - `/appointment/**` → `appointment-service`
  - `/auth/**` → `auth-service`
  - `/medical-record/**` → `medical-record-service`

### Auth Service
- **Service ID**: `auth-service`
- **Porta**: Dinâmica (registrada no Eureka)
- **Descrição**: Serviço de autenticação e gerenciamento de tokens JWT
- **Container**: `auth-service`
- **Database**: `auth-service-db` (Postgres, porta 5431)
- **Cache**: Redis (porta 6379)
- **Keycloak Integration**: Sincronização com Keycloak

### Triage Service
- **Service ID**: `triage-service`
- **Porta**: Dinâmica (registrada no Eureka)
- **Descrição**: Serviço de triagem com protocolo Manchester
- **Container**: `triage-service`
- **Database**: `triage-postgres` (Postgres, porta 5432)
- **Message Queue**: Kafka (porta 9092)

### Appointment Service
- **Service ID**: `appointment-service`
- **Porta**: Dinâmica (registrada no Eureka)
- **Descrição**: Serviço de agendamento de consultas
- **Container**: `appointment-service`
- **Database**: `appointment-postgres` (Postgres, porta 5433)
- **Message Queue**: Kafka (porta 9092)

### Medical Record Service
- **Service ID**: `medical-record-service`
- **Porta**: Dinâmica (registrada no Eureka)
- **Descrição**: Serviço de gerenciamento de prontuários médicos
- **Container**: `medical-record-service`
- **Database**: `medical-record-postgres` (Postgres, porta 5434)
- **Message Queue**: Kafka (porta 9092)

---

## 💾 Bancos de Dados

### Keycloak Database
- **Host**: localhost
- **Porta**: 5430
- **Database**: keycloak_db
- **Usuário**: postgres
- **Senha**: password
- **Container**: `keycloak-postgres`
- **Connection String**: `postgresql://postgres:password@localhost:5430/keycloak_db`

### Auth Service Database
- **Host**: localhost
- **Porta**: 5431
- **Database**: auth_db
- **Usuário**: postgres
- **Senha**: password
- **Container**: `auth-service-db`
- **Connection String**: `postgresql://postgres:password@localhost:5431/auth_db`

### Triage Service Database
- **Host**: localhost
- **Porta**: 5432
- **Database**: triage_db
- **Usuário**: postgres
- **Senha**: password
- **Container**: `triage-postgres`
- **Connection String**: `postgresql://postgres:password@localhost:5432/triage_db`

### Appointment Service Database
- **Host**: localhost
- **Porta**: 5433
- **Database**: appointment_db
- **Usuário**: postgres
- **Senha**: password
- **Container**: `appointment-postgres`
- **Connection String**: `postgresql://postgres:password@localhost:5433/appointment_db`

### Medical Record Service Database
- **Host**: localhost
- **Porta**: 5434
- **Database**: medical_record_db
- **Usuário**: postgres
- **Senha**: password
- **Container**: `medical-record-postgres`
- **Connection String**: `postgresql://postgres:password@localhost:5434/medical_record_db`

---

## 🐳 Acesso via Docker Compose

### Iniciar todos os serviços:
```bash
docker compose up --build
```

### Iniciar apenas banco de dados:
```bash
docker compose up -d postgres
```

### Parar todos os serviços:
```bash
docker compose down
```

### Logs de um serviço específico:
```bash
docker compose logs -f <service-name>
```

---

## 💻 Acesso para Desenvolvimento Local

Se estiver rodando os microserviços **localmente** (não no Docker):

### 1. Inicie a infraestrutura
```bash
# Inicie apenas o Docker (Keycloak, Postgres, Kafka, Redis, Eureka, Config Server)
docker compose up -d
```

### 2. Execute um serviço com Hot Reload
```bash
./mvnw spring-boot:run -Dspring-boot.devtools.restart.enabled=true
```

### 3. Execute com Debug
```bash
./mvnw spring-boot:run \
  -Dspring-boot.devtools.restart.enabled=true \
  -Dspring-boot.run.fork=false \
  -Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'
```

### 4. Variáveis de Ambiente Necessárias

```bash
# Eureka
export EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8762/eureka/

# Config Server
export SPRING_CLOUD_CONFIG_URI=http://localhost:8888

# JWT/Keycloak
export SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://localhost:8080/realms/sus-connect
export JWT_JWK_SET_URI=http://localhost:8080/realms/sus-connect/protocol/openid-connect/certs

# Kafka
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Redis
export SPRING_DATA_REDIS_HOST=localhost
export SPRING_DATA_REDIS_PORT=6379

# Database (exemplo para Triage)
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/triage_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=password
```

---

## ✅ Health Checks

### Verificar saúde de um serviço:
```bash
curl http://localhost:8888/actuator/health
curl http://localhost:8762/actuator/health
```

### Verificar todos os serviços registrados no Eureka:
```bash
curl http://localhost:8762/eureka/apps
```

### Verificar disponibilidade do Keycloak:
```bash
curl http://localhost:8080/realms/master/.well-known/openid-configuration
```

### Verificar disponibilidade do Kafka:
```bash
docker exec kafka kafka-broker-api-versions --bootstrap-server localhost:9092
```

### Verificar disponibilidade do Redis:
```bash
redis-cli -p 6379 ping
```

---

## 📚 Endpoints da API

Para documentação completa dos endpoints da aplicação, consulte:
- [API Documentation](./docs/API/README.md)
- Bruno Collection: [docs/API/collection.bru](./docs/API/collection.bru)

---

## 🔐 Credenciais Padrão

| Serviço | Usuário | Senha |
|---------|---------|-------|
| Keycloak Admin | admin | admin |
| PostgreSQL | postgres | password |
| Realm (Keycloak) | sus-connect | - |

⚠️ **IMPORTANTE**: Alterar as credenciais em produção!

---

## 📝 Notas Importantes

1. **Portas Dinâmicas**: Os microserviços usam portas dinâmicas para evitar conflitos. Verifique o Eureka Dashboard para encontrar a porta de cada serviço.

2. **Network**: Todos os containers executam na rede `susconnect-network`, permitindo comunicação interna via nome do container.

3. **Health Checks**: Cada container possui health checks configurados. Use `docker compose ps` para verificar o status.

4. **Desenvolvimento Local**: Se executar serviços localmente (não no Docker), use `localhost` ou `127.0.0.1`. Para containers comunicarem com o host, use `host.docker.internal`.

5. **Configurações**: As configurações centralizadas estão em [config-repo/](./config-repo/), gerenciadas pelo Config Server.

---

## 🚀 Próximos Passos

- [x] Documentar URLs dos serviços
- [ ] Documentar fluxos de autenticação
- [ ] Documentar padrões de comunicação assíncrona
- [ ] Adicionar exemplos de requisições com cURL/Postman
- [ ] Documentar estratégia de deploys

---

**Última atualização**: Junho de 2026
