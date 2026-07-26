# Roteiro da apresentação técnica — SUS-Connect APS

Tempo estimado: **aproximadamente 7 minutos e 30 segundos**.

## Preparação antes da gravação

### Arquivos Java abertos na IDE

Deixe estas classes abertas, nesta ordem:

1. `ApsPrioritizationController.java`
2. `GetDashboardUseCase.java`
3. `ListTerritoriesUseCase.java`
4. `GetTerritoryDetailsUseCase.java`
5. `PriorityCalculator.java`
6. `CreateSearchActionUseCase.java`
7. `SearchAction.java`
8. `UpdateSearchActionProgressUseCase.java`
9. `SearchActionGateway.java`
10. `SearchActionRepositoryAdapter.java`
11. `SearchActionJpaRepository.java`

Arquivos completos:

- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/infra/web/controller/ApsPrioritizationController.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/core/usecase/GetDashboardUseCase.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/core/usecase/ListTerritoriesUseCase.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/core/usecase/GetTerritoryDetailsUseCase.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/core/domain/PriorityCalculator.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/core/usecase/CreateSearchActionUseCase.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/core/domain/SearchAction.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/core/usecase/UpdateSearchActionProgressUseCase.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/core/gateway/SearchActionGateway.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/infra/gateway/SearchActionRepositoryAdapter.java`
- `aps-prioritization-service/src/main/java/br/com/fiap/susconnect/aps/infra/repository/SearchActionJpaRepository.java`

### Arquivos HTML abertos no navegador

Deixe somente estes dois HTMLs abertos:

1. `docs/apresentacao/apresentacao_aps.html` — visão geral do projeto e representação visual da solução.
2. `analytics/reports/analise_aps_sus.html` — dados e resultados da análise exploratória de APS.

Não é necessário abrir `analise_leitos_sus_2026.html` nem `sintese_oportunidades_integradas_sus.html`, porque eles não fazem parte do fluxo principal deste MVP.

---

## 1. Introdução e visão geral do projeto — 0:00 a 0:50

> Olá, eu sou o Luiz Saraiva e desenvolvi este projeto junto com a minha dupla, o Emerson Silva. Nesta apresentação, vou mostrar como organizamos tecnicamente a solução e como o fluxo funciona na prática por meio da API.
>
> Antes de entrar no código, vou explicar rapidamente o que construímos. O SUS-Connect APS é uma solução para apoiar a gestão da Atenção Primária. A ideia é ajudar o coordenador a identificar quais territórios precisam primeiro de uma ação de busca ativa preventiva e, principalmente, entender o motivo dessa prioridade.
>
> O sistema não trabalha com prontuários nem classifica pacientes. Ele analisa dados agregados de territórios e transforma esses indicadores em uma fila de prioridades explicável. Depois, essa prioridade pode virar uma ação planejada, acompanhada até a conclusão.

---

## 2. Dados e análises utilizados — 0:50 a 1:30

> A necessidade do projeto foi analisada a partir de dados públicos e agregados. Utilizamos estimativas populacionais do IBGE, dados de cadastros vinculados à Atenção Primária e indicadores preventivos publicados pelo SISAB e pelo Previne Brasil.
>
> Nesse recorte, encontramos uma vinculação nacional estimada em aproximadamente 38%. Também identificamos mais de mil municípios, com população acima de 20 mil habitantes, abaixo de 50% de vinculação, além de 276 municípios com média de indicadores preventivos inferior a 40%.
>
> Esses dados não servem para diagnosticar pessoas nem para afirmar que existe um problema clínico. Eles mostram uma oportunidade de organizar melhor o trabalho territorial. Na API, os exemplos são fictícios e agregados, criados apenas para demonstrar o funcionamento do produto com segurança.

---

## 3. Organização da arquitetura — 1:30 a 1:55

> A aplicação foi organizada com Clean Architecture. A requisição entra pelo controller, passa por um caso de uso e chega às regras de domínio. Quando é necessário consultar ou gravar informações, o núcleo utiliza uma interface de gateway, que é implementada pelos componentes de infraestrutura e pelos repositórios JPA.
>
> Com isso, a regra principal do produto fica isolada dos detalhes de framework e banco de dados.

---

## 4. Controller: entrada da API — 1:55 a 2:20

> O `ApsPrioritizationController` é a porta de entrada da API. Ele recebe os parâmetros e o corpo da requisição, transforma essas informações em comandos e encaminha cada operação para o caso de uso responsável.
>
> O ponto importante é que o controller não decide se um território tem prioridade alta e também não consulta o banco diretamente. Ele cuida apenas da comunicação HTTP. Até o tratamento de erros fica separado, em um componente que traduz os erros da aplicação para respostas padronizadas da API.

---

## 5. Casos de uso: fluxo da aplicação — 2:20 a 3:00

> Depois do controller, o fluxo chega aos casos de uso. Cada classe representa uma operação do sistema.
>
> O `GetDashboardUseCase` reúne os territórios e as ações para produzir uma visão operacional. O `ListTerritoriesUseCase` calcula as prioridades, aplica os filtros e ordena a fila. O `GetTerritoryDetailsUseCase` apresenta os indicadores, explica a classificação e recupera o histórico daquele território.
>
> Já o `CreateSearchActionUseCase` transforma a prioridade em uma ação planejada, enquanto o `UpdateSearchActionProgressUseCase` registra a evolução dessa ação. Os casos de uso coordenam o fluxo, mas deixam as regras de negócio no domínio e o acesso ao banco nos gateways.

---

## 6. Domínio: regras de prioridade e progresso — 3:00 a 3:40

> No domínio ficam as regras centrais do produto. O `PriorityCalculator` compara a vinculação e os indicadores preventivos com suas metas.
>
> Quando os dois grupos estão abaixo do esperado, a prioridade é alta. Quando apenas um deles está abaixo, ela é média. Quando todos atingem as metas, a prioridade é baixa. O cálculo também devolve os motivos, então o gestor consegue entender por que aquele território apareceu na fila.
>
> A entidade `SearchAction` representa o trabalho que será realizado. Ela controla o planejamento e o progresso agregado da ação, calcula o percentual executado e evita situações inconsistentes, como marcar uma ação como concluída sem registrar nenhuma execução.

---

## 7. Gateway, adapter e repository: persistência — 3:40 a 4:05

> Para acessar os dados, os casos de uso dependem de interfaces chamadas gateways. Neste fluxo, o `SearchActionGateway` define as operações de persistência que o núcleo precisa, sem conhecer JPA ou PostgreSQL.
>
> O `SearchActionRepositoryAdapter` implementa esse contrato e traduz os objetos do domínio para o formato de persistência. Por fim, o `SearchActionJpaRepository` executa as operações no banco. Dessa forma, a regra de negócio continua independente da tecnologia usada para armazenar os dados.

---

## 8. API: disponibilidade do serviço — 4:05 a 4:20

### `01 - Health | serviço no ar`

> O primeiro retorno confirma que o serviço está no ar e pronto para receber as requisições. Essa é uma verificação técnica do ambiente; a partir daqui começa o fluxo funcional do produto.

---

## 9. API: visão inicial do dashboard — 4:20 a 4:45

### `02 - Dashboard inicial | fila territorial`

> O dashboard apresenta uma visão agregada da operação. Ele mostra quantos territórios estão em cada nível de prioridade, quais aparecem no topo da fila e como estão as ações abertas ou concluídas.
>
> Essa visão funciona como ponto de partida para a decisão. Ela resume o cenário, mas os motivos de cada prioridade continuam disponíveis no detalhe do território.

---

## 10. API: fila de prioridades altas — 4:45 a 5:05

### `03 - Prioridades HIGH | escolher território`

> Aqui a aplicação transforma os indicadores em uma fila de trabalho. O filtro de prioridade alta mostra os territórios que apresentam, ao mesmo tempo, vinculação abaixo da meta e algum indicador preventivo também abaixo do esperado.
>
> Assim, o gestor não recebe apenas uma lista de números: ele recebe uma ordem inicial para investigar e planejar a atuação.

---

## 11. API: detalhe e explicação da prioridade — 5:05 a 5:35

### `04 - Detalhe Jardim Esperança | explicar regra`

> No detalhe conseguimos entender a classificação. O Jardim Esperança é um território fictício usado na demonstração. Ele tem 42% de vinculação para uma meta de 50%. Também apresenta indicadores preventivos abaixo das metas, como condições crônicas e acompanhamento pré-natal.
>
> Por isso, a prioridade é alta. O mais importante é que a API apresenta os valores, as metas e os motivos da decisão. A classificação é explicável e pode ser conferida pelo gestor.

---

## 12. API: criação da ação territorial — 5:35 a 6:05

### `05 - Criar ação territorial | prioridade vira trabalho`

> Depois de entender a prioridade, o sistema permite transformar essa análise em uma ação territorial. A ação define o foco preventivo, o período, a equipe responsável e uma meta agregada de execução.
>
> Ela é vinculada ao território, e não a uma lista de pacientes. Quando é criada, começa com o estado planejado. Isso representa a passagem da análise para uma atividade operacional concreta.

---

## 13. API: atualização do progresso — 6:05 a 6:35

### `06 - Atualizar progresso | execução agregada`

> Conforme a equipe executa a ação, o progresso pode ser atualizado. O sistema registra a quantidade realizada, o novo estado e uma observação geral do resultado.
>
> A própria entidade calcula o percentual de execução com base na meta e valida se a atualização é coerente. Continuamos trabalhando apenas com totais agregados, sem acompanhamento individual de pessoas.

---

## 14. API: fechamento do ciclo no dashboard — 6:35 a 7:00

### `07 - Dashboard após progresso | fechar ciclo`

> Ao consultar novamente o dashboard, a visão operacional já considera o progresso registrado. Com isso, fechamos o ciclo principal do MVP: observar os indicadores, priorizar o território, explicar a decisão, planejar uma ação e acompanhar sua execução.
>
> O produto acompanha o trabalho realizado. Ele não tenta afirmar, a partir dessa atualização, que houve uma melhora clínica ou que a ação causou uma mudança nos indicadores. Esse tipo de conclusão exigiria outro período e uma análise específica.

---

## 15. Encerramento técnico — 7:00 a 7:20

> Tecnicamente, a solução mantém as regras do negócio separadas da API e da persistência. A prioridade é calculada de forma explícita, os motivos ficam disponíveis para conferência e todo o fluxo trabalha com dados territoriais e agregados.
>
> Assim, o MVP demonstra como dados públicos podem apoiar uma decisão operacional e se transformar em uma ação acompanhável, preservando os limites éticos e o papel dos profissionais da Atenção Primária.
