---
description: Como rodar o ambiente completo do SUS-Connect com Docker Compose
---

# Fluxo de Execução do Ambiente Completo

Este workflow descreve os passos para construir e iniciar o ecossistema de microsserviços do SUS-Connect Triagem Inteligente.

## Pré-requisitos

- Docker Desktop ou Docker Engine com Compose habilitado.
- Java 17+ instalado (para build Maven opcionalmente).

---

## Modo Produção (build completo)

Gera imagem Docker otimizada para cada serviço.

1. **Subir todos os serviços:**

```bash
docker compose up -d --build
```

1. **Verificar o status dos serviços:**

```bash
docker compose ps
```

1. **Parar tudo e limpar volumes:**

```bash
docker compose down -v
```

---

## Modo Dev (hot reload)

Usa overrides de `docker-compose.dev.yml` com health checks desabilitados e logging DEBUG. Qualquer alteração em `.java` ou `resources` reinicia automaticamente o contexto do serviço.

1. **Subir o ambiente dev:**

```bash
docker compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

1. **Verificar logs de um serviço:**

```bash
docker compose -f docker-compose.yml -f docker-compose.dev.yml logs -f triage-service
```

1. **Rebuild de um serviço específico:**

```bash
docker compose -f docker-compose.yml -f docker-compose.dev.yml restart triage-service
```

1. **Parar o ambiente dev:**

```bash
docker compose -f docker-compose.yml -f docker-compose.dev.yml down
```

---

## Subir apenas a infraestrutura (para desenvolvimento local com Maven)

```bash
docker compose up -d keycloak-postgres keycloak zookeeper kafka triage-postgres appointment-postgres medical-record-postgres auth-service-db redis
```

Em seguida, rodar cada serviço localmente:

```bash
cd triage-service && ../mvnw spring-boot:run
```

---

## Serviços e Portas

| Serviço | URL | Descrição |
| --- | --- | --- |
| Service Registry (Eureka) | `http://localhost:8762` | Dashboard de descoberta de serviços |
| API Gateway | `http://localhost:8761` | Ponto de entrada externo |
| Keycloak | `http://localhost:8080` | Identity Provider (admin/admin) |
| Triage Service | `http://localhost:8201` | Classificação Manchester |
| Appointment Service | `http://localhost:8202` | Agendamentos |
| Medical Record Service | `http://localhost:8203` | Prontuários |
| Kafka | `localhost:9092` | Message broker |
| Redis | `localhost:6379` | Cache |

## Bancos de Dados

| Banco | Porta | Credenciais |
| --- | --- | --- |
| `triage_db` | `5432` | postgres/password |
| `appointment_db` | `5433` | postgres/password |
| `medical_record_db` | `5434` | postgres/password |
| `auth_db` | `5431` | postgres/password |
| `keycloak_db` | `5430` | postgres/password |
