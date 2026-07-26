# Roteiro da apresentação técnica — SUS-Connect APS

## Objetivo da apresentação

Este roteiro organiza a explicação técnica do projeto sem transformar a apresentação em um passo a passo de navegação. A proposta é explicar o significado de cada parte enquanto o código e as respostas da API são mostrados.

Tempo estimado: **aproximadamente 7 minutos e 30 segundos**.

---

## 1. Introdução e visão geral do projeto — 0:00 a 0:50

### Apresentação da dupla

> Olá, eu sou o Luiz Saraiva e desenvolvi este projeto junto com a minha dupla, o Emerson Silva. Nesta apresentação, vou mostrar como organizamos tecnicamente a solução e como o fluxo funciona na prática por meio da API.

### O que este tópico explica

O SUS-Connect APS é um MVP de apoio à gestão da Atenção Primária à Saúde. Ele ajuda a responder qual território deve receber primeiro uma ação de busca ativa preventiva e apresenta os motivos dessa prioridade.

O sistema não avalia pessoas individualmente e não realiza diagnóstico clínico. A unidade analisada é sempre um território ou uma UBS, utilizando indicadores agregados. A prioridade representa um sinal operacional para orientar o trabalho da equipe.

### Fala sugerida

> Antes de entrar no código, vou explicar rapidamente o que construímos. O SUS-Connect APS é uma solução para apoiar a gestão da Atenção Primária. A ideia é ajudar o coordenador a identificar quais territórios precisam primeiro de uma ação de busca ativa preventiva e, principalmente, entender o motivo dessa prioridade.
>
> O sistema não trabalha com prontuários nem classifica pacientes. Ele analisa dados agregados de territórios e transforma esses indicadores em uma fila de prioridades explicável. Depois, essa prioridade pode virar uma ação planejada, acompanhada até a conclusão.

---

## 2. Dados e análises utilizados — 0:50 a 1:30

### O que este tópico explica

A necessidade do produto foi estudada com dados públicos e agregados:

- estimativas populacionais do IBGE de 2025;
- dados de cadastros vinculados à Atenção Primária, disponibilizados pelo SISAB, com competência de dezembro de 2024;
- indicadores do SISAB e do Previne Brasil referentes ao terceiro quadrimestre de 2024;
- informações públicas sobre unidades e estrutura de atendimento, utilizadas como contexto territorial.

O cruzamento exploratório desses dados encontrou sinais de dificuldade de vinculação e de acompanhamento preventivo. A análise estimou uma vinculação nacional próxima de **38,11%**. Entre os municípios com mais de 20 mil habitantes, **1.091** apresentaram vinculação inferior a 50%, e **276** tiveram média dos indicadores preventivos abaixo de 40%.

Esses números ajudam a demonstrar uma oportunidade de priorização operacional. Eles não comprovam baixa qualidade clínica, não estabelecem relações de causa e efeito e não representam o resultado produzido pelo sistema.

Os territórios e as ações exibidos na demonstração da API são **fictícios e agregados**. Eles foram preparados para representar situações coerentes com os padrões observados nas bases públicas, sem utilizar nomes, CPF, endereços, prontuários ou qualquer dado individual de paciente.

### Fala sugerida

> A necessidade do projeto foi analisada a partir de dados públicos e agregados. Utilizamos estimativas populacionais do IBGE, dados de cadastros vinculados à Atenção Primária e indicadores preventivos publicados pelo SISAB e pelo Previne Brasil.
>
> Nesse recorte, encontramos uma vinculação nacional estimada em aproximadamente 38%. Também identificamos mais de mil municípios, com população acima de 20 mil habitantes, abaixo de 50% de vinculação, além de 276 municípios com média de indicadores preventivos inferior a 40%.
>
> Esses dados não servem para diagnosticar pessoas nem para afirmar que existe um problema clínico. Eles mostram uma oportunidade de organizar melhor o trabalho territorial. Na API, os exemplos são fictícios e agregados, criados apenas para demonstrar o funcionamento do produto com segurança.

