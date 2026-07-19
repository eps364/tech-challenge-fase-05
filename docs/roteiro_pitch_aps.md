# Roteiro de pitch - SUS Conecta

Duracao sugerida: 5 minutos e 45 segundos.

Publico: banca do hackathon, gestores de APS e pessoas interessadas na
operacao do SUS.

Material de apoio: `docs/apresentacao_aps.html`.

## Mensagem central

O SUS Conecta apoia a coordenacao da APS a transformar indicadores territoriais
dispersos em uma prioridade explicavel de busca ativa. A proposta nao decide o
tratamento de uma pessoa e nao promete evitar internacoes. Ela ajuda a equipe a
organizar uma resposta preventiva, acompanhar sua execucao agregada e dar
visibilidade ao proximo ciclo de decisao.

## Preparacao

- Abra `docs/apresentacao_aps.html` em tela cheia.
- Apresente os oito slides usando as setas do teclado.
- Fale "dados demonstrativos" ao chegar ao painel de Jardim Esperanca.
- Reserve os ultimos 15 segundos para a frase de encerramento, sem avancar o
  slide final antes dela.

## Roteiro falado

### Slide 1 - A oportunidade antes da urgencia (35 segundos)

> Pense em Joao, um personagem ficticio que perdeu a continuidade do
> acompanhamento de hipertensao e diabetes. Anos depois, ele procura uma UPA
> com um quadro mais grave. Nao estamos dizendo que uma ferramenta teria
> evitado esse desfecho. O que existe antes da urgencia e uma oportunidade de
> organizacao: perceber que um territorio pode estar desconectado do cuidado
> preventivo e agir antes, de forma coordenada.
>
> O SUS Conecta nasce para isso. Ele ajuda a coordenacao da Atencao Primaria a
> decidir onde comecar uma busca ativa territorial, com clareza, explicacao e
> responsabilidade.

Transicao: "E essa nao e uma dor isolada. Os dados mostram a escala da
oportunidade."

### Slide 2 - A evidencia que orientou a escolha (45 segundos)

> Na analise de bases abertas, observamos um vinculo aproximado de 38,11% da
> populacao na APS. Encontramos 1.071 municipios com mais de 20 mil habitantes
> e vinculo abaixo de 50%, alem de 273 municipios nessa faixa populacional com
> media de indicadores abaixo de 40.
>
> Processamos 100.242 registros de indicadores, dados de 5.570 municipios e a
> populacao do Censo 2022. Esses numeros nao diagnosticam pessoas e nao provam
> uma causa clinica. Eles mostram, com evidencias agregadas, onde a coordenacao
> pode precisar investigar e organizar uma resposta preventiva.

Transicao: "Mas dado sem uma decisao clara ainda vira mais uma planilha para a
rotina da equipe."

### Slide 3 - A dor operacional (40 segundos)

> Hoje, um coordenador pode precisar cruzar cobertura, indicadores, planilhas e
> historico de acoes para decidir a proxima prioridade. Com tempo e equipe
> limitados, a busca ativa corre o risco de ser reativa, dificil de explicar e
> ainda mais dificil de acompanhar.
>
> Nosso problema nao e a ausencia de esforco das equipes. E a falta de uma
> leitura unica, simples e territorial para transformar esse esforco em uma
> rotina preventiva mais coordenada.

Transicao: "Por isso, o MVP reduz a decisao a uma pergunta objetiva: onde agir
primeiro e por qual motivo?"

### Slide 4 - O dashboard e a prioridade explicavel (60 segundos)

> Este e o painel do MVP com dados demonstrativos agregados. O territorio
> Jardim Esperanca aparece primeiro porque tem tres sinais claros: 42% de
> populacao vinculada, abaixo da meta de 50%; 32% no indicador de condicoes
> cronicas, abaixo da meta de 60%; e 72% no acompanhamento pre-natal, abaixo da
> meta de 85%.
>
> A regra e transparente: quando o vinculo fica abaixo da meta e pelo menos um
> indicador preventivo tambem fica abaixo da sua meta, a prioridade e alta. Se
> apenas um sinal fica abaixo, a prioridade e media. Quando todos atendem as
> referencias, ela e baixa.
>
> Isso nao e score clinico e nao e lista de pacientes. E uma fila territorial
> explicavel para que a coordenacao saiba por que aquele territorio deve receber
> atencao primeiro.

Transicao: "Priorizar e importante, mas so gera valor quando a decisao vira uma
acao acompanhavel."

### Slide 5 - Da prioridade para a acao (55 segundos)

