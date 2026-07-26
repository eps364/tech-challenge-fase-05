# Roteiro de regravacao do pitch - SUS Conecta

Roteiro sincronizado com `apresentacao_aps.html`.

## Como usar sem parecer leitura

- Use cada paragrafo como uma ideia, e nao como uma frase para decorar.
- Olhe rapidamente para o roteiro, volte para a camera e conte a ideia com suas
  palavras.
- Nas telas com numeros, aponte primeiro para o slide e depois explique o que
  chamou sua atencao.
- As expressoes "a gente", "na pratica" e "o ponto aqui" sao intencionais:
  deixam a fala mais proxima de uma conversa.
- Se trocar uma palavra durante a gravacao, continue. O sentido e mais
  importante do que repetir o texto exatamente.

## Controle de tempo

- Fala planejada: aproximadamente **6 min 50 s**.
- Gravacao esperada, com pausas e troca de slides: **7 min 10 s a 7 min 30 s**.
- Limite de seguranca para encerrar: **7 min 40 s**.
- Limite do enunciado: **8 minutos**.

> Nao tente preencher todo o tempo disponivel. A margem final absorve pausas,
> pequenas hesitacoes e o tempo de troca de tela.

## O que e real e o que e demonstrativo

| Elemento apresentado | Classificacao | Como explicar |
|---|---|---|
| Bases publicas do IBGE, SISAB/Previne Brasil e Dados Abertos do SUS | Real | Sao dados oficiais, publicos e agregados |
| Numeros nacionais exibidos nos slides | Real, calculado pelo projeto | Sao resultados reproduziveis da analise das bases oficiais |
| Jardim Esperanca, Parque das Flores, Vila Nova e Centro | Ficticio | Sao territorios criados apenas para demonstrar o produto |
| Nomes de UBS, indicadores, metas, equipes e acoes desses territorios | Simulado | Nao representam municipios, unidades ou pessoas reais |
| Respostas HTTP, persistencia, validacoes e cobertura de testes | Evidencia tecnica real | Foram obtidas executando a aplicacao sobre a massa ficticia |

## Tipos de dados utilizados

| Tipo de dado agregado | Fonte principal | Uso no projeto |
|---|---|---|
| Populacao estimada por municipio | IBGE, estimativa de 2025 | Dimensionar os territorios e calcular indicadores relativos |
| Cadastros vinculados por municipio | SISAB, dezembro de 2024 | Construir uma aproximacao de cobertura de vinculo |
| Percentuais de indicadores preventivos | SISAB/Previne Brasil, 3o quadrimestre de 2024 | Identificar lacunas agregadas de acompanhamento preventivo |
| Quantidade de UBS e estabelecimentos | Dados Abertos do SUS | Acrescentar contexto de estrutura territorial |
| Leitos e UTI | Dados Abertos do SUS | Analise exploratoria de contexto; nao entra no score principal |

As bases possuem competencias diferentes. A analise e um recorte exploratorio,
nao um painel em tempo real e nao uma medicao de risco individual, qualidade
assistencial, fila de espera ou impacto causal.

---

## Slide 1 - Abertura

**Tempo-alvo: 0:00 a 0:45**

> Oi, eu sou Luiz Saraiva. Eu e o Emerson Silva desenvolvemos juntos o SUS
> Conecta para a Fase 5 do Tech Challenge.
>
> A ideia nasceu de uma pergunta bem pratica: se a equipe nao consegue atuar em
> todos os lugares ao mesmo tempo, como decidir qual territorio precisa de
> atencao primeiro?
>
> Hoje, essa analise pode depender de varias planilhas, indicadores separados e
> muito trabalho manual.
>
> O que o SUS Conecta faz e organizar esses dados, explicar a prioridade e
> ajudar a acompanhar a acao. Tudo isso olhando para o territorio, sem
> identificar pacientes e sem tirar da equipe a decisao final.

**[AVANCE]**

---

## Slide 2 - Evidencia e tipos de dados

**Tempo-alvo: 0:45 a 1:40**