---

## 3. Organização da arquitetura — 1:30 a 1:55

### O que este tópico explica

O serviço segue Clean Architecture para separar regras de negócio de detalhes técnicos. O fluxo principal é:

```text
Requisição HTTP
    ↓
Controller
    ↓
Caso de uso
    ↓
Domínio
    ↓
Gateway
    ↓
Adapter de persistência
    ↓
JpaRepository
    ↓
PostgreSQL
```

Essa separação permite que as regras de prioridade e acompanhamento existam no núcleo da aplicação sem depender de Spring, HTTP, JPA ou banco de dados.

### Fala sugerida

> A aplicação foi organizada com Clean Architecture. A requisição entra pelo controller, passa por um caso de uso e chega às regras de domínio. Quando é necessário consultar ou gravar informações, o núcleo utiliza uma interface de gateway, que é implementada pelos componentes de infraestrutura e pelos repositórios JPA.
>
> Com isso, a regra principal do produto fica isolada dos detalhes de framework e banco de dados.

---

## 4. Controller: entrada da API — 1:55 a 2:20

### Classe principal

`ApsPrioritizationController`

### O que este tópico explica

O controller representa a camada de entrada HTTP. Ele:

- recebe as requisições do dashboard, dos territórios e das ações;
- valida e converte os dados recebidos em comandos da aplicação;
- chama o caso de uso correspondente;
- converte o resultado para a resposta da API.

Ele não calcula a prioridade e não acessa o banco diretamente. Sua responsabilidade é adaptar a comunicação HTTP para o formato entendido pelo núcleo da aplicação.

Os erros de domínio também permanecem separados do controller. O `ApsExceptionHandler` converte essas exceções em respostas HTTP padronizadas com `ProblemDetail`.

### Fala sugerida

> O `ApsPrioritizationController` é a porta de entrada da API. Ele recebe os parâmetros e o corpo da requisição, transforma essas informações em comandos e encaminha cada operação para o caso de uso responsável.
>
> O ponto importante é que o controller não decide se um território tem prioridade alta e também não consulta o banco diretamente. Ele cuida apenas da comunicação HTTP. Até o tratamento de erros fica separado, em um componente que traduz os erros da aplicação para respostas padronizadas da API.

---

## 5. Casos de uso: fluxo da aplicação — 2:20 a 3:00

### Classes principais

| Caso de uso | Responsabilidade |
|---|---|
| `GetDashboardUseCase` | Reúne territórios e ações para montar os totais, alertas e principais prioridades do dashboard. |
| `ListTerritoriesUseCase` | Calcula a prioridade dos territórios, aplica filtros e ordena a fila. |
| `GetTerritoryDetailsUseCase` | Retorna os indicadores, os motivos da classificação e o histórico de ações de um território. |
| `CreateSearchActionUseCase` | Verifica o território e cria uma ação de busca ativa planejada. |
| `UpdateSearchActionProgressUseCase` | Localiza uma ação existente e aplica a atualização de seu progresso. |

### O que este tópico explica

Os casos de uso representam as operações que o sistema oferece. Eles coordenam as informações necessárias, acionam as regras de domínio e utilizam os gateways quando precisam ler ou persistir dados.

Essa camada não conhece detalhes de rota HTTP e também não implementa consultas JPA. Ela descreve o comportamento da aplicação em termos do negócio.

### Fala sugerida

> Depois do controller, o fluxo chega aos casos de uso. Cada classe representa uma operação do sistema.
>
> O caso de uso do dashboard reúne territórios e ações para produzir uma visão operacional. O de listagem classifica, filtra e ordena os territórios. O de detalhes explica os indicadores e recupera o histórico daquele território. Já os casos de uso de ações criam o planejamento da busca ativa e atualizam seu progresso.
>
> Os casos de uso fazem a coordenação do fluxo, mas deixam as regras de negócio no domínio e o acesso ao banco nos gateways.

---

