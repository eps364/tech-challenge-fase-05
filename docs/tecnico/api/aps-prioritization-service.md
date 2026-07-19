# APS Prioritization Service

Base URL local: `http://localhost:8205/api/v1`

Este servico prioriza territorios/UBS para busca ativa preventiva. Ele usa
indicadores agregados e nao aceita dados pessoais ou prontuarios.

## Massa de demonstracao

| Territorio | ID | Situacao inicial |
| --- | --- | --- |
| Jardim Esperanca | `10000000-0000-0000-0000-000000000001` | Alta prioridade: vinculo APS de 42% e indicadores de condicoes cronicas/prenatal abaixo da meta. |
| Vila Nova | `10000000-0000-0000-0000-000000000002` | Media prioridade e uma acao vencida para chamar a atencao do gestor. |
| Parque das Flores | `10000000-0000-0000-0000-000000000003` | Media prioridade por vinculo abaixo da meta. |
| Centro | `10000000-0000-0000-0000-000000000004` | Baixa prioridade e uma acao concluida. |

## Endpoints

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `GET` | `/dashboard` | Resume territorios prioritarios, acoes abertas, concluidas e em atencao. |
| `GET` | `/territories?priority=HIGH&focus=CHRONIC_CONDITIONS` | Lista territorios ordenados por prioridade. Os filtros sao opcionais. |
| `GET` | `/territories/{territoryId}` | Explica a prioridade, mostra indicadores e historico agregado de acoes. |
| `POST` | `/territories` | Carrega um territorio com indicadores agregados. |
| `PUT` | `/territories/{territoryId}/indicators` | Substitui os indicadores agregados de um territorio. |
| `POST` | `/territories/{territoryId}/actions` | Cria uma acao territorial de busca ativa. |
| `PATCH` | `/actions/{actionId}/progress` | Atualiza a situacao e o progresso agregado de uma acao. |

## Regra de prioridade

- `HIGH`: vinculo APS abaixo da meta configurada e pelo menos um indicador preventivo abaixo da meta.
- `MEDIUM`: apenas um dos sinais esta abaixo da meta.
- `LOW`: vinculo e indicadores atendem as metas.

A meta de vinculo inicial e 50%, configurada por `APS_LINKAGE_TARGET`. O
resultado e apoio administrativo explicavel, nao diagnostico ou previsao clinica.

## Exemplo: criar uma acao para Jardim Esperanca

```json
{
  "focus": "CHRONIC_CONDITIONS",
  "objective": "Reconnect people with chronic conditions to preventive follow-up",
  "responsibleTeam": "ESF Jardim Esperanca",
  "plannedStart": "2026-07-18",
  "plannedEnd": "2026-07-25",
  "targetCount": 80,
  "notes": "Demonstration action with aggregate counts only"
}
```

Em seguida, atualize o resultado de forma agregada:

```json
{
  "status": "IN_PROGRESS",
  "performedCount": 54,
  "resultNotes": "54 contacts made and 31 people reconnected to follow-up"
}
```

## Colecoes de teste

- Bruno: `docs/tecnico/api/Aps-Prioritization`.
- Insomnia: `docs/tecnico/api/aps-prioritization-insomnia.json`.

As duas colecoes executam o fluxo demonstrativo sem depender de dados de pacientes.
