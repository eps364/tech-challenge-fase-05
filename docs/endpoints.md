# Endpoints - Appointment Service

Base URL local: `http://localhost:8202`

Autenticacao: desabilitada no escopo reduzido para facilitar desenvolvimento
local da feature de agendamento.

## Pacientes

### GET /api/v1/patients

Lista a massa local de pacientes criada pelo Flyway.

## Agendamentos

### POST /api/v1/appointments

Cria uma consulta ou exame.

```json
{
  "patientId": "11111111-1111-1111-1111-111111111111",
  "professionalId": "99999999-9999-9999-9999-999999999991",
  "dateTime": "2026-07-10T10:00:00",
  "appointmentType": "EXAM",
  "serviceName": "Hemograma completo",
  "facilityName": "UBS Central",
  "preparationNotes": "Jejum de 8 horas. Beber agua normalmente."
}
```

Campos:

- `appointmentType`: `CONSULTATION` ou `EXAM`.
- `preparationNotes`: texto livre para jejum, medicamento, documentos ou
  outras condicoes especiais.

### GET /api/v1/appointments/{id}

Consulta um agendamento por ID.

### GET /api/v1/appointments?patientId={id}

Lista agendamentos de um paciente.

### PATCH /api/v1/appointments/{id}/cancel

Cancela um agendamento.

### PATCH /api/v1/appointments/{id}/cannot-attend

Marca que o paciente nao podera comparecer, cancela o horario e tenta gerar
uma oferta para outro paciente com agendamento futuro equivalente.

```json
{
  "reason": "Paciente informou que nao conseguira comparecer."
}
```

Resposta:

```json
{
  "appointment": {
    "id": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
    "status": "CANCELLED",
    "patientNotification": "Agendamento cancelado..."
  },
  "generatedOffer": {
    "id": "uuid",
    "candidatePatientId": "44444444-4444-4444-4444-444444444444",
    "status": "PENDING",
    "message": "Uma vaga abriu..."
  }
}
```

### POST /api/v1/appointments/notifications/reminders?hoursAhead=48

Atualiza a mensagem de notificacao dos agendamentos confirmados dentro da
janela informada.

## Ofertas de Vaga

### GET /api/v1/appointments/offers?patientId={id}

Lista ofertas pendentes para um paciente.

### PATCH /api/v1/appointments/offers/{offerId}/accept

Aceita a oferta e move o agendamento futuro do paciente para o horario aberto.

### PATCH /api/v1/appointments/offers/{offerId}/decline

Recusa a oferta sem alterar o agendamento atual do paciente.
