# Endpoints - Tech Challenge Fase 05

Documento de referencia dos endpoints implementados no projeto, seguindo o modelo da Fase 03 (visao por servico + detalhe de cada rota).

## Resumo por servico

| Servico | Metodo | Endpoint | Autenticacao | Descricao |
| --- | --- | --- | --- | --- |
| Auth Service | POST | `/auth/register` | Publica | Registra novo usuario. |
| Auth Service | POST | `/auth/login` | Publica | Autentica usuario e retorna tokens. |
| Auth Service | POST | `/auth/refresh` | Publica (com refresh token) | Gera novo access token. |
| Auth Service | POST | `/auth/logout` | Bearer token | Efetua logout e revoga token atual. |
| Auth Service | GET | `/auth/test/public` | Publica | Endpoint de teste publico. |
| Auth Service | GET | `/auth/test/private` | Bearer token | Endpoint de teste autenticado. |
| Triage Service | POST | `/api/v1/triage` | Bearer token | Cria triagem — retorna id, riskLevel, createdAt. |
| Triage Service | GET | `/api/v1/triage/{id}` | Bearer token | Placeholder — retorna "OK". |

## Detalhe de cada endpoint

## Auth Service

Base URL via API Gateway: `http://localhost:8761`  
Base URL direta (porta dinâmica — verifique no Eureka): `http://localhost:<porta-dinâmica>`

### 1) POST /auth/register

- Objetivo: registrar um novo usuario.
- Autenticacao: nao requer token.
- Headers:
  - `Content-Type: application/json`
  - `Accept: application/json`
- Body (JSON):

```json
{
  "username": "john.doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "ChangeMe123!"
}
```

- Resposta de sucesso:
  - `201 Created`
  - Body:

```text
User registered successfully
```

- Erros comuns:
  - `400 Bad Request` (RuntimeException)

### 2) POST /auth/login

- Objetivo: autenticar usuario e retornar access token e refresh token.
- Autenticacao: nao requer token.
- Headers:
  - `Content-Type: application/json`
  - `Accept: application/json`
- Body (JSON):

```json
{
  "username": "john.doe",
  "password": "ChangeMe123!"
}
```

- Resposta de sucesso:
  - `200 OK`
  - Body (JSON):

```json
{
  "accessToken": "<jwt>",
  "refreshToken": "<refresh-token>",
  "expiresIn": 300,
  "tokenType": "Bearer"
}
```

- Erros comuns:
  - `401 Unauthorized` (AuthenticationException)
  - `400 Bad Request` (RuntimeException)

### 3) POST /auth/refresh

- Objetivo: gerar novos tokens a partir do refresh token.
- Autenticacao: nao requer bearer token no header; requer refresh token no body.
- Headers:
  - `Content-Type: application/json`
  - `Accept: application/json`
- Body (JSON):

```json
{
  "refreshToken": "<refresh-token>"
}
```

- Resposta de sucesso:
  - `200 OK`
  - Body (JSON):

```json
{
  "accessToken": "<jwt>",
  "refreshToken": "<refresh-token>",
  "expiresIn": 300,
  "tokenType": "Bearer"
}
```

- Erros comuns:
  - `401 Unauthorized` (AuthenticationException)
  - `400 Bad Request` (RuntimeException)

### 4) POST /auth/logout

- Objetivo: realizar logout do token atual.
- Autenticacao: requer bearer token.
- Headers:
  - `Authorization: Bearer <access-token>`
  - `Accept: application/json`
- Body: nao possui.

- Resposta de sucesso:
  - `200 OK`
  - Body:

```text
Logged out successfully
```

- Erros comuns:
  - `401 Unauthorized` (token ausente/invalido)
  - `400 Bad Request` (RuntimeException)

### 5) GET /auth/test/public

- Objetivo: validar endpoint publico do servico.
- Autenticacao: nao requer token.
- Headers:
  - `Accept: application/json`
- Body: nao possui.

- Resposta de sucesso:
  - `200 OK`
  - Body:

```text
This is a public endpoint
```

### 6) GET /auth/test/private

- Objetivo: validar endpoint autenticado do servico.
- Autenticacao: requer bearer token.
- Headers:
  - `Authorization: Bearer <access-token>`
  - `Accept: application/json`
- Body: nao possui.

- Resposta de sucesso:
  - `200 OK`
  - Body (exemplo):

```text
Hello john.doe! This is a private endpoint
```

- Erros comuns:
  - `401 Unauthorized` (token ausente/invalido)

## Triage Service

Base URL via API Gateway: `http://localhost:8761`  
Base URL direta (porta dinâmica — verifique no Eureka): `http://localhost:<porta-dinâmica>`

Observacao: o endpoint POST /api/v1/triage está funcional. O endpoint GET /api/v1/triage/{id} é placeholder (retorna "OK").

### 7) POST /api/v1/triage

- Objetivo: criar uma nova triagem com classificação inicial BLUE (Manchester Protocol).
- Autenticacao: requer bearer token.
- Headers:
  - `Authorization: Bearer <access-token>`
  - `Content-Type: application/json`
  - `Accept: application/json`
- Body (JSON):

```json
{
  "patientId": "550e8400-e29b-41d4-a716-446655440000"
}
```

- Resposta de sucesso:
  - `201 Created`
  - Body (JSON):

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "riskLevel": "BLUE",
  "createdAt": "2026-06-05T20:00:00"
}
```

- Erros comuns:
  - `400 Bad Request` (patientId nulo ou inválido)

### 8) GET /api/v1/triage/{id}

- Objetivo: consultar triagem por identificador.
- Autenticacao: requer bearer token.
- Headers:
  - `Authorization: Bearer <access-token>`
  - `Accept: application/json`
- Path param:
  - `id` (UUID)
- Body: nao possui.

- Resposta de sucesso:
  - `200 OK`
  - Body atual:

```text
OK
```

- Erros comuns:
  - `400 Bad Request` (UUID invalido)

## Endpoints ainda nao implementados nesta fase

| Servico | Status |
| --- | --- |
| Appointment Service | Em desenvolvimento |
| Medical Record Service | Em desenvolvimento |
