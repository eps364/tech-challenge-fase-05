# API Collection (Bruno)

Esta pasta contém os endpoints implementados no projeto no formato Bruno.

## ⚠️ Status de Implementação

| Serviço | Status | Endpoints |
|---------|--------|-----------|
| Auth Service | ✅ Ativo | 6 endpoints |
| Triage Service | ⚠️ Skeleton | 2 endpoints |
| Appointment Service | 🔲 Em Desenvolvimento | Nenhum ainda |
| Medical Record Service | 🔲 Em Desenvolvimento | Nenhum ainda |

## Endpoints incluidos

### Auth Service ✅
- `POST /auth/register` — Registro de novo usuário
- `POST /auth/login` — Autenticação
- `POST /auth/refresh` — Renovação de token
- `POST /auth/logout` — Logout (autenticado)
- `GET /auth/test/public` — Teste público
- `GET /auth/test/private` — Teste privado (requer autenticação)

### Triage Service ⚠️ (Endpoints Skeleton)
- `POST /api/v1/triage` — Criar nova triagem
- `GET /api/v1/triage/{id}` — Buscar triagem por ID

### Appointment Service 🔲 (Em Desenvolvimento)
```
(Endpoints virão em Phase 2)
```

### Medical Record Service 🔲 (Em Desenvolvimento)
```
(Endpoints virão em Phase 2)
```

## Uso rápido

1. Abra a pasta `docs/API` no Bruno.
2. Selecione o ambiente `environments/local.bru`.
3. Para endpoints autenticados, preencha os valores de `accessToken` obtidos via POST /auth/login.
4. Execute as requests.

## Acesso aos Swagger UI

Cada serviço expõe documentação OpenAPI em `/swagger-ui.html`:

| Serviço | URL |
|---------|-----|
| Auth Service | Via API Gateway: http://localhost:8761/auth/swagger-ui.html |
| Triage Service | Via API Gateway: http://localhost:8761/triage/swagger-ui.html |
