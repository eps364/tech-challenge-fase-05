# Correção de Erros do Docker

## Problema Identificado

### 1. Keycloak - Erro de Validação Liquibase
**Erro**: `Validation Failed: changesets check sum`

**Causa**: O banco de dados do Keycloak foi criado com uma versão diferente e o checksum mudou.

**Solução**: Remover o volume do banco de dados do Keycloak
```bash
docker compose down -v
```

### 2. Kafka - Container Unhealthy
**Erro**: `container kafka is unhealthy`

**Causa**: O healthcheck do Kafka estava falhando antes do serviço estar completamente pronto.

**Solução Implementada**: 
- Aumentado `start_period` para 40s
- Aumentado `retries` para 10
- Adicionado redirecionamento de stderr no healthcheck

## Como Corrigir

### Opção 1: Script Automático (Recomendado)

```bash
chmod +x fix-docker-issues.sh
./fix-docker-issues.sh
```

### Opção 2: Comandos Manuais

```bash
# 1. Parar tudo e remover volumes
docker compose down -v

# 2. Limpar volumes órfãos
docker volume prune -f

# 3. Build das imagens
docker compose build

# 4. Subir em ordem
docker compose up -d keycloak-postgres zookeeper
sleep 20

docker compose up -d kafka
sleep 20

docker compose up -d keycloak
sleep 30

docker compose up -d config-server service-registry
sleep 20

docker compose up -d
```

### Opção 3: Desenvolvimento (Modo Rápido)

Use o docker-compose.dev.yml que tem healthchecks otimizados:

```bash
docker compose -f docker-compose.yml -f docker-compose.dev.yml down -v
docker compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

## Verificar Status

```bash
# Ver status de todos os containers
docker compose ps

# Ver logs em tempo real
docker compose logs -f

# Ver logs de um serviço específico
docker compose logs -f keycloak
docker compose logs -f kafka

# Verificar saúde de um container
docker inspect --format='{{json .State.Health}}' keycloak | jq
docker inspect --format='{{json .State.Health}}' kafka | jq
```

## Ordem de Inicialização Recomendada

1. **Bancos de Dados** (5-10s cada)
   - keycloak-postgres
   - auth-service-db
   - triage-postgres
   - appointment-postgres
   - medical-record-postgres

2. **Infraestrutura** (15-30s cada)
   - zookeeper (10s)
   - redis (5s)
   - kafka (20-30s) ⚠️ **Mais lento**

3. **Auth** (30-60s)
   - keycloak (30-60s) ⚠️ **Mais lento**

4. **Config** (10-20s cada)
   - config-server (10s)
   - service-registry (15s)

5. **Gateway e Microserviços** (20-30s cada)
   - api-gateway
   - auth-service
   - triage-service
   - appointment-service
   - medical-record-service

## Troubleshooting

### Keycloak não sobe

```bash
# Ver logs detalhados
docker compose logs keycloak

# Se persistir erro de Liquibase
docker volume rm tech-challenge-fase-05_keycloak_postgres_data
docker compose up -d keycloak-postgres
sleep 10
docker compose up -d keycloak
```

### Kafka unhealthy

```bash
# Verificar logs do Zookeeper primeiro
docker compose logs zookeeper

# Verificar logs do Kafka
docker compose logs kafka

# Testar manualmente
docker exec -it kafka kafka-broker-api-versions --bootstrap-server localhost:9092

# Se necessário, restart
docker compose restart kafka
```

### Serviços não encontram o Config Server

```bash
# Verificar se config-server está rodando
docker compose ps config-server

# Verificar logs
docker compose logs config-server

# Restart dos serviços dependentes
docker compose restart auth-service triage-service appointment-service medical-record-service
```

## Alterações Feitas

1. ✅ **docker-compose.yml**: Melhorado healthcheck do Kafka
   - `start_period: 40s` (era 0)
   - `retries: 10` (era 5)
   - Redirecionamento de stderr no comando

2. ✅ **fix-docker-issues.sh**: Script criado para automação completa

3. ✅ **docker/keycloak/Dockerfile**: Restaurado para versão 26.5.4
