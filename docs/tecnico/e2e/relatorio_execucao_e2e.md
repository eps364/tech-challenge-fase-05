# Relatorio de execucao E2E - APS

Gerado em: 2026-07-18T21:54:30-03:00

Este fluxo usa a API real no Docker em `http://localhost:8215`, PostgreSQL dedicado e uma base limpa.
Nenhum dado individual, prontuario ou identificador de paciente e enviado.

## Ambiente

- Compose: `docker-compose.e2e.yml`
- Servico: `aps-e2e-service` na porta 8215
- Banco: `aps-e2e-postgres`, schema criado pelo Flyway
- Massa demonstrativa automatica: desabilitada
- Healthcheck: HTTP 200, status UP

## 1. Criacao de territorio prioritario

**Endpoint:** `POST /api/v1/territories`

### Recebido
```json
{
    "code":  "E2E-APS-001",
    "name":  "Territorio E2E Gestao Ativa",
    "unitName":  "UBS E2E",
    "linkedPopulationPercent":  42,
    "dataCompetence":  "2026-06",
    "indicators":  [
                       {
                           "focus":  "CHRONIC_CONDITIONS",
                           "score":  32,
                           "target":  60
                       },
                       {
                           "focus":  "PRENATAL_CARE",
                           "score":  72,
                           "target":  85
                       }
                   ]
}
```

### Processado
- CreateTerritoryUseCase valida o codigo e persiste o territorio no PostgreSQL.
- PriorityCalculator compara vinculo 42% com a meta de 50%.
- Os indicadores de condicoes cronicas (32%/60%) e prenatal (72%/85%) tambem ficam abaixo da meta.
- A combinacao dos dois sinais resulta em prioridade HIGH com motivos explicitos.

### Output
HTTP 201
```json
{
    "id":  "9b6adb56-8d69-4cb2-9f1f-440edca56726",
    "code":  "E2E-APS-001",
    "name":  "Territorio E2E Gestao Ativa",
    "unitName":  "UBS E2E",
    "linkedPopulationPercent":  42.00,
    "dataCompetence":  "2026-06",
    "priority":  {
                     "level":  "HIGH",
                     "linkageTarget":  50.00,
                     "reasons":  [
                                     "Linked population 42.00% is below the configured target of 50.00%",
                                     "Condicoes cronicas is 32.00% against target 60.00%",
                                     "Acompanhamento prenatal is 72.00% against target 85.00%"
                                 ]
                 },
    "indicators":  [
                       {
                           "focus":  "CHRONIC_CONDITIONS",
                           "label":  "Condicoes cronicas",
                           "score":  32.00,
                           "target":  60.00,
                           "belowTarget":  true
                       },
                       {
                           "focus":  "PRENATAL_CARE",
                           "label":  "Acompanhamento prenatal",
                           "score":  72.00,
                           "target":  85.00,
                           "belowTarget":  true
                       }
                   ],
    "actions":  [

                ]
}
```

### Assercoes
- PASS: HTTP 201 Created.
- PASS: priority.level = HIGH.
- PASS: priority.reasons contem os tres sinais esperados.

## 2. Criacao de acao territorial

**Endpoint:** `POST /api/v1/territories/{territoryId}/actions`

### Recebido
```json
{
    "focus":  "CHRONIC_CONDITIONS",
    "objective":  "Reconnect people with chronic conditions to preventive follow-up",
    "responsibleTeam":  "ESF E2E",
    "plannedStart":  "2026-07-18",
    "plannedEnd":  "2026-07-25",
    "targetCount":  80,
    "notes":  "E2E aggregate demonstration. No patient records."
}
```

### Processado
- CreateSearchActionUseCase confirma que o territorio existe.
- SearchAction cria a acao com status PLANNED e performedCount igual a zero.
- O adapter JPA persiste meta, prazo, equipe e foco no PostgreSQL.

