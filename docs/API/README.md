# API Collection (Bruno)

Esta pasta contém os endpoints implementados no projeto no formato Bruno.

## ⚠️ Status de Implementação

| Serviço | Status | Endpoints |
| --- | --- | --- |
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

Endpoints virão em Phase 2

### Medical Record Service 🔲 (Em Desenvolvimento)

Endpoints virão em Phase 2

## Uso rápido

1. Abra a pasta `docs/API` no Bruno.
2. Selecione o ambiente `environments/local.bru`.
3. Para endpoints autenticados, preencha os valores de `accessToken` obtidos via POST /auth/login.
4. Execute as requests.

## 📋 Fluxo Completo (Login → Uso → Refresh → Logout)

### Passo 1️⃣: Login (obter tokens)

Execute **`Auth → Login`** com credenciais válidas:

**Request:**

```json
POST /auth/login
{
  "username": "john4.doe",
  "password": "ChangeMe123!"
}
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJSUzI1NiIs...",
  "expiresIn": 300,
  "tokenType": "Bearer"
}
```

**O que acontece:**

- ✅ O script `post-response` extrai automaticamente `accessToken` e `refreshToken`
- ✅ Salva em variáveis de ambiente: `{{accessToken}}` e `{{refreshToken}}`
- ✅ Essas variáveis ficam disponíveis para todos os endpoints autenticados

---

### Passo 2️⃣: Usar o Access Token em endpoints autenticados

Agora você pode executar qualquer endpoint que requer autenticação. Exemplos:

#### Teste Privado (Auth Service)

Execute **`Auth → Test-Private`**:

```http
GET /auth/test/private
Authorization: Bearer {{accessToken}}
```

**Response esperada:**

```json
"Hello john4.doe! This is a private endpoint"
```

#### Criar Triagem (Triage Service)

Execute **`Triage → Create-Triage`**:

```http
POST /api/v1/triage
Authorization: Bearer {{accessToken}}
Content-Type: application/json

{
  "patientName": "João Silva",
  "age": 45,
  "symptoms": ["febre", "tosse"],
  "riskLevel": "medium"
}
```

#### Buscar Triagem por ID

Execute **`Triage → Get-Triage-By-Id`**:

```http
GET /api/v1/triage/{id}
Authorization: Bearer {{accessToken}}
```

---

### Passo 3️⃣: Renovar Token (quando expirar)

Quando o `accessToken` expirar (após `expiresIn` segundos), execute **`Auth → Refresh-Token`**:

**Request:**

```json
POST /auth/refresh
{
  "refreshToken": "{{refreshToken}}"
}
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJSUzI1NiIs...",
  "expiresIn": 300,
  "tokenType": "Bearer"
}
```

**O que acontece:**

- ✅ O script `post-response` automaticamente atualiza `{{accessToken}}`
- ✅ Você continua usando os endpoints sem interrupção
- ✅ Sem necessidade de fazer login novamente

---

### Passo 4️⃣: Logout (encerrar sessão)

Execute **`Auth → Logout`** para revogar o token:

**Request:**

```http
POST /auth/logout
Authorization: Bearer {{accessToken}}
```

**Response:**

```http
200 OK
"Logged out successfully"
```

**O que acontece:**

- ✅ Token é adicionado à blacklist no Redis
- ✅ Script `post-response` deleta `{{accessToken}}` automaticamente
- ✅ Endpoints autenticados retornarão 401 se você tentar usar o token antigo

---

## 🔄 Resumo Executivo do Fluxo

| Passo | Endpoint | Ação | Resultado |
| --- | --- | --- | --- |
| 1 | `POST /auth/login` | Enviar credenciais | Obtem `accessToken` e `refreshToken` |
| 2 | Qualquer endpoint autenticado | Usar `{{accessToken}}` no header `Authorization: Bearer` | Acessa recurso protegido |
| 3 | `POST /auth/refresh` | Usar `{{refreshToken}}` | Renova `accessToken` |
| 4 | `POST /auth/logout` | Usar `{{accessToken}}` | Revoga token e limpa variáveis |

## ⏱️ Tempos de Expiração

| Token | Duração | Renovação |
| --- | --- | --- |
| `accessToken` | 300 segundos (5 min) | Renove via `/auth/refresh` antes de expirar |
| `refreshToken` | Válido por mais tempo | Não precisa renovar, apenas use no refresh |

## 🛡️ Boas Práticas

1. **Sempre execute Login antes de qualquer request autenticada**
2. **Se receber 401, execute Refresh-Token para renovar**
3. **Se Refresh falhar (401), execute Login novamente**
4. **Ao terminar, execute Logout para limpar a sessão**
5. **Variáveis são salvas automaticamente** — não precisa copiar/colar tokens

## Acesso aos Swagger UI

Cada serviço expõe documentação OpenAPI em `/swagger-ui.html`:

| Serviço | URL |
| --- | --- |
| Auth Service | Via API Gateway: `http://localhost:8761/auth/swagger-ui.html` |
| Triage Service | Via API Gateway: `http://localhost:8761/triage/swagger-ui.html` |