## 6. Domínio: regras de prioridade e progresso — 3:00 a 3:40

### Classes principais

`PriorityCalculator` e `SearchAction`

### O que este tópico explica

O `PriorityCalculator` compara dois tipos de sinal:

1. o percentual de população vinculada com a meta de vinculação;
2. os indicadores preventivos observados com suas respectivas metas.

A classificação segue uma regra explícita:

- **HIGH:** vinculação abaixo da meta e pelo menos um indicador preventivo abaixo da meta;
- **MEDIUM:** apenas um desses grupos apresenta insuficiência;
- **LOW:** vinculação e indicadores atingem as metas.

Além do nível, o cálculo produz os motivos da classificação. Por isso, a prioridade não aparece como uma pontuação sem explicação.

A entidade `SearchAction` representa a execução operacional. Uma nova ação começa como `PLANNED` e pode receber quantidade realizada, estado e observações de resultado. O domínio também calcula seu percentual de progresso e impede estados incoerentes, como concluir uma ação sem nenhuma execução registrada.

### Fala sugerida

> No domínio ficam as regras centrais do produto. O `PriorityCalculator` compara a vinculação e os indicadores preventivos com suas metas.
>
> Quando os dois grupos estão abaixo do esperado, a prioridade é alta. Quando apenas um deles está abaixo, ela é média. Quando todos atingem as metas, a prioridade é baixa. O cálculo também devolve os motivos, então o gestor consegue entender por que aquele território apareceu na fila.
>
> A entidade `SearchAction` representa o trabalho que será realizado. Ela controla o planejamento e o progresso agregado da ação, calcula o percentual executado e evita situações inconsistentes, como marcar uma ação como concluída sem registrar nenhuma execução.

---

## 7. Gateways e repositórios: persistência — 3:40 a 4:05

### Classes principais

- interfaces do núcleo: `TerritoryGateway` e `SearchActionGateway`;
- implementações de infraestrutura: `TerritoryRepositoryAdapter` e `SearchActionRepositoryAdapter`;
- acesso ao banco: `TerritoryJpaRepository` e `SearchActionJpaRepository`.

### O que este tópico explica

Os gateways são contratos definidos pelo núcleo. Os casos de uso dependem desses contratos, não de JPA.

Os adapters implementam os gateways e fazem a conversão entre os objetos do domínio e as entidades persistidas. Somente depois dessa adaptação os repositórios Spring Data executam as operações no PostgreSQL.

Essa inversão de dependência preserva o núcleo e permite substituir a tecnologia de persistência sem alterar a regra de negócio.

### Fala sugerida

> Para acessar os dados, os casos de uso dependem de interfaces chamadas gateways. As implementações ficam na infraestrutura.
>
> Os adapters traduzem os objetos do domínio para o formato de persistência e utilizam os repositórios JPA para conversar com o PostgreSQL. Dessa forma, o domínio não precisa conhecer JPA, tabelas ou consultas específicas do banco.

---

## 8. API: disponibilidade do serviço — 4:05 a 4:20

### Requisição relacionada

`01 - Health | serviço no ar`

### O que este tópico explica

O endpoint de health demonstra que a aplicação está disponível e que o ambiente da apresentação está respondendo. Ele é uma verificação técnica da execução do serviço, não uma funcionalidade de negócio.

### Fala sugerida

> O primeiro retorno confirma que o serviço está no ar e pronto para receber as requisições. Essa é uma verificação técnica do ambiente; a partir daqui começa o fluxo funcional do produto.

---

## 9. API: visão inicial do dashboard — 4:20 a 4:45

### Requisição relacionada

`02 - Dashboard inicial | fila territorial`

### O que este tópico explica

O dashboard reúne informações que, isoladamente, estariam espalhadas entre territórios e ações. Ele apresenta:

- quantidade de territórios por nível de prioridade;
- principais prioridades do momento;
- ações abertas e concluídas;
- alertas de prazo ou execução.

