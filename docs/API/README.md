# API Collection (Bruno)

Esta pasta contém todos os endpoints do projeto no formato Bruno.

## Status de Implementação

| Serviço | Status | Endpoints |
| --- | --- | --- |
| Auth Service | ✅ Ativo | 6 endpoints |
| Triage Service | ✅ Ativo | 3 endpoints |
| Appointment Service | ✅ Ativo | 4 endpoints |
| Medical Record Service | ✅ Ativo | 4 endpoints |

## Variáveis de Ambiente (`environments/local.bru`)

| Variável | Valor padrão | Descrição |
| --- | --- | --- |
| `authBaseUrl` | `http://localhost:8761` | Base URL do Auth Service via Gateway |
| `triageBaseUrl` | `http://localhost:8761/triage` | Base URL do Triage Service via Gateway |
| `appointmentBaseUrl` | `http://localhost:8761/appointment` | Base URL do Appointment Service via Gateway |
| `medicalRecordBaseUrl` | `http://localhost:8761/medical-record` | Base URL do Medical Record Service via Gateway |
| `accessToken` | — | Preenchido automaticamente pelo script do Login |
| `refreshToken` | — | Preenchido automaticamente pelo script do Login |
| `triageId` | UUID de exemplo | Preenchido automaticamente pelo Create Triage |
| `appointmentId` | UUID de exemplo | Preenchido automaticamente pelo Schedule Appointment |
| `medicalRecordId` | UUID de exemplo | Preenchido automaticamente pelo Create Medical Record |
| `patientId` | UUID de exemplo | ID do paciente usado nos requests |

## Endpoints

### Auth Service ✅

| Seq | Método | Path | Auth | Descrição |
| --- | --- | --- | --- | --- |
| 1 | `POST` | `/auth/register` | ❌ | Registro de novo usuário |
| 2 | `POST` | `/auth/login` | ❌ | Autenticação — salva tokens automaticamente |
| 3 | `POST` | `/auth/refresh` | ❌ | Renovação de access token |
| 4 | `POST` | `/auth/logout` | ✅ | Logout — revoga token via Redis blacklist |
| 5 | `GET` | `/auth/test/public` | ❌ | Endpoint público de diagnóstico |
| 6 | `GET` | `/auth/test/private` | ✅ | Endpoint privado de diagnóstico |

### Triage Service ✅

| Seq | Método | Path | Auth | Descrição |
| --- | --- | --- | --- | --- |
| 1 | `POST` | `/api/v1/triage` | ✅ | Cria nova triagem (inicia como BLUE) — salva `triageId` |
| 2 | `GET` | `/api/v1/triage/{id}` | ✅ | Busca triagem por ID |
| 3 | `PATCH` | `/api/v1/triage/{id}/classify` | ✅ | Classifica nível de risco (Manchester Protocol) |

**Níveis de risco válidos:** `RED`, `ORANGE`, `YELLOW`, `GREEN`, `BLUE`

### Appointment Service ✅

| Seq | Método | Path | Auth | Descrição |
| --- | --- | --- | --- | --- |
| 1 | `POST` | `/api/v1/appointments` | ✅ | Agenda consulta — salva `appointmentId` |
| 2 | `GET` | `/api/v1/appointments/{id}` | ✅ | Busca consulta por ID |
| 3 | `PATCH` | `/api/v1/appointments/{id}/cancel` | ✅ | Cancela uma consulta |
| 4 | `GET` | `/api/v1/appointments?patientId={id}` | ✅ | Lista consultas de um paciente |

### Medical Record Service ✅

| Seq | Método | Path | Auth | Descrição |
| --- | --- | --- | --- | --- |
| 1 | `POST` | `/api/v1/medical-records` | ✅ | Cria prontuário — salva `medicalRecordId` |
| 2 | `GET` | `/api/v1/medical-records/{id}` | ✅ | Busca prontuário por ID |
| 3 | `PATCH` | `/api/v1/medical-records/{id}` | ✅ | Atualiza diagnóstico / prescrição |
| 4 | `GET` | `/api/v1/medical-records?patientId={id}` | ✅ | Lista prontuários de um paciente |

---

## Uso rápido

1. Abra a pasta `docs/API` no Bruno.
2. Selecione o ambiente `environments/local.bru`.
3. Execute as requests na ordem sugerida abaixo.

