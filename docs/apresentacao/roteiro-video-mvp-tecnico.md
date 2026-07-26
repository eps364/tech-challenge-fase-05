# Roteiro do vídeo técnico do MVP - SUS Conecta

Duração-alvo: **7 minutos e 35 segundos**.

Objetivo: demonstrar o backend funcionando, explicar as classes centrais e
mostrar como dados agregados sustentaram a escolha do problema, sem confundir
a análise exploratória com a massa fictícia da API.

Ferramentas de gravação recomendadas:

- IDE para mostrar estrutura e classes;
- relatório HTML ou Markdown da análise;
- Bruno com a collection `docs/tecnico/api/`;
- terminal apenas para healthcheck e, se desejado, evidências de teste;
- tela em 1920 x 1080, com notificações, abas pessoais e barra do Windows
  ocultas.

## Mensagem que deve orientar todo o vídeo

> O SUS Conecta recebe indicadores agregados por território, aplica uma regra
> operacional explicável e ajuda a coordenação a transformar prioridade em uma
> ação de busca ativa acompanhada de forma agregada.

Não dizer que o sistema diagnostica, identifica pacientes, prediz agravamento,
evita internação ou comprova impacto clínico.

## Preparação antes de gravar

### 1. Ambiente

Subir o serviço:

```powershell
docker compose up -d --build aps-prioritization-service
curl.exe http://localhost:8205/actuator/health
```

Resposta esperada:

```json
{"status":"UP"}
```

Para obter a mesma massa e os mesmos contadores descritos neste roteiro, o
banco demonstrativo deve estar limpo. O comando abaixo remove o volume local
da demonstração; use-o somente se não houver dados que precisem ser
preservados:

```powershell
docker compose down -v
docker compose up -d --build aps-prioritization-service
```

### 2. Bruno

1. Abrir como collection a pasta `docs/tecnico/api/`.
2. Selecionar o ambiente `aps-local`.
3. Atualizar `apsDemoStartDate` para o dia da gravação.
4. Atualizar `apsDemoEndDate` para sete dias depois.
5. Confirmar que `apsCreatedActionId` será preenchido automaticamente pelo
   request 05.
6. Executar a sequência uma vez antes da gravação.
7. Limpar novamente a massa antes do take final.

### 3. Abas e arquivos já preparados

Abrir nesta ordem:

1. `analytics/reports/analise_aps_sus.html`;
2. árvore `core` e `infra` do serviço;
3. `PriorityCalculator.java`;
4. `SearchAction.java`;
5. `ApsPrioritizationController.java`;
6. `UseCaseConfig.java`;
7. Bruno, com os requests 01 a 07 visíveis;
8. `docs/tecnico/e2e/relatorio_execucao_e2e.md`.

Não navegar procurando arquivos durante a gravação. Deixar cada ponto pronto
reduz silêncio e risco de ultrapassar o limite.

## Mapa técnico para explicar

```mermaid
flowchart LR
  HTTP["ApsPrioritizationController\nrequests e validação HTTP"] --> UC["Casos de uso\norquestração"]
  UC --> D["Domínio\nTerritory, PriorityCalculator e SearchAction"]
  UC --> G["Gateways\nportas de persistência"]
  G --> A["Adapters JPA\nmapeamento domínio-banco"]
  A --> DB["PostgreSQL 15\nschema criado por Flyway"]
  D --> UC
  UC --> HTTP
```

As dependências apontam para o `core`. Spring, JPA, Jakarta Validation,
ProblemDetail e Flyway permanecem em `infra`.

## Classes principais e o que dizer

| Classe ou grupo | Papel no fluxo | Ponto para mostrar |
| --- | --- | --- |
| `ApsPrioritizationController` | Traduz HTTP em comandos do core e devolve os outputs. | Endpoints `/dashboard`, `/territories`, `/actions`. |
| `PriorityCalculator` | Aplica a regra `HIGH`, `MEDIUM` ou `LOW` e gera os motivos. | Método `assess`. |
| `Territory` | Protege invariantes dos dados territoriais agregados. | Percentuais de 0 a 100, competência obrigatória e foco sem duplicidade. |
| `SearchAction` | Controla status, progresso, prazo e regra de conclusão. | `updateProgress`, `progressPercent`, `isOverdue` e `isDueSoon`. |
| Casos de uso | Orquestram leitura, classificação, criação e atualização. | `GetDashboardUseCase`, `GetTerritoryDetailsUseCase`, `CreateSearchActionUseCase` e `UpdateSearchActionProgressUseCase`. |
| Gateways e adapters | Mantém o core independente do banco. | `TerritoryGateway`/`SearchActionGateway` e adapters JPA. |
| `UseCaseConfig` | Faz a composição das dependências no limite de infraestrutura. | Meta de vínculo configurável e injeção dos gateways. |
| `ApsExceptionHandler` | Converte erros conhecidos em `ProblemDetail`. | Validação de domínio retorna HTTP `422`. |

