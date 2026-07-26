# URLs dos Servicos Ativos

O MVP ativo possui uma API de priorizacao territorial APS e seu banco de dados.
Nao ha gateway, agenda de pacientes, prontuario, mensageria ou servico de
agendamento no fluxo atual.

| Componente | URL ou porta | Uso |
| --- | --- | --- |
| APS Prioritization API | `http://localhost:8205/api/v1` | Endpoints do MVP. |
| Swagger UI | `http://localhost:8205/swagger-ui/index.html` | Demonstracao e exploracao da API. |
| Health | `http://localhost:8205/actuator/health` | Verificacao do servico. |
| APS PostgreSQL | `localhost:5434` | Persistencia de territorios, indicadores e acoes. |

Para iniciar o ambiente:

```bash
docker compose up -d --build aps-prioritization-service
docker compose ps aps-prioritization-service aps-prioritization-postgres
```
