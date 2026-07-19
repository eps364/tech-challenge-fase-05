# Roteiro completo de apresentacao - SUS Conecta

Duracao sugerida: 7 a 8 minutos.

Publico: banca do hackathon, gestores de APS e pessoas que ainda nao conhecem
o problema ou a proposta.

Material de apoio: `docs/apresentacao/apresentacao_aps.html`.

## Como usar este roteiro

Este texto foi escrito para ser lido no telefone durante a gravacao. Cada
bloco corresponde a um slide. Leia com calma, faca uma pequena pausa ao fim de
cada bloco e avance apenas quando encontrar a indicacao **[AVANCE]**.

O roteiro evita detalhes de linguagem de programacao, infraestrutura e testes
tecnicos. A mensagem deve ser entendida por qualquer pessoa: qual problema foi
observado, quais dados deram contexto a ele e como o SUS Conecta apoia uma
decisao operacional.

## Colinha de fontes para consulta rapida

| Base | Fonte oficial | Ano ou competencia | Uso na analise |
| --- | --- | --- | --- |
| Estimativas de populacao | IBGE | 2025, referencia em 01/07/2025 | Comparar municipios de tamanhos diferentes. |
| Cadastro vinculado | Ministerio da Saude, SISAB | 202412 | Estimar um sinal aproximado de vinculo com a APS. |
| Indicadores de desempenho | Ministerio da Saude, SISAB/Previne Brasil | 2024Q3 | Observar sinais agregados de acompanhamento preventivo. |
| Unidades Basicas de Saude | Ministerio da Saude, Dados Abertos do SUS | Atualizacao indicada em julho/2026 | Contextualizar a oferta territorial de UBS. |
| Hospitais e leitos | Ministerio da Saude, Dados Abertos do SUS | 202605, ou maio/2026 | Contextualizar a analise exploratoria da rede. |

Importante: as bases possuem competencias diferentes. Elas nao representam uma
fotografia em tempo real e nao permitem conclusao clinica sobre uma pessoa. No
projeto, elas sao evidencias agregadas para orientar uma investigacao e uma
decisao territorial.

## Slide 1 - Abertura: antes da urgencia existe uma oportunidade de cuidado

Tempo sugerido: 55 segundos.

> Ola, eu sou Luiz Saraiva e este projeto foi desenvolvido em dupla com
> Emerson Silva, para a Fase 5 do Tech Challenge.
>
> Nossa proposta se chama SUS Conecta. Antes de mostrar a solucao, eu quero
> explicar, de maneira simples, o problema que estamos tentando resolver.
>
> Na Atencao Primaria a Saude, ou APS, os coordenadores precisam decidir onde
> comecar uma acao preventiva. Eles lidam com varios territorios, indicadores,
> planilhas e equipes com capacidade limitada. A pergunta nao e apenas quais
> numeros estao baixos. A pergunta pratica e: em qual territorio devemos agir
> primeiro, por que devemos agir ali e como vamos acompanhar essa acao?
>
> O SUS Conecta apoia essa decisao. Ele organiza sinais territoriais agregados
> em uma prioridade clara para a busca ativa preventiva. Depois, ajuda a
> transformar essa prioridade em uma acao com equipe, prazo, meta e
> acompanhamento.
>
> A proposta nao identifica pacientes, nao diagnostica pessoas e nao substitui
> a decisao da equipe de saude. Ela ajuda a coordenacao a enxergar onde vale
> organizar o proximo esforco territorial.

**[AVANCE]**

## Slide 2 - Evidencia nacional: quais bases foram utilizadas

Tempo sugerido: 1 minuto e 20 segundos.

> Para entender se esse problema tinha relevancia alem de uma percepcao
> isolada, nos partimos de bases publicas e oficiais.
>
> A primeira fonte foi o IBGE, com a estimativa municipal de populacao de
> 2025, cuja referencia e primeiro de julho de 2025. Essa base informa o
> tamanho estimado da populacao de cada municipio. Ela foi importante porque
> permite comparar territorios de tamanhos diferentes de forma proporcional.
>
> A segunda fonte foi o cadastro vinculado do SISAB, do Ministerio da Saude,
> na competencia de dezembro de 2024. Essa base foi usada como um sinal
> aproximado de vinculo da populacao com a APS.
>
> A terceira fonte foi a base de indicadores de desempenho do SISAB, ligada ao
> periodo do Previne Brasil, no terceiro quadrimestre de 2024. Ela trouxe
> dados agregados sobre acompanhamento preventivo, como condicoes cronicas e
> pre-natal.
>
> Tambem analisamos a base de UBS, atualizada no portal em julho de 2026, e a
> base de hospitais e leitos, com competencia de maio de 2026. Elas ajudaram a
> entender o contexto mais amplo da oferta de saude no territorio.
>
> Nesta tela, os numeros mostram a escala da analise: 5.571 municipios,
> aproximadamente 213,4 milhoes de habitantes na estimativa de 2025 e 100.242
> registros brutos de indicadores processados. Esses dados nao representam
> pessoas individualmente. Eles representam sinais agregados que merecem ser
> investigados pela gestao local.