### Output
HTTP 201
```json
{
    "id":  "62d69975-19c3-4af0-96e5-268110259e90",
    "territoryId":  "9b6adb56-8d69-4cb2-9f1f-440edca56726",
    "focus":  "CHRONIC_CONDITIONS",
    "focusLabel":  "Condicoes cronicas",
    "objective":  "Reconnect people with chronic conditions to preventive follow-up",
    "responsibleTeam":  "ESF E2E",
    "plannedStart":  "2026-07-18",
    "plannedEnd":  "2026-07-25",
    "targetCount":  80,
    "performedCount":  0,
    "progressPercent":  0.00,
    "status":  "PLANNED",
    "notes":  "E2E aggregate demonstration. No patient records.",
    "resultNotes":  null,
    "createdAt":  "2026-07-19T00:54:30.269759191",
    "updatedAt":  "2026-07-19T00:54:30.269759191"
}
```

### Assercoes
- PASS: HTTP 201 Created.
- PASS: status = PLANNED.
- PASS: performedCount = 0.

## 3. Registro de progresso agregado

**Endpoint:** `PATCH /api/v1/actions/{actionId}/progress`

### Recebido
```json
{
    "status":  "IN_PROGRESS",
    "performedCount":  54,
    "resultNotes":  "54 aggregate contacts completed."
}
```

### Processado
- UpdateSearchActionProgressUseCase recupera a acao persistida.
- SearchAction aceita 54 contatos agregados e altera o status para IN_PROGRESS.
- O output calcula progressPercent como 54 / 80, equivalente a 67,50%.

### Output
HTTP 200
```json
{
    "id":  "62d69975-19c3-4af0-96e5-268110259e90",
    "territoryId":  "9b6adb56-8d69-4cb2-9f1f-440edca56726",
    "focus":  "CHRONIC_CONDITIONS",
    "focusLabel":  "Condicoes cronicas",
    "objective":  "Reconnect people with chronic conditions to preventive follow-up",
    "responsibleTeam":  "ESF E2E",
    "plannedStart":  "2026-07-18",
    "plannedEnd":  "2026-07-25",
    "targetCount":  80,
    "performedCount":  54,
    "progressPercent":  67.50,
    "status":  "IN_PROGRESS",
    "notes":  "E2E aggregate demonstration. No patient records.",
    "resultNotes":  "54 aggregate contacts completed.",
    "createdAt":  "2026-07-19T00:54:30.269759",
    "updatedAt":  "2026-07-19T00:54:30.301028181"
}
```

### Assercoes
- PASS: HTTP 200 OK.
- PASS: status = IN_PROGRESS.
- PASS: progressPercent = 67.50.

## 4. Bloqueio de conclusao invalida

**Endpoint:** `PATCH /api/v1/actions/{actionId}/progress`

### Recebido
```json
{
    "status":  "COMPLETED",
    "performedCount":  0,
    "resultNotes":  ""
}
```

### Processado
- SearchAction rejeita COMPLETED quando performedCount e zero.
- ApsExceptionHandler traduz a regra de dominio para RFC 9457 com status 422.
- A acao existente nao e alterada pela requisicao invalida.

### Output
HTTP 422
```json
{
    "type":  "https://api.example.com/problems/aps-prioritization/validation-error",
    "title":  "Invalid APS prioritization data",
    "status":  422,
    "detail":  "A completed action must have a performed count",
    "instance":  "/api/v1/actions/62d69975-19c3-4af0-96e5-268110259e90/progress"
}
```

### Assercoes
- PASS: HTTP 422 Unprocessable Entity.
- PASS: O problema retornado informa erro de validacao.

## 5. Leitura persistida do territorio

**Endpoint:** `GET /api/v1/territories/{territoryId}`

### Recebido
```json
{

}
```

### Processado
- GetTerritoryDetailsUseCase le territorio, indicadores e acoes do PostgreSQL.
- A prioridade e recalculada em tempo de consulta; ela nao e uma coluna persistida.
- A acao permanece IN_PROGRESS com 54 contatos porque a conclusao invalida foi recusada.

