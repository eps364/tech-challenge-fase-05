# Endpoints - APS Prioritization Service

Base URL local: `http://localhost:8205/api/v1`

| Metodo | Endpoint | Objetivo |
| --- | --- | --- |
| `GET` | `/dashboard` | Resume prioridades e acoes territoriais. |
| `GET` | `/territories` | Lista territorios por prioridade e foco preventivo. |
| `GET` | `/territories/{territoryId}` | Explica a prioridade e mostra historico agregado. |
| `POST` | `/territories` | Cria um territorio com indicadores agregados. |
| `PUT` | `/territories/{territoryId}/indicators` | Substitui os indicadores agregados de um territorio. |
| `POST` | `/territories/{territoryId}/actions` | Cria uma acao territorial de busca ativa. |
| `PATCH` | `/actions/{actionId}/progress` | Atualiza status e progresso agregado da acao. |

Documentacao completa, exemplos e IDs de massa:
[`docs/API/aps-prioritization-service.md`](API/aps-prioritization-service.md).
