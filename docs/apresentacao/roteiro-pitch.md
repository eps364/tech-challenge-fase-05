# Roteiro de regravação do pitch - SUS Conecta

Roteiro sincronizado com `apresentacao_aps.html`.

## Como usar sem parecer leitura

- Use cada parágrafo como uma ideia, e não como uma frase para decorar.
- Olhe rapidamente para o roteiro, volte para a câmera e conte a ideia com suas
  palavras.
- Nas telas com números, aponte primeiro para o slide e depois explique o que
  chamou sua atenção.
- As expressões "a gente", "na prática" e "o ponto aqui" são intencionais:
  deixam a fala mais próxima de uma conversa.
- Se trocar uma palavra durante a gravação, continue. O sentido é mais
  importante do que repetir o texto exatamente.

## Controle de tempo

- Fala planejada: aproximadamente **6 min 50 s**.
- Gravação esperada, com pausas e troca de slides: **7 min 10 s a 7 min 30 s**.
- Limite de segurança para encerrar: **7 min 40 s**.
- Limite do enunciado: **8 minutos**.

> Não tente preencher todo o tempo disponível. A margem final absorve pausas,
> pequenas hesitações e o tempo de troca de tela.

## O que é real e o que é demonstrativo

| Elemento apresentado | Classificação | Como explicar |
|---|---|---|
| Bases públicas do IBGE, SISAB/Previne Brasil e Dados Abertos do SUS | Real | São dados oficiais, públicos e agregados |
| Números nacionais exibidos nos slides | Real, calculado pelo projeto | São resultados reproduzíveis da análise das bases oficiais |
| Jardim Esperança, Parque das Flores, Vila Nova e Centro | Fictício | São territórios criados apenas para demonstrar o produto |
| Nomes de UBS, indicadores, metas, equipes e ações desses territórios | Simulado | Não representam municípios, unidades ou pessoas reais |
| Respostas HTTP, persistência, validações e cobertura de testes | Evidência técnica real | Foram obtidas executando a aplicação sobre a massa fictícia |

## Tipos de dados utilizados

| Tipo de dado agregado | Fonte principal | Uso no projeto |
|---|---|---|
| População estimada por município | IBGE, estimativa de 2025 | Dimensionar os territórios e calcular indicadores relativos |
| Cadastros vinculados por município | SISAB, dezembro de 2024 | Construir uma aproximação de cobertura de vínculo |
| Percentuais de indicadores preventivos | SISAB/Previne Brasil, 3º quadrimestre de 2024 | Identificar lacunas agregadas de acompanhamento preventivo |
| Quantidade de UBS e estabelecimentos | Dados Abertos do SUS | Acrescentar contexto de estrutura territorial |
| Leitos e UTI | Dados Abertos do SUS | Análise exploratória de contexto; não entra no score principal |

As bases possuem competências diferentes. A análise é um recorte exploratório,
não um painel em tempo real e não uma medição de risco individual, qualidade
assistencial, fila de espera ou impacto causal.

---

## Slide 1 - Abertura

**Tempo-alvo: 0:00 a 0:45**

> Oi, eu sou Luiz Saraiva. Eu e o Emerson Silva desenvolvemos juntos o SUS
> Conecta para a Fase 5 do Tech Challenge.
>
> A ideia nasceu de uma pergunta bem prática: se a equipe não consegue atuar em
> todos os lugares ao mesmo tempo, como decidir qual território precisa de
> atenção primeiro?
>
> Hoje, essa análise pode depender de várias planilhas, indicadores separados e
> muito trabalho manual.
>
> O que o SUS Conecta faz é organizar esses dados, explicar a prioridade e
> ajudar a acompanhar a ação. Tudo isso olhando para o território, sem
> identificar pacientes e sem tirar da equipe a decisão final.

**[AVANCE]**

---

## Slide 2 - Evidência e tipos de dados

**Tempo-alvo: 0:45 a 1:40**