---

## 📋 Fluxo Completo

### 1️⃣ Registrar usuário (se necessário)

Execute **`Auth → Register`**:

```json
POST /auth/register
{
  "username": "john4.doe",
  "email": "john.doe4@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "ChangeMe123!"
}
```

**Response (201 Created):** `"User registered successfully"`

---

### 2️⃣ Login (obter tokens)

Execute **`Auth → Login`**:

```json
POST /auth/login
{
  "username": "john4.doe",
  "password": "ChangeMe123!"
}
```

**Response (200 OK):**

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJSUzI1NiIs...",
  "expiresIn": 300,
  "tokenType": "Bearer"
}
```

✅ `accessToken` e `refreshToken` são salvos automaticamente nas variáveis de ambiente.

---

### 3️⃣ Criar Triagem

Execute **`Triage → Create Triage`**:

```http
POST /triage/api/v1/triage
Authorization: Bearer {{accessToken}}
Content-Type: application/json

{
  "patientId": "{{patientId}}"
}
```

**Response (201 Created):**

```json
{
  "id": "uuid",
  "patientId": "uuid",
  "riskLevel": "BLUE",
  "createdAt": "2026-06-10T10:00:00",
  "updatedAt": "2026-06-10T10:00:00"
}
```

✅ `triageId` salvo automaticamente.

---

### 4️⃣ Classificar Risco (Manchester Protocol)

Execute **`Triage → Classify Triage`**:

```http
PATCH /triage/api/v1/triage/{{triageId}}/classify
Authorization: Bearer {{accessToken}}
Content-Type: application/json

{
  "riskLevel": "YELLOW"
}
```

**Response (200 OK):** `TriageOutput` com `riskLevel` atualizado.

---

### 5️⃣ Agendar Consulta

Execute **`Appointment → Schedule Appointment`**:

```json
POST /appointment/api/v1/appointments
{
  "triageId": "{{triageId}}",
  "patientId": "{{patientId}}",
  "dateTime": "2026-06-10T10:00:00"
}
```

**Response (201 Created):** `AppointmentOutput` — ✅ `appointmentId` salvo automaticamente.

---

### 6️⃣ Criar Prontuário

Execute **`Medical Record → Create Medical Record`**:

```json
POST /medical-record/api/v1/medical-records
{
  "appointmentId": "{{appointmentId}}",
  "patientId": "{{patientId}}",
  "diagnosis": "Acute pharyngitis",
  "prescription": "Amoxicillin 500mg 8/8h for 7 days",
  "consultationDate": "2026-06-10T10:30:00"
}
```

**Response (201 Created):** `MedicalRecordOutput` — ✅ `medicalRecordId` salvo automaticamente.

---

### 7️⃣ Renovar Token (quando expirar)

Execute **`Auth → Refresh Token`**:

```json
POST /auth/refresh
{
  "refreshToken": "{{refreshToken}}"
}
```

✅ `accessToken` atualizado automaticamente.

---

### 8️⃣ Logout

Execute **`Auth → Logout`**:

```http
POST /auth/logout
Authorization: Bearer {{accessToken}}
```

**Response (200 OK):** `"Logged out successfully"`

✅ Token adicionado à blacklist Redis. Variáveis limpas automaticamente.

---

## ⏱️ Tempos de Expiração

| Token | Duração | Ação |
| --- | --- | --- |
| `accessToken` | 300 segundos (5 min) | Renove via `/auth/refresh` |
| `refreshToken` | Longa duração | Use apenas no refresh |

## 🛡️ Boas Práticas

1. **Execute Login antes de qualquer request autenticada**
2. **Se receber 401, execute Refresh Token para renovar**
3. **Se Refresh falhar (401), execute Login novamente**
4. **Ao terminar, execute Logout para revogar o token**
5. **Scripts `post-response` salvam IDs automaticamente** — execute Create antes de Get/Patch

## 🔗 Swagger UI

| Serviço | URL |
| --- | --- |
| Auth Service | `http://localhost:8761/auth/swagger-ui.html` |
| Triage Service | `http://localhost:8761/triage/swagger-ui.html` |
| Appointment Service | `http://localhost:8761/appointment/swagger-ui.html` |
| Medical Record Service | `http://localhost:8761/medical-record/swagger-ui.html` |
