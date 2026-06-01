# API Collection (Bruno)

Esta pasta contem os endpoints implementados no projeto no formato Bruno.

## Endpoints incluidos

- Auth
  - POST /auth/register
  - POST /auth/login
  - POST /auth/refresh
  - POST /auth/logout
  - GET /auth/test/public
  - GET /auth/test/private
- Triage
  - POST /api/v1/triage
  - GET /api/v1/triage/{id}

## Uso rapido

1. Abra a pasta `docs/API` no Bruno.
2. Selecione o ambiente `environments/local.bru`.
3. Preencha os valores de `accessToken` e `refreshToken`.
4. Execute as requests.