> Para entender se esse problema realmente tinha escala, a gente foi atrás de
> dados oficiais e sempre agregados.
>
> Usamos cinco tipos de informação: população por município, cadastros
> vinculados à APS, indicadores preventivos, quantidade de UBS e
> estabelecimentos, além de leitos e UTI para entender o contexto da rede.
>
> A população vem do IBGE de 2025. Os dados de vínculo são do SISAB, de
> dezembro de 2024, e os indicadores são do terceiro quadrimestre de 2024. Os
> dados de estrutura ajudaram no contexto, mas leitos e UTI não entram na regra
> de prioridade do produto.
>
> E esses números que aparecem na tela são reais: foram 5.571 municípios,
> cerca de 213,4 milhões de habitantes e mais de 100 mil registros de
> indicadores analisados. Como as fontes são de períodos diferentes, esse é um
> recorte exploratório, não uma fotografia do SUS em tempo real.

**[AVANCE]**

---

## Slide 3 - Oportunidade encontrada

**Tempo-alvo: 1:40 a 2:35**

> Depois veio a parte de tratamento dos dados. A gente juntou as bases pelo
> código do município, padronizou os percentuais e aplicou os mesmos filtros em
> todo o conjunto.
>
> O que apareceu foi o seguinte: a aproximação nacional de vínculo ficou em
> 38,11%. Entre os municípios com pelo menos 20 mil habitantes, 1.091 ficaram
> abaixo de 50%. E, nesse mesmo recorte, 276 tiveram média inferior a 40% nos
> indicadores preventivos.
>
> Aqui tem um cuidado importante: esses volumes são registros processados, não
> pessoas atendidas. E o percentual de vínculo é uma aproximação histórica,
> porque as bases não são todas do mesmo período.
>
> Então, o que a análise mostra é uma oportunidade para investigar e organizar
> melhor a resposta local. Ela não avalia a qualidade do atendimento e não
> prova que existe um problema clínico naquele município.

**[AVANCE]**

---

## Slide 4 - Produto e regra de priorização

**Tempo-alvo: 2:35 a 3:40**

> Agora eu entro na parte do produto. Mas, antes, um ponto importante: Jardim
> Esperança, Parque das Flores, Vila Nova e Centro são exemplos fictícios. Os
> nomes das UBS, os percentuais, as metas e as equipes também foram simulados.
>
> A lógica é bem direta. Se o vínculo está abaixo da meta e pelo menos um
> indicador preventivo também está, a prioridade é alta. Se apenas um desses
> sinais está abaixo, ela é média. Se todos atingem as metas, ela é baixa.
>
> Pegando Jardim Esperança como exemplo: o vínculo está em 42%, para uma meta
> de 50%. Condições crônicas está em 32%, para uma meta de 60%. E o
> acompanhamento pré-natal está em 72%, para uma meta de 85%.
>
> Por isso o território aparece com prioridade alta. E, em vez de mostrar só
> uma cor, o painel explica os motivos. A regra ajuda a coordenação, mas quem
> decide o que fazer continua sendo a equipe.

**[AVANCE]**

---

## Slide 5 - Ação e evidências técnicas

**Tempo-alvo: 3:40 a 4:40**

> Depois que a coordenação escolhe uma prioridade, ela consegue transformar
> esse sinal em uma ação de verdade, com foco, equipe responsável, prazo e
> meta.
>
> Nesse exemplo, o foco é acompanhamento de condições crônicas, quem assume é
> uma equipe de Saúde da Família e o prazo é de sete dias. A meta é realizar 80
> contatos, e o painel mostra 54 realizados. Esses valores continuam sendo
> fictícios.
>
> O que é real aqui é a validação técnica. A API criou a ação e retornou 201,
> salvou os dados no PostgreSQL, atualizou o progresso e bloqueou, com retorno
> 422, uma tentativa de concluir a ação sem informar o resultado.
>
> Os 67,5% mostram apenas o andamento da ação, não uma melhora clínica. E os
> testes automatizados chegaram a 98,94% de cobertura de linhas.

**[AVANCE]**

---

## Slide 6 - Fluxo e arquitetura

**Tempo-alvo: 4:40 a 5:30**