**[AVANCE]**

## Slide 3 - Da base bruta a uma oportunidade de gestao

Tempo sugerido: 1 minuto e 20 segundos.

> Depois de reunir as fontes, realizamos a analise em etapas simples.
>
> Primeiro, organizamos os dados por municipio, para que informacoes de fontes
> diferentes pudessem ser comparadas no mesmo territorio. Depois, usamos a
> estimativa populacional do IBGE para calcular proporcoes e evitar que um
> municipio grande parecesse comparavel a um municipio pequeno apenas pelo
> numero absoluto de registros.
>
> Em seguida, observamos tres grupos de sinais: vinculo aproximado com a APS,
> indicadores preventivos e disponibilidade territorial de UBS. Na analise
> exploratoria, tambem olhamos leitos para compreender o contexto mais amplo
> da rede. Os leitos nao fazem parte da regra do MVP; eles ajudaram apenas a
> entender a oportunidade inicial.
>
> Encontramos um vinculo aproximado de 38,11% da populacao na APS. Entre os
> municipios com mais de 20 mil habitantes, identificamos 1.091 com vinculo
> abaixo de 50%. Tambem encontramos 276 municipios nessa faixa populacional
> com media de indicadores abaixo de 40, em uma escala de zero a cem.
>
> A conclusao responsavel nao e que esses municipios possuem um problema
> clinico provado. A conclusao e que existem sinais territoriais que podem
> justificar uma verificacao local e uma melhor organizacao da busca ativa.
> Os volumes exibidos neste slide representam registros processados, e nao
> qualidade do atendimento ou quantidade de pessoas atendidas.

**[AVANCE]**

## Slide 4 - Dashboard: uma prioridade que pode ser explicada

Tempo sugerido: 1 minuto e 15 segundos.

> Agora eu apresento como o SUS Conecta transforma essa ideia em uma rotina
> simples de gestao.
>
> Os territorios deste painel sao demonstrativos. Jardim Esperanca, Parque das
> Flores, Vila Nova e Centro nao representam municipios reais nem pacientes
> reais. Eles foram criados apenas para demonstrar a decisao de forma segura.
>
> Jardim Esperanca aparece como prioridade alta por tres motivos visiveis.
> Primeiro, possui 42% de populacao vinculada a APS, abaixo da referencia de
> 50%. Segundo, apresenta 32% no indicador de condicoes cronicas, abaixo da
> meta de 60%. Terceiro, possui 72% no acompanhamento pre-natal, abaixo da
> meta de 85%.
>
> A regra e transparente. Se o vinculo esta abaixo da meta e pelo menos um
> indicador preventivo tambem esta abaixo da sua meta, o territorio recebe
> prioridade alta. Se apenas um sinal esta abaixo, a prioridade e media. Se os
> sinais atendem as referencias, a prioridade e baixa.
>
> Isso nao e uma classificacao de risco clinico. E uma fila de trabalho para a
> coordenacao saber onde pode ser mais util iniciar uma conversa com a equipe e
> organizar uma acao preventiva.

**[AVANCE]**

## Slide 5 - Da prioridade para a acao acompanhavel

Tempo sugerido: 1 minuto.

> Priorizar, por si so, nao resolve o problema. A prioridade precisa virar uma
> acao que a equipe consiga executar e acompanhar.
>
> No exemplo de Jardim Esperanca, a coordenacao cria uma acao focada em
> reconectar pessoas ao acompanhamento preventivo de condicoes cronicas. A
> acao recebe uma equipe responsavel, um prazo de sete dias e uma meta agregada
> de 80 contatos.
>
> A equipe registra que realizou 54 contatos. Por isso, o painel mostra um
> progresso de 67,5% da meta. O que esta sendo acompanhado e a execucao da
> acao territorial, e nao a vida clinica ou o prontuario de cada pessoa.
>
> Existe tambem uma regra simples de qualidade: uma acao nao pode ser marcada
> como concluida se nenhum resultado foi registrado. Isso evita que a gestao
> confunda uma acao planejada com uma acao efetivamente executada.
>
> Os pequenos numeros no rodape deste slide sao apenas evidencias de que o
> fluxo foi validado. Nao e necessario le-los na gravacao. A mensagem
> importante e que a solucao acompanha a acao do inicio ate o retorno ao
> painel.