> Para Jardim Esperanca, a coordenacao cria uma acao de reconexao ao
> acompanhamento de condicoes cronicas. Ela define foco, equipe responsavel,
> prazo e uma meta agregada de 80 contatos. A equipe atualiza o que foi feito,
> e o painel mostra 54 contatos realizados: 67,5% do objetivo operacional.
>
> O sistema acompanha a execucao, nao o prontuario de cada pessoa. E a regra
> protege a qualidade do processo: uma acao nao pode ser concluida sem uma
> quantidade realizada. Isso foi validado no fluxo ponta a ponta, com resposta
> 422 para a tentativa invalida e persistencia no PostgreSQL para a atualizacao
> valida.
>
> Assim, a gestao deixa de acompanhar apenas intencao e passa a enxergar a
> execucao da busca ativa.

Transicao: "E fizemos isso mantendo a arquitetura proporcional ao problema."

### Slide 6 - Como a solucao funciona (45 segundos)

> A entrada e exclusivamente territorial e agregada: vinculo, competencia dos
> dados e indicadores preventivos. Uma regra configuravel calcula a prioridade
> e exibe os motivos. A coordenacao enxerga a fila, decide a proxima acao e a
> equipe registra o progresso agregado. O painel fecha o ciclo com acoes
> abertas, concluidas e alertas de prazo.
>
> O nucleo e Java, com Spring, JPA e PostgreSQL na infraestrutura. O fluxo foi
> exercitado de ponta a ponta em containers dedicados e o projeto atingiu 98,94%
> de cobertura de linhas. Mais importante do que a tecnologia: a decisao humana
> continua no centro.

Transicao: "Essa escolha de escopo gera valor para quem gerencia, para quem
executa e para quem depende da rede."

### Slide 7 - Valor para o SUS e para a populacao (40 segundos)

> Para a gestao de APS, o SUS Conecta reduz a consolidacao manual e oferece uma
> fila territorial comparavel. Para as equipes de UBS, transforma uma lacuna em
> uma acao com foco, prazo e meta claros. Para a populacao, a hipotese de valor
> e uma rede mais atenta a continuidade preventiva, sem expor dados pessoais no
> mecanismo de priorizacao.
>
> Nao vendemos uma promessa clinica. Entregamos uma forma mais simples de
> explicar, executar e acompanhar uma decisao territorial que hoje pode estar
> espalhada em planilhas.

Transicao: "Em uma frase, esta e a mudanca que queremos levar para a APS."

### Slide 8 - Encerramento e convite (25 segundos)

> O SUS Conecta transforma sinal em acao e acao em acompanhamento. O MVP ja
> demonstra que e possivel priorizar um territorio com regras claras, criar uma
> busca ativa e acompanhar seu progresso de forma agregada e rastreavel.
>
> O proximo passo e validar com coordenadores de APS se essa explicacao
> territorial torna a preparacao da busca ativa mais rapida e mais util. Porque
> cuidar melhor tambem e conseguir enxergar, com tempo, onde a rede precisa se
> organizar primeiro.

## Respostas curtas para perguntas da banca

### "O sistema identifica pacientes em risco?"

> Nao. A unidade de priorizacao e o territorio ou a UBS. O MVP nao armazena
> dados pessoais, prontuarios, diagnosticos ou risco clinico individual. A
> abordagem e humana e operacional: a equipe local continua responsavel por
> qualquer contato e decisao assistencial.

### "Como voces definiram a prioridade?"

> Usamos uma regra explicavel: vinculo abaixo da meta mais pelo menos um
> indicador preventivo abaixo da sua meta resulta em prioridade alta. Um unico
> sinal abaixo gera prioridade media. As metas sao configuraveis e o painel
> mostra os motivos da classificacao.

### "Voces conseguem provar que isso reduz internacoes?"

> Ainda nao, e nao fazemos essa afirmacao. O MVP comprova o fluxo de
> priorizacao e acompanhamento operacional. O impacto assistencial precisa ser
> avaliado em uma validacao posterior com equipes e indicadores definidos pelo
> gestor.

### "Por que nao integrar logo com e-SUS ou prontuarios?"

> Para o hackathon, escolhemos um MVP demonstravel e seguro. A solucao funciona
> com dados agregados e nao depende de integracao externa em tempo real. Uma
> integracao futura so faz sentido depois de validar utilidade, governanca e
> requisitos de privacidade com a rede local.

## Frases a evitar no pitch

- "O sistema preve agravamento clinico."
- "O sistema evita internacoes."
- "Joao e um registro do sistema."
- "Os dados nacionais mostram a situacao atual de cada UBS."

Prefira: "apoia uma priorizacao territorial", "indica uma hipotese operacional"
e "acompanha a execucao agregada da busca ativa".