Não é necessário abrir todas essas classes. Para caber no tempo, mostrar a
árvore e abrir somente `PriorityCalculator`, `SearchAction` e
`ApsPrioritizationController`.

## Roteiro cronometrado

### 0:00-0:25 - Abertura técnica

**Na tela:** título do projeto e estrutura raiz do repositório.

**Fala:**

> Este é o SUS Conecta, um backend em Java 21 e Spring Boot 3.4.5 para apoiar
> a priorização territorial de busca ativa na Atenção Primária. Nesta
> demonstração eu vou mostrar de onde veio a oportunidade, como organizamos a
> arquitetura e, principalmente, o fluxo real da API: identificar um
> território, explicar a prioridade, criar uma ação e registrar seu progresso.

### 0:25-1:00 - Dados e análise

**Na tela:** topo de `analytics/reports/analise_aps_sus.html`, com os indicadores
principais.

**Fala:**

> A escolha do problema foi apoiada por dados públicos agregados. Usamos
> IBGE 2025 e bases agregadas do SISAB. O pipeline Python organizou as fontes
> por município e calculou proporções. Observamos vínculo aproximado nacional
> de 38,11%, 1.091 municípios com mais de 20 mil habitantes abaixo de 50% de
> vínculo e 276 com média de indicadores abaixo de 40. Esses resultados
> sustentam uma oportunidade de investigação territorial; não provam qualidade
> clínica nem causalidade.

**Apontar rapidamente:**

- `analytics/scripts/analisar_aps_sus.py`;
- `data/raw` como preservação das fontes;
- `data/processed` como saídas tabulares;
- `analytics/reports` como resultado reproduzível.

**Frase de transição:**

> A análise escolheu a oportunidade. A demonstração da API usa territórios
> fictícios e agregados para não expor pessoas.

### 1:00-1:50 - Arquitetura e classes centrais

**Na tela:** árvore `core`/`infra`, seguida de `PriorityCalculator` e
`SearchAction`.

**Fala:**

> O serviço segue Clean Architecture. No core ficam domínio, casos de uso,
> DTOs e interfaces de gateway, todos em Java puro. O controller, Spring, JPA,
> validação HTTP e tratamento de erros ficam em infraestrutura.
>
> A regra principal está em `PriorityCalculator`. Vínculo abaixo da meta e ao
> menos um indicador preventivo abaixo da própria meta resultam em prioridade
> alta. Apenas um desses sinais resulta em média; nenhum resulta em baixa. O
> retorno inclui os motivos, portanto não é uma caixa preta.
>
> `SearchAction` concentra a regra de execução: nasce como `PLANNED`, calcula o
> percentual realizado, identifica prazo e impede concluir uma ação com
> quantidade realizada igual a zero. Os casos de uso orquestram essas regras e
> acessam persistência por gateways, sem acoplar o core ao PostgreSQL.

### 1:50-2:10 - Infraestrutura e qualidade

**Na tela:** `UseCaseConfig`, migration `V1` e, por poucos segundos, o resumo de
testes.

**Fala:**

> `UseCaseConfig` monta os casos de uso e injeta a meta de vínculo configurável.
> Os adapters traduzem domínio e entidades JPA. O PostgreSQL 15 é criado por
> Flyway, enquanto o Hibernate apenas valida o schema. A suíte atual possui 18
> testes sem falhas e cobertura de linhas de 98,94%.

### 2:10-2:25 - API no ar

**Na tela:** request 01 do Bruno.

**Executar:** `01 - Health | servico no ar`.

**Fala:**

> A aplicação está em Docker, na porta 8205. O healthcheck retorna HTTP 200 e
> status `UP`.

**Apontar:** `status = UP`.

### 2:25-2:55 - Dashboard inicial

**Executar:** `02 - Dashboard inicial | fila territorial`.

**Fala:**

> O primeiro endpoint resume a decisão operacional. Com a massa limpa, existe
> um território em alta prioridade, duas ações abertas e uma concluída no
> período. O dashboard também devolve as cinco maiores prioridades e ações
> vencidas ou próximas do prazo.