> Por trás desse fluxo, a solução faz cinco coisas: recebe os dados agregados,
> calcula as lacunas, organiza os territórios, registra a ação e acompanha o
> andamento.
>
> A gente construiu a aplicação com Java 21, Spring Boot e Clean Architecture.
> Na prática, as regras principais ficam separadas da API e do banco de dados.
> Isso deixa a regra mais fácil de entender, testar e evoluir.
>
> A persistência usa PostgreSQL e Flyway. Para a demonstração, os containers
> sobem o serviço e o banco sem depender de prontuário ou de uma integração
> externa em tempo real.

**[AVANCE]**

---

## Slide 7 - Diferencial e impacto esperado

**Tempo-alvo: 5:30 a 6:20**

> Para a gente, o principal diferencial é que o SUS Conecta não para no
> indicador. Ele transforma uma lacuna em uma próxima ação: onde atuar, por que,
> com qual meta, em qual prazo e com qual resultado.
>
> Para a gestão, isso pode reduzir o trabalho manual e deixar a prioridade mais
> fácil de explicar. Para a equipe da UBS, fica mais claro o que precisa ser
> feito. E tudo isso sem usar dados pessoais na priorização.
>
> O impacto, por enquanto, é uma hipótese. A gente espera reduzir o tempo gasto
> nessa decisão e aumentar o número de ações que chegam ao fim com um resultado
> registrado.
>
> O jeito certo de confirmar isso é com um piloto, comparando esses indicadores
> antes e depois. Neste momento, a gente ainda não afirma impacto clínico ou
> causal.

**[AVANCE]**

---

## Slide 8 - Encerramento

**Tempo-alvo: 6:20 a 6:50**

> Daqui para frente, os próximos passos são validar a solução com coordenadores,
> ajustar as metas conforme a realidade local e, depois, estudar integrações
> autorizadas.
>
> Se eu tivesse que resumir o projeto em uma frase, seria esta: o SUS Conecta
> ajuda a transformar dados agregados em uma decisão territorial mais clara e
> em uma ação que pode ser acompanhada.
>
> Sem expor dados individuais e sem substituir quem realmente conhece o
> território: a equipe de saúde. Obrigado.

---

## Frases curtas para responder à banca

### Os dados são reais?

> As bases e os números nacionais da análise são reais, públicos e agregados.
> Já os territórios e as ações da demonstração foram criados por nós e são
> fictícios.

### O sistema prioriza pacientes?

> Não. Ele olha para territórios e unidades usando indicadores agregados. Não
> existe score individual, prontuário ou decisão clínica.

### O score usa inteligência artificial?

> Não nesta versão. A regra é direta e pode ser auditada: ela compara o
> indicador com a meta e mostra por que chegou aquela prioridade.

### O produto prova impacto na saúde?

> Ainda não. O MVP mostra que a solução funciona tecnicamente e apresenta uma
> hipótese de ganho operacional. Impacto na saúde só pode ser avaliado em um
> piloto adequado.

### Por que as competências das bases são diferentes?

> Porque usamos as publicações oficiais que estavam disponíveis, e cada fonte
> tem seu próprio calendário. Por isso a análise é um recorte exploratório, não
> um retrato em tempo real.

## Expressões que devem ser evitadas

Evitar:

- "pacientes de risco";
- "o sistema decide";
- "o produto comprova que";
- "dados em tempo real";
- "previne internações";
- "a IA escolhe quem será atendido".

Preferir:

- "territórios prioritários";
- "apoio à decisão";
- "a análise sugere";
- "recorte exploratório";
- "impacto esperado";
- "regra determinística e explicável".

## Checklist de gravação

- Apresentar os dois participantes apenas uma vez, na abertura.
- Manter um cronômetro visível e avançar o slide nos marcadores `[AVANCE]`.
- Aos 4 minutos, estar no slide 5.
- Aos 6 minutos e 20 segundos, iniciar o encerramento.
- Se houver atraso, cortar detalhes do slide 6; nunca acelerar a conclusão.
- Encerrar até 7 minutos e 40 segundos para preservar margem.
- Não abrir código, terminal ou documentação técnica neste vídeo de pitch.
- Gravar o vídeo técnico separadamente, seguindo
  `roteiro-video-mvp-tecnico.md`.