### Output
HTTP 200
```json
{
    "id":  "9b6adb56-8d69-4cb2-9f1f-440edca56726",
    "code":  "E2E-APS-001",
    "name":  "Territorio E2E Gestao Ativa",
    "unitName":  "UBS E2E",
    "linkedPopulationPercent":  42.00,
    "dataCompetence":  "2026-06",
    "priority":  {
                     "level":  "HIGH",
                     "linkageTarget":  50.00,
                     "reasons":  [
                                     "Linked population 42.00% is below the configured target of 50.00%",
                                     "Condicoes cronicas is 32.00% against target 60.00%",
                                     "Acompanhamento prenatal is 72.00% against target 85.00%"
                                 ]
                 },
    "indicators":  [
                       {
                           "focus":  "CHRONIC_CONDITIONS",
                           "label":  "Condicoes cronicas",
                           "score":  32.00,
                           "target":  60.00,
                           "belowTarget":  true
                       },
                       {
                           "focus":  "PRENATAL_CARE",
                           "label":  "Acompanhamento prenatal",
                           "score":  72.00,
                           "target":  85.00,
                           "belowTarget":  true
                       }
                   ],
    "actions":  [
                    {
                        "id":  "62d69975-19c3-4af0-96e5-268110259e90",
                        "territoryId":  "9b6adb56-8d69-4cb2-9f1f-440edca56726",
                        "focus":  "CHRONIC_CONDITIONS",
                        "focusLabel":  "Condicoes cronicas",
                        "objective":  "Reconnect people with chronic conditions to preventive follow-up",
                        "responsibleTeam":  "ESF E2E",
                        "plannedStart":  "2026-07-18",
                        "plannedEnd":  "2026-07-25",
                        "targetCount":  80,
                        "performedCount":  54,
                        "progressPercent":  67.50,
                        "status":  "IN_PROGRESS",
                        "notes":  "E2E aggregate demonstration. No patient records.",
                        "resultNotes":  "54 aggregate contacts completed.",
                        "createdAt":  "2026-07-19T00:54:30.269759",
                        "updatedAt":  "2026-07-19T00:54:30.301028"
                    }
                ]
}
```

### Assercoes
- PASS: HTTP 200 OK.
- PASS: A acao criada esta presente no retorno.
- PASS: performedCount = 54 e status = IN_PROGRESS apos a tentativa invalida.

## 6. Painel apos o ciclo operacional

**Endpoint:** `GET /api/v1/dashboard`

### Recebido
```json
{

}
```

### Processado
- GetDashboardUseCase consolida prioridades e acoes abertas a partir do PostgreSQL.
- O painel identifica um territorio HIGH e uma acao ainda aberta.

### Output
HTTP 200
```json
{
    "periodStart":  "2026-07-01",
    "periodEnd":  "2026-07-19",
    "highPriorityTerritoryCount":  1,
    "openActionCount":  1,
    "completedActionCount":  0,
    "topPriorities":  [
                          {
                              "id":  "9b6adb56-8d69-4cb2-9f1f-440edca56726",
                              "code":  "E2E-APS-001",
                              "name":  "Territorio E2E Gestao Ativa",
                              "unitName":  "UBS E2E",
                              "linkedPopulationPercent":  42.00,
                              "dataCompetence":  "2026-06",
                              "priority":  "HIGH",
                              "attentionFocus":  "CHRONIC_CONDITIONS",
                              "attentionFocusLabel":  "Condicoes cronicas",
                              "openActionCount":  1
                          }
                      ],
    "attentionActions":  [
                             {
                                 "actionId":  "62d69975-19c3-4af0-96e5-268110259e90",
                                 "territoryId":  "9b6adb56-8d69-4cb2-9f1f-440edca56726",
                                 "territoryName":  "Territorio E2E Gestao Ativa",
                                 "plannedEnd":  "2026-07-25",
                                 "reason":  "DUE_SOON"
                             }
                         ]
}
```

### Assercoes
- PASS: HTTP 200 OK.
- PASS: highPriorityTerritoryCount = 1.
- PASS: openActionCount = 1.

## Logs tecnicos do container