> Para entender se esse problema realmente tinha escala, a gente foi atras de
> dados oficiais e sempre agregados.
>
> Usamos cinco tipos de informacao: populacao por municipio, cadastros
> vinculados a APS, indicadores preventivos, quantidade de UBS e
> estabelecimentos, alem de leitos e UTI para entender o contexto da rede.
>
> A populacao vem do IBGE de 2025. Os dados de vinculo sao do SISAB, de
> dezembro de 2024, e os indicadores sao do terceiro quadrimestre de 2024. Os
> dados de estrutura ajudaram no contexto, mas leitos e UTI nao entram na regra
> de prioridade do produto.
>
> E esses numeros que aparecem na tela sao reais: foram 5.571 municipios,
> cerca de 213,4 milhoes de habitantes e mais de 100 mil registros de
> indicadores analisados. Como as fontes sao de periodos diferentes, esse e um
> recorte exploratorio, nao uma fotografia do SUS em tempo real.

**[AVANCE]**

---

## Slide 3 - Oportunidade encontrada

**Tempo-alvo: 1:40 a 2:35**

> Depois veio a parte de tratamento dos dados. A gente juntou as bases pelo
> codigo do municipio, padronizou os percentuais e aplicou os mesmos filtros em
> todo o conjunto.
>
> O que apareceu foi o seguinte: a aproximacao nacional de vinculo ficou em
> 38,11%. Entre os municipios com pelo menos 20 mil habitantes, 1.091 ficaram
> abaixo de 50%. E, nesse mesmo recorte, 276 tiveram media inferior a 40% nos
> indicadores preventivos.
>
> Aqui tem um cuidado importante: esses volumes sao registros processados, nao
> pessoas atendidas. E o percentual de vinculo e uma aproximacao historica,
> porque as bases nao sao todas do mesmo periodo.
>
> Entao, o que a analise mostra e uma oportunidade para investigar e organizar
> melhor a resposta local. Ela nao avalia a qualidade do atendimento e nao
> prova que existe um problema clinico naquele municipio.

**[AVANCE]**

---

## Slide 4 - Produto e regra de priorizacao

**Tempo-alvo: 2:35 a 3:40**

> Agora eu entro na parte do produto. Mas, antes, um ponto importante: Jardim
> Esperanca, Parque das Flores, Vila Nova e Centro sao exemplos ficticios. Os
> nomes das UBS, os percentuais, as metas e as equipes tambem foram simulados.
>
> A logica e bem direta. Se o vinculo esta abaixo da meta e pelo menos um
> indicador preventivo tambem esta, a prioridade e alta. Se apenas um desses
> sinais esta abaixo, ela e media. Se todos atingem as metas, ela e baixa.
>
> Pegando Jardim Esperanca como exemplo: o vinculo esta em 42%, para uma meta
> de 50%. Condicoes cronicas esta em 32%, para uma meta de 60%. E o
> acompanhamento pre-natal esta em 72%, para uma meta de 85%.
>
> Por isso o territorio aparece com prioridade alta. E, em vez de mostrar so
> uma cor, o painel explica os motivos. A regra ajuda a coordenacao, mas quem
> decide o que fazer continua sendo a equipe.

**[AVANCE]**

---

## Slide 5 - Acao e evidencias tecnicas

**Tempo-alvo: 3:40 a 4:40**

> Depois que a coordenacao escolhe uma prioridade, ela consegue transformar
> esse sinal em uma acao de verdade, com foco, equipe responsavel, prazo e
> meta.
>
> Nesse exemplo, o foco e acompanhamento de condicoes cronicas, quem assume e
> uma equipe de Saude da Familia e o prazo e de sete dias. A meta e realizar 80
> contatos, e o painel mostra 54 realizados. Esses valores continuam sendo
> ficticios.
>
> O que e real aqui e a validacao tecnica. A API criou a acao e retornou 201,
> salvou os dados no PostgreSQL, atualizou o progresso e bloqueou, com retorno
> 422, uma tentativa de concluir a acao sem informar o resultado.
>
> Os 67,5% mostram apenas o andamento da acao, nao uma melhora clinica. E os
> testes automatizados chegaram a 98,94% de cobertura de linhas.

**[AVANCE]**

---

## Slide 6 - Fluxo e arquitetura

**Tempo-alvo: 4:40 a 5:30**