**Apontar:**

- `highPriorityTerritoryCount = 1`;
- `openActionCount = 2`;
- `completedActionCount = 1`;
- `topPriorities`;
- `attentionActions`.

### 2:55-3:20 - Escolha do território

**Executar:** `03 - Prioridades HIGH | escolher territorio`.

**Fala:**

> A listagem aceita filtros e já vem ordenada. Filtrando por `HIGH`, Jardim
> Esperança aparece como primeiro território, com foco de atenção em
> `CHRONIC_CONDITIONS`. A unidade de decisão é território ou UBS, nunca uma
> pessoa.

**Apontar:**

- `name = Jardim Esperanca`;
- `priority = HIGH`;
- `attentionFocus = CHRONIC_CONDITIONS`;
- `openActionCount`.

### 3:20-4:05 - Explicação da prioridade

**Executar:** `04 - Detalhe Jardim Esperanca | explicar regra`.

**Fala:**

> O detalhe comprova a explicabilidade. Jardim Esperança possui 42% de
> população vinculada para uma meta configurada de 50%. Condições crônicas
> está em 32% para meta de 60%, e pré-natal em 72% para meta de 85%. O objeto
> `priority` devolve o nível, a meta usada e as razões textuais. A competência
> dos dados também acompanha o território. O sistema apoia a coordenação a
> investigar e organizar trabalho; ele não classifica risco clínico.

**Apontar:**

- `linkedPopulationPercent = 42.00`;
- `dataCompetence`;
- `priority.level = HIGH`;
- `priority.linkageTarget = 50.00`;
- `priority.reasons`;
- `indicators`.

### 4:05-5:05 - Criação de uma ação

**Na tela:** request 05, mostrando o payload antes de executar.

**Executar:** `05 - Criar acao territorial | prioridade vira trabalho`.

**Fala antes da chamada:**

> Agora a prioridade vira uma ação territorial. O payload informa foco
> preventivo, objetivo, equipe responsável, período e uma meta agregada de 80
> contatos. Não existe nome, CPF, endereço, prontuário ou diagnóstico.

**Fala depois da chamada:**

> A API retorna HTTP 201. A ação nasce como `PLANNED`, com zero realizado e
> zero por cento de progresso. O ID retornado é capturado automaticamente pelo
> Bruno para atualizar exatamente esta ação no próximo passo.

**Apontar:**

- HTTP `201`;
- `id`;
- `status = PLANNED`;
- `targetCount = 80`;
- `performedCount = 0`;
- `progressPercent = 0.00`.

### 5:05-5:45 - Atualização do progresso

**Executar:** `06 - Atualizar progresso | execucao agregada`.

**Fala:**

> A equipe registra 54 contatos agregados e muda a situação para
> `IN_PROGRESS`. O domínio recalcula 54 dividido por 80 e devolve 67,50%. Esse
> número mede execução operacional. Ele não informa quem foi contatado e não
> comprova melhora clínica.

**Apontar:**

- HTTP `200`;
- `status = IN_PROGRESS`;
- `performedCount = 54`;
- `progressPercent = 67.50`;
- `resultNotes`.

### 5:45-6:10 - Fechamento do ciclo no dashboard

**Executar:** `07 - Dashboard apos progresso | fechar ciclo`.

**Fala:**

> Voltando ao dashboard, a nova ação permanece aberta e o contador passa de
> duas para três. A coordenação continua vendo as prioridades e os alertas de
> prazo. Assim o fluxo fecha no mesmo painel em que a decisão começou.

**Apontar:**

- `openActionCount = 3`;
- `topPriorities`;
- `attentionActions`.

### 6:10-6:40 - Regra de erro e evidência E2E

**Na tela:** seção "Bloqueio de conclusão inválida" do relatório E2E.

**Fala:**

> Além do caminho feliz, o fluxo E2E envia uma tentativa de concluir uma ação
> com zero realizado. `SearchAction` rejeita a transição e o
> `ApsExceptionHandler` converte a regra em `ProblemDetail`, com HTTP 422. O
> mesmo relatório comprova que a tentativa inválida não corrompeu o estado
> persistido no PostgreSQL.

**Apontar:**

- payload com `status = COMPLETED` e `performedCount = 0`;
- HTTP `422`;
- asserções `PASS`;
- leitura posterior ainda com `IN_PROGRESS` e 54.

### 6:40-7:10 - O que está entregue e o que não está

**Na tela:** README ou checklist final.

**Fala:**