Os logs abaixo comprovam inicializacao e migracao. A trilha de negocio acima e produzida pelo executor HTTP, pois o dominio nao registra dados operacionais sensiveis em logs.

```text
aps-e2e-service-1  | 2026-07-19T00:54:26.724625918Z 2026-07-19T00:54:26.724Z  INFO 1 --- [aps-prioritization-service] [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.6.13.Final
aps-e2e-service-1  | 2026-07-19T00:54:26.742435424Z 2026-07-19T00:54:26.742Z  INFO 1 --- [aps-prioritization-service] [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
aps-e2e-service-1  | 2026-07-19T00:54:26.896594586Z 2026-07-19T00:54:26.896Z  INFO 1 --- [aps-prioritization-service] [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
aps-e2e-service-1  | 2026-07-19T00:54:26.932024868Z 2026-07-19T00:54:26.931Z  WARN 1 --- [aps-prioritization-service] [           main] org.hibernate.orm.deprecation            : HHH90000025: PostgreSQLDialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
aps-e2e-service-1  | 2026-07-19T00:54:26.940409481Z 2026-07-19T00:54:26.940Z  INFO 1 --- [aps-prioritization-service] [           main] org.hibernate.orm.connections.pooling    : HHH10001005: Database info:
aps-e2e-service-1  | 2026-07-19T00:54:26.940430583Z 	Database JDBC URL [Connecting through datasource 'HikariDataSource (HikariPool-1)']
aps-e2e-service-1  | 2026-07-19T00:54:26.940433303Z 	Database driver: undefined/unknown
aps-e2e-service-1  | 2026-07-19T00:54:26.940435163Z 	Database version: 15.18
aps-e2e-service-1  | 2026-07-19T00:54:26.940436843Z 	Autocommit mode: undefined/unknown
aps-e2e-service-1  | 2026-07-19T00:54:26.940438513Z 	Isolation level: undefined/unknown
aps-e2e-service-1  | 2026-07-19T00:54:26.940440343Z 	Minimum pool size: undefined/unknown
aps-e2e-service-1  | 2026-07-19T00:54:26.940454044Z 	Maximum pool size: undefined/unknown
aps-e2e-service-1  | 2026-07-19T00:54:27.575435792Z 2026-07-19T00:54:27.574Z  INFO 1 --- [aps-prioritization-service] [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
aps-e2e-service-1  | 2026-07-19T00:54:27.602991910Z 2026-07-19T00:54:27.602Z  INFO 1 --- [aps-prioritization-service] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
aps-e2e-service-1  | 2026-07-19T00:54:28.521955726Z 2026-07-19T00:54:28.521Z  INFO 1 --- [aps-prioritization-service] [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 3 endpoints beneath base path '/actuator'
aps-e2e-service-1  | 2026-07-19T00:54:28.574703465Z 2026-07-19T00:54:28.574Z  INFO 1 --- [aps-prioritization-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8205 (http) with context path '/'
aps-e2e-service-1  | 2026-07-19T00:54:28.586726074Z 2026-07-19T00:54:28.586Z  INFO 1 --- [aps-prioritization-service] [           main] .s.a.ApsPrioritizationServiceApplication : Started ApsPrioritizationServiceApplication in 4.467 seconds (process running for 4.858)
aps-e2e-service-1  | 2026-07-19T00:54:29.884183034Z 2026-07-19T00:54:29.883Z  INFO 1 --- [aps-prioritization-service] [nio-8205-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
aps-e2e-service-1  | 2026-07-19T00:54:29.884430578Z 2026-07-19T00:54:29.884Z  INFO 1 --- [aps-prioritization-service] [nio-8205-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
aps-e2e-service-1  | 2026-07-19T00:54:29.885469348Z 2026-07-19T00:54:29.885Z  INFO 1 --- [aps-prioritization-service] [nio-8205-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
```

## Resultado

PASS: o fluxo E2E executou contra a aplicacao Docker e PostgreSQL dedicados.
O relatorio mostra, para cada etapa, o que a API recebeu, a regra/caso de uso aplicado e o output efetivamente retornado.