> Por tras desse fluxo, a solucao faz cinco coisas: recebe os dados agregados,
> calcula as lacunas, organiza os territorios, registra a acao e acompanha o
> andamento.
>
> A gente construiu a aplicacao com Java 21, Spring Boot e Clean Architecture.
> Na pratica, as regras principais ficam separadas da API e do banco de dados.
> Isso deixa a regra mais facil de entender, testar e evoluir.
>
> A persistencia usa PostgreSQL e Flyway. Para a demonstracao, os containers
> sobem o servico e o banco sem depender de prontuario ou de uma integracao
> externa em tempo real.

**[AVANCE]**

---

## Slide 7 - Diferencial e impacto esperado

**Tempo-alvo: 5:30 a 6:20**

> Para a gente, o principal diferencial e que o SUS Conecta nao para no
> indicador. Ele transforma uma lacuna em uma proxima acao: onde atuar, por que,
> com qual meta, em qual prazo e com qual resultado.
>
> Para a gestao, isso pode reduzir o trabalho manual e deixar a prioridade mais
> facil de explicar. Para a equipe da UBS, fica mais claro o que precisa ser
> feito. E tudo isso sem usar dados pessoais na priorizacao.
>
> O impacto, por enquanto, e uma hipotese. A gente espera reduzir o tempo gasto
> nessa decisao e aumentar o numero de acoes que chegam ao fim com um resultado
> registrado.
>
> O jeito certo de confirmar isso e com um piloto, comparando esses indicadores
> antes e depois. Neste momento, a gente ainda nao afirma impacto clinico ou
> causal.

**[AVANCE]**

---

## Slide 8 - Encerramento

**Tempo-alvo: 6:20 a 6:50**

> Daqui para frente, os proximos passos sao validar a solucao com coordenadores,
> ajustar as metas conforme a realidade local e, depois, estudar integracoes
> autorizadas.
>
> Se eu tivesse que resumir o projeto em uma frase, seria esta: o SUS Conecta
> ajuda a transformar dados agregados em uma decisao territorial mais clara e
> em uma acao que pode ser acompanhada.
>
> Sem expor dados individuais e sem substituir quem realmente conhece o
> territorio: a equipe de saude. Obrigado.

---

## Frases curtas para responder a banca

### Os dados sao reais?

> As bases e os numeros nacionais da analise sao reais, publicos e agregados.
> Ja os territorios e as acoes da demonstracao foram criados por nos e sao
> ficticios.

### O sistema prioriza pacientes?

> Nao. Ele olha para territorios e unidades usando indicadores agregados. Nao
> existe score individual, prontuario ou decisao clinica.

### O score usa inteligencia artificial?

> Nao nesta versao. A regra e direta e pode ser auditada: ela compara o
> indicador com a meta e mostra por que chegou aquela prioridade.

### O produto prova impacto na saude?

> Ainda nao. O MVP mostra que a solucao funciona tecnicamente e apresenta uma
> hipotese de ganho operacional. Impacto na saude so pode ser avaliado em um
> piloto adequado.

### Por que as competencias das bases sao diferentes?

> Porque usamos as publicacoes oficiais que estavam disponiveis, e cada fonte
> tem seu proprio calendario. Por isso a analise e um recorte exploratorio, nao
> um retrato em tempo real.

## Expressoes que devem ser evitadas

Evitar:

- "pacientes de risco";
- "o sistema decide";
- "o produto comprova que";
- "dados em tempo real";
- "previne internacoes";
- "a IA escolhe quem sera atendido".

Preferir:

- "territorios prioritarios";
- "apoio a decisao";
- "a analise sugere";
- "recorte exploratorio";
- "impacto esperado";
- "regra deterministica e explicavel".

## Checklist de gravacao

- Apresentar os dois participantes apenas uma vez, na abertura.
- Manter um cronometro visivel e avancar o slide nos marcadores `[AVANCE]`.
- Aos 4 minutos, estar no slide 5.
- Aos 6 minutos e 20 segundos, iniciar o encerramento.
- Se houver atraso, cortar detalhes do slide 6; nunca acelerar a conclusao.
- Encerrar ate 7 minutos e 40 segundos para preservar margem.
- Nao abrir codigo, terminal ou documentacao tecnica neste video de pitch.
- Gravar o video tecnico separadamente, seguindo
  `roteiro-video-mvp-tecnico.md`.