**[AVANCE]**

## Slide 6 - Como a solucao funciona, sem complexidade desnecessaria

Tempo sugerido: 55 segundos.

> O funcionamento do SUS Conecta pode ser resumido em cinco etapas.
>
> Primeiro, entram dados agregados por territorio: vinculo, competencia dos
> dados e indicadores preventivos.
>
> Segundo, uma regra clara classifica o territorio como alta, media ou baixa
> prioridade e mostra os motivos dessa classificacao.
>
> Terceiro, a coordenacao enxerga a fila de territorios e decide qual merece
> uma acao primeiro.
>
> Quarto, a equipe recebe uma acao com foco, responsavel, prazo e meta.
>
> Quinto, o progresso volta para o painel em forma agregada, permitindo que a
> coordenacao enxergue o que esta aberto, em andamento, concluido ou proximo
> do prazo.
>
> O ponto central e que a decisao humana continua no centro. O sistema nao
> toma uma decisao clinica. Ele organiza a informacao para que a equipe tenha
> mais clareza antes de agir.

**[AVANCE]**

## Slide 7 - Valor para o SUS e para a populacao

Tempo sugerido: 55 segundos.

> O valor da proposta aparece em quatro frentes.
>
> Para a gestao da APS, o SUS Conecta reduz a necessidade de reunir varias
> planilhas antes de decidir. Em vez de apenas observar muitos numeros, a
> coordenacao recebe uma fila territorial explicavel.
>
> Para as equipes de UBS e Estrategia Saude da Familia, a solucao transforma
> uma lacuna percebida em uma acao com foco, meta, prazo e responsavel claros.
>
> Para a populacao, a hipotese de valor e uma rede mais atenta a continuidade
> preventiva, sem expor dados pessoais no mecanismo de priorizacao.
>
> E para o SUS, a proposta mostra uma forma de transformar dados agregados em
> coordenacao local, respeitando limites de interpretacao e a responsabilidade
> da equipe de saude.
>
> Nos nao prometemos evitar internacoes, prever agravamentos ou provar um
> impacto clinico. Entregamos uma forma mais clara de explicar, organizar e
> acompanhar uma decisao territorial.

**[AVANCE]**

## Slide 8 - Encerramento

Tempo sugerido: 40 segundos.

> Para encerrar, o SUS Conecta transforma sinal em acao e acao em
> acompanhamento.
>
> O MVP demonstra que e possivel identificar um territorio prioritario com
> regras claras, explicar o motivo da prioridade, criar uma acao preventiva e
> acompanhar sua execucao de forma agregada.
>
> O proximo passo nao e ampliar a complexidade. E validar a utilidade dessa
> leitura territorial com coordenadores de APS e equipes de UBS: ela torna a
> preparacao da busca ativa mais rapida, mais clara e mais util para a rotina?
>
> Porque cuidar melhor tambem e conseguir enxergar, com antecedencia, onde a
> rede precisa se organizar primeiro.
>
> Muito obrigado.

## Respostas curtas para perguntas da banca

### O sistema identifica pacientes em risco?

> Nao. A prioridade e sempre territorial ou por UBS. O MVP nao armazena nome,
> CPF, endereco, prontuario, diagnostico ou risco clinico individual.

### Como voces definiram a prioridade?

> A prioridade usa uma regra explicavel: vinculo abaixo da meta mais pelo
> menos um indicador preventivo abaixo da meta resulta em prioridade alta. O
> painel sempre mostra quais sinais levaram a essa classificacao.

### Voces conseguem provar que a solucao reduz internacoes?

> Ainda nao. Essa nao e uma afirmacao do projeto. O MVP demonstra uma forma de
> organizar e acompanhar a busca ativa territorial. Impacto assistencial exige
> validacao posterior com gestores, equipes e indicadores definidos localmente.

### Por que nao integrar imediatamente com prontuarios ou sistemas do SUS?

> Porque o objetivo desta fase e validar uma rotina simples, demonstravel e
> segura. O MVP funciona com dados agregados e nao depende de dados pessoais.
> Qualquer integracao futura deve ser discutida com a rede local, considerando
> utilidade, governanca e privacidade.

## Frases a evitar na gravacao

- "O sistema diagnostica pessoas."
- "O sistema preve agravamento clinico."
- "O sistema evita internacoes."
- "Os dados nacionais mostram a situacao atual de cada UBS."

Prefira dizer: "apoia uma priorizacao territorial", "mostra um sinal agregado
para investigacao" e "acompanha a execucao da busca ativa".