Seu objetivo é oferecer uma leitura rápida do cenário e direcionar a atenção do coordenador, sem substituir a análise detalhada de cada território.

### Fala sugerida

> O dashboard apresenta uma visão agregada da operação. Ele mostra quantos territórios estão em cada nível de prioridade, quais aparecem no topo da fila e como estão as ações abertas ou concluídas.
>
> Essa tela funciona como ponto de partida para a decisão. Ela resume o cenário, mas os motivos de cada prioridade continuam disponíveis no detalhe do território.

---

## 10. API: fila de prioridades altas — 4:45 a 5:05

### Requisição relacionada

`03 - Prioridades HIGH | escolher território`

### O que este tópico explica

A listagem transforma os indicadores em uma fila territorial. O caso de uso classifica todos os territórios, aplica o filtro solicitado e devolve os resultados ordenados.

Na demonstração, o filtro `HIGH` destaca os territórios em que a vinculação e pelo menos um indicador preventivo estão simultaneamente abaixo das metas. Essa combinação sugere maior necessidade operacional de atenção.

### Fala sugerida

> Aqui a aplicação transforma os indicadores em uma fila de trabalho. O filtro de prioridade alta mostra os territórios que apresentam, ao mesmo tempo, vinculação abaixo da meta e algum indicador preventivo também abaixo do esperado.
>
> Assim, o gestor não recebe apenas uma lista de números: ele recebe uma ordem inicial para investigar e planejar a atuação.

---

## 11. API: detalhe e explicação da prioridade — 5:05 a 5:35

### Requisição relacionada

`04 - Detalhe Jardim Esperança | explicar regra`

### O que este tópico explica

O detalhe mostra a competência dos dados, os valores observados, as metas, o nível calculado e os motivos da classificação.

No exemplo fictício do Jardim Esperança:

- a população vinculada está em 42%, diante de uma meta de 50%;
- o indicador de condições crônicas está em 32%, diante de uma meta de 60%;
- o acompanhamento pré-natal está em 72%, diante de uma meta de 85%.

Como a vinculação e indicadores preventivos estão abaixo das metas, o território recebe prioridade `HIGH`. A resposta também pode incluir ações já associadas ao território, conectando a análise ao histórico operacional.

### Fala sugerida

> No detalhe conseguimos entender a classificação. O Jardim Esperança é um território fictício usado na demonstração. Ele tem 42% de vinculação para uma meta de 50%. Também apresenta indicadores preventivos abaixo das metas, como condições crônicas e acompanhamento pré-natal.
>
> Por isso, a prioridade é alta. O mais importante é que a API apresenta os valores, as metas e os motivos da decisão. A classificação é explicável e pode ser conferida pelo gestor.

---

## 12. API: criação da ação territorial — 5:35 a 6:05

### Requisição relacionada

`05 - Criar ação territorial | prioridade vira trabalho`

### O que este tópico explica

A criação da ação converte a prioridade identificada em trabalho planejado. A ação pertence ao território, possui um foco preventivo, período de execução, meta agregada e equipe responsável.

O sistema registra quantidades totais, nunca uma relação nominal de pacientes. Ao ser criada, a ação começa em `PLANNED`, indicando que existe um planejamento, mas a execução ainda não foi iniciada.

### Fala sugerida

> Depois de entender a prioridade, o sistema permite transformar essa análise em uma ação territorial. A ação define o foco preventivo, o período, a equipe responsável e uma meta agregada de execução.
>
> Ela é vinculada ao território, e não a uma lista de pacientes. Quando é criada, começa com o estado planejado. Isso representa a passagem da análise para uma atividade operacional concreta.

---

## 13. API: atualização do progresso — 6:05 a 6:35

### Requisição relacionada

`06 - Atualizar progresso | execução agregada`

### O que este tópico explica

A atualização registra a evolução da ação criada anteriormente. A quantidade realizada, o estado e as observações continuam sendo informações agregadas.

O domínio calcula o percentual de progresso comparando a quantidade realizada com a meta. Ele também valida a coerência da transição. Assim, o sistema não apenas armazena dados: ele protege as regras do processo operacional.