> O MVP entregue cobre o fluxo principal por API, possui documentação
> Swagger, collections Bruno e Insomnia, testes unitários, integração HTTP e
> E2E com PostgreSQL. A massa demonstrativa é fictícia e o funcionamento não
> depende de uma fonte externa em tempo real. Ainda não entregamos front-end,
> integração com sistemas locais nem medição de impacto assistencial. Esses
> pontos dependem de validação e governança com uma rede parceira.

### 7:10-7:35 - Encerramento

**Na tela:** dashboard ou diagrama do fluxo.

**Fala:**

> Tecnicamente, o SUS Conecta mantém a regra no domínio, a orquestração nos
> casos de uso e os frameworks na infraestrutura. Na prática, ele transforma
> um sinal territorial agregado em uma prioridade explicável, uma ação
> executável e um acompanhamento visível. A decisão final continua com a
> coordenação e a equipe de saúde.

Finalizar a gravação imediatamente. Não improvisar um segundo encerramento.

## Chamadas equivalentes em HTTP

O Bruno é mais seguro para a gravação porque captura o ID criado. As chamadas
abaixo servem como referência técnica.

### Health

```http
GET http://localhost:8205/actuator/health
```

### Dashboard

```http
GET http://localhost:8205/api/v1/dashboard
```

### Territórios de alta prioridade

```http
GET http://localhost:8205/api/v1/territories?priority=HIGH
```

### Detalhe de Jardim Esperança

```http
GET http://localhost:8205/api/v1/territories/10000000-0000-0000-0000-000000000001
```

### Criar ação

```http
POST http://localhost:8205/api/v1/territories/10000000-0000-0000-0000-000000000001/actions
Content-Type: application/json

{
  "focus": "CHRONIC_CONDITIONS",
  "objective": "Organizar busca ativa territorial para acompanhamento preventivo de condições crônicas",
  "responsibleTeam": "ESF Jardim Esperança",
  "plannedStart": "AJUSTAR_PARA_DATA_DA_GRAVACAO",
  "plannedEnd": "AJUSTAR_PARA_SETE_DIAS_DEPOIS",
  "targetCount": 80,
  "notes": "Massa demonstrativa com contagens agregadas. Não há dados de pacientes."
}
```

### Atualizar a ação criada

```http
PATCH http://localhost:8205/api/v1/actions/{id-retornado-no-post}/progress
Content-Type: application/json

{
  "status": "IN_PROGRESS",
  "performedCount": 54,
  "resultNotes": "54 contatos agregados registrados pela equipe no território."
}
```

### Demonstrar a validação de conclusão

```http
PATCH http://localhost:8205/api/v1/actions/{id-retornado-no-post}/progress
Content-Type: application/json

{
  "status": "COMPLETED",
  "performedCount": 0,
  "resultNotes": "Tentativa demonstrativa inválida."
}
```

Resposta esperada: HTTP `422` com `application/problem+json`.

## Plano de contingência

### Se o Docker não iniciar

Não gravar uma demonstração incompleta. Corrigir o ambiente antes do take. Ter
como evidência secundária o relatório E2E, mas o vídeo deve mostrar a API
respondendo.

### Se a massa estiver alterada

Parar, limpar apenas o volume demonstrativo com consciência de que os dados
locais serão apagados, subir novamente e repetir a sequência.

### Se o request 06 não encontrar o ID

Confirmar que o request 05 retornou HTTP 201 e que `apsCreatedActionId` foi
preenchido no ambiente Bruno. Não editar um UUID durante a gravação.

### Se o tempo ultrapassar 7:35 no ensaio

Cortar explicações, nesta ordem:

1. detalhes de adapters e `UseCaseConfig`;
2. nomes de todas as fontes, mantendo apenas IBGE e SISAB;
3. leitura de campos secundários do dashboard.

Não cortar a criação da ação, a atualização do progresso nem o retorno ao
dashboard, pois esses passos demonstram a funcionalidade principal.

## Checklist do take final

- Duração total menor que 8 minutos.
- Healthcheck `UP`.
- Massa limpa e contadores previsíveis.
- Datas do request 05 atualizadas.
- ID criado capturado automaticamente.
- Nenhuma notificação, aba pessoal ou credencial visível.
- Código com zoom suficiente para leitura.
- Dados sempre descritos como agregados ou fictícios.
- Regra `HIGH` explicada sem chamar de risco clínico.
- Progresso descrito como execução, não impacto.
- Limitações declaradas.
- Áudio ouvido do início ao fim antes do upload.
- Link testado em janela anônima depois da publicação.