### Fala sugerida

> Conforme a equipe executa a ação, o progresso pode ser atualizado. O sistema registra a quantidade realizada, o novo estado e uma observação geral do resultado.
>
> A própria entidade calcula o percentual de execução com base na meta e valida se a atualização é coerente. Continuamos trabalhando apenas com totais agregados, sem acompanhamento individual de pessoas.

---

## 14. API: fechamento do ciclo no dashboard — 6:35 a 7:00

### Requisição relacionada

`07 - Dashboard após progresso | fechar ciclo`

### O que este tópico explica

Depois da atualização, o dashboard passa a refletir a situação mais recente das ações. Isso fecha o ciclo principal do MVP:

```text
observar indicadores
    → priorizar territórios
    → explicar a prioridade
    → planejar uma ação
    → acompanhar a execução
```

O MVP demonstra acompanhamento operacional. Ele não afirma que a ação causou melhora clínica ou alteração imediata nos indicadores, pois essa conclusão exigiria avaliação longitudinal e metodologia própria.

### Fala sugerida

> Ao consultar novamente o dashboard, a visão operacional já considera o progresso registrado. Com isso, fechamos o ciclo principal do MVP: observar os indicadores, priorizar o território, explicar a decisão, planejar uma ação e acompanhar sua execução.
>
> O produto acompanha o trabalho realizado. Ele não tenta afirmar, a partir dessa atualização, que houve uma melhora clínica ou que a ação causou uma mudança nos indicadores. Esse tipo de conclusão exigiria outro período e uma análise específica.

---

## 15. Encerramento técnico — 7:00 a 7:20

### O que este tópico explica

A conclusão deve conectar as decisões técnicas ao propósito do produto:

- Clean Architecture separa responsabilidades e protege o domínio;
- a regra de prioridade é explícita e auditável;
- os dados usados pelo produto são territoriais e agregados;
- a API cobre o fluxo completo, da análise ao acompanhamento da ação;
- o resultado apoia decisões operacionais, sem substituir avaliação clínica.

### Fala sugerida

> Tecnicamente, a solução mantém as regras do negócio separadas da API e da persistência. A prioridade é calculada de forma explícita, os motivos ficam disponíveis para conferência e todo o fluxo trabalha com dados territoriais e agregados.
>
> Assim, o MVP demonstra como dados públicos podem apoiar uma decisão operacional e se transformar em uma ação acompanhável, preservando os limites éticos e o papel dos profissionais da Atenção Primária.

---

## Mapa rápido das responsabilidades

| Etapa | Componente principal | Ideia central |
|---|---|---|
| Entrada HTTP | `ApsPrioritizationController` | Recebe e adapta a requisição. |
| Coordenação | Classes `UseCase` | Organizam cada operação do sistema. |
| Regra de prioridade | `PriorityCalculator` | Classifica e explica a prioridade. |
| Regra da ação | `SearchAction` | Controla estado e progresso agregado. |
| Contratos de dados | `TerritoryGateway` e `SearchActionGateway` | Isolam o núcleo da persistência. |
| Adaptação | `TerritoryRepositoryAdapter` e `SearchActionRepositoryAdapter` | Convertem domínio e persistência. |
| Banco de dados | Repositórios JPA e PostgreSQL | Armazenam territórios e ações. |

## Resumo das etapas demonstradas na API

| Etapa | Significado |
|---|---|
| Health | Confirma a disponibilidade técnica do serviço. |
| Dashboard inicial | Resume prioridades, ações e alertas. |
| Filtro `HIGH` | Destaca territórios com dois grupos de sinais abaixo das metas. |
| Detalhe | Explica valores, metas e motivos da classificação. |
| Criação da ação | Converte prioridade em planejamento territorial. |
| Atualização | Registra a execução agregada e aplica regras de domínio. |
| Dashboard final | Reflete o progresso e fecha o ciclo operacional. |
