# Roteiro de regravacao do pitch - SUS Conecta

Roteiro sincronizado com `apresentacao_aps.html`.

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

> Ola, eu sou Luiz Saraiva. O SUS Conecta foi desenvolvido em conjunto por mim
> e por Emerson Silva para a Fase 5 do Tech Challenge.
>
> Nosso ponto de partida foi uma pergunta operacional da Atencao Primaria:
> qual territorio deve receber primeiro uma acao de busca ativa preventiva, e
> por que?
>
> Na rotina, a coordenacao precisa comparar varios territorios, indicadores e
> planilhas, enquanto as equipes possuem capacidade limitada.
>
> A proposta transforma dados publicos e agregados em uma fila territorial
> explicavel e em um fluxo simples para planejar, executar e acompanhar a
> acao. Ela nao identifica pacientes e nao substitui a decisao da equipe.

**[AVANCE]**

---

## Slide 2 - Evidencia e tipos de dados

**Tempo-alvo: 0:45 a 1:40**

> Para verificar se o problema tinha escala, usamos dados oficiais e agregados:
> populacao municipal do IBGE, cadastros vinculados e indicadores preventivos
> do SISAB, alem de dados de UBS, estabelecimentos e leitos do SUS.
>
> O recorte combina a estimativa do IBGE de 2025, o cadastro do SISAB de
> dezembro de 2024 e os indicadores do terceiro quadrimestre de 2024. UBS,
> estabelecimentos e leitos serviram como contexto; leitos nao entram na regra
> de prioridade.
>
> Os numeros deste slide sao reais e foram calculados a partir dessas bases:
> analisamos 5.571 municipios, cerca de 213,4 milhoes de habitantes e 100.242
> registros de indicadores. A media ponderada ficou em 47,34, numa escala de
> zero a cem.
>
> Como as fontes possuem competencias diferentes, esse e um retrato
> exploratorio, nao uma fotografia sincronizada do SUS. Ele nao usa dados de
> pacientes nem mede risco clinico individual.

**[AVANCE]**

---

## Slide 3 - Oportunidade encontrada

**Tempo-alvo: 1:40 a 2:35**

> Combinamos as bases por codigo de municipio, normalizamos os percentuais e
> aplicamos filtros reproduziveis. Relacionamos populacao e cadastros para obter
> uma aproximacao de vinculo e consolidamos os indicadores preventivos. Os dados
> de UBS deram contexto, mas nao alteraram a classificacao do MVP.
>
> A aproximacao nacional de vinculo ficou em 38,11%. Entre os municipios com
> pelo menos 20 mil habitantes, 1.091 ficaram abaixo de 50% nessa aproximacao.
> No mesmo recorte populacional, 276 tiveram media inferior a 40% nos
> indicadores preventivos analisados.
>
> Os volumes representam registros processados, nao pessoas atendidas, e o
> vinculo e uma aproximacao historica, nao uma taxa oficial em tempo real.
>
> Esses resultados indicam onde vale investigar e organizar uma resposta local.
> Nao provam causalidade, nao avaliam a qualidade do cuidado e nao substituem a
> decisao da equipe de saude.

**[AVANCE]**

---

## Slide 4 - Produto e regra de priorizacao

**Tempo-alvo: 2:35 a 3:40**

> Aqui comeca a demonstracao do produto. Jardim Esperanca, Parque das Flores,
> Vila Nova e Centro sao territorios ficticios. Os nomes de UBS, percentuais,
> metas e equipes tambem foram simulados; nenhuma unidade real e representada.
>
> A classificacao segue uma regra objetiva. Vinculo baixo junto com pelo menos
> um indicador abaixo da meta gera prioridade alta. Apenas um desses sinais gera
> prioridade media. Quando todos atingem as referencias, a prioridade e baixa.
>
> No exemplo, a populacao vinculada esta em 42% para uma meta de 50%. O
> indicador de condicoes cronicas esta em 32% para uma meta de 60%, e o
> acompanhamento pre-natal esta em 72% para uma meta de 85%. Como ha vinculo
> baixo e indicadores preventivos abaixo das metas, Jardim Esperanca recebe
> prioridade alta. O painel explica cada motivo, em vez de exibir apenas uma
> cor.
>
> A regra e simples, auditavel e serve como apoio: a coordenacao continua sendo
> responsavel pela decisao final.

**[AVANCE]**

---

## Slide 5 - Acao e evidencias tecnicas

**Tempo-alvo: 3:40 a 4:40**

> Depois de selecionar uma prioridade, a equipe registra uma acao com
> territorio, indicador, responsavel, prazo e meta. No exemplo, o foco e
> acompanhamento de condicoes cronicas, a equipe responsavel e uma ESF e o
> prazo operacional e de sete dias.
>
> Os valores de 54 contatos realizados para uma meta de 80 tambem sao
> ficticios. O que e real neste slide e a comprovacao tecnica: a API criou a
> acao com HTTP 201, persistiu o resultado, atualizou o progresso e rejeitou com
> HTTP 422 uma tentativa invalida de concluir a acao sem informar o realizado.
>
> O progresso de 67,5% descreve somente a execucao operacional. Ele nao significa
> melhora clinica. Nos testes automatizados, o servico atingiu 98,94% de
> cobertura de linhas, incluindo o fluxo HTTP com PostgreSQL.
>
> Assim, o MVP conecta priorizacao, execucao e acompanhamento sem perder a
> explicabilidade.

**[AVANCE]**

---

## Slide 6 - Fluxo e arquitetura

**Tempo-alvo: 4:40 a 5:30**

> O fluxo possui cinco etapas: carregar o cenario agregado, calcular lacunas,
> classificar e ordenar territorios, registrar a acao e acompanhar sua
> execucao. A prioridade e recalculada a cada consulta com seus motivos.
>
> A aplicacao foi construida em Java 21 e Spring Boot, seguindo Clean
> Architecture. O nucleo de dominio permanece independente de HTTP, banco de
> dados e frameworks. PostgreSQL e Flyway cuidam da persistencia, e a API
> documenta os contratos de entrada, saida e erro.
>
> Os containers sobem o servico e o banco para a demonstracao, sem depender de
> prontuario ou integracao externa em tempo real.

**[AVANCE]**

---

## Slide 7 - Diferencial e impacto esperado

**Tempo-alvo: 5:30 a 6:20**

> O diferencial nao e apenas mostrar indicadores. O SUS Conecta transforma a
> lacuna em uma proxima acao rastreavel: onde atuar, por qual motivo, com qual
> meta, em qual prazo e com qual resultado.
>
> Para a gestao, isso reduz a consolidacao manual e torna a prioridade
> comparavel. Para as equipes de UBS, diminui a ambiguidade entre perceber uma
> lacuna e receber uma acao executavel. Para o SUS, oferece uma rotina com
> limites de interpretacao claros. A populacao pode se beneficiar de maior
> continuidade preventiva sem exposicao de dados pessoais.
>
> O impacto apresentado e uma hipotese mensuravel. Esperamos reduzir o tempo
> gasto na priorizacao e aumentar a proporcao de acoes concluidas com resultado
> registrado. Em uma etapa futura, um piloto controlado poderia comparar esses
> indicadores antes e depois da adocao.
>
> Ainda nao afirmamos ganho assistencial ou impacto causal.

**[AVANCE]**

---

## Slide 8 - Encerramento

**Tempo-alvo: 6:20 a 6:50**

> Os proximos passos sao integrar fontes autorizadas, executar um piloto com
> coordenadores, avaliar o tempo de decisao e evoluir as metas de acordo com a
> realidade local. Qualquer integracao futura devera preservar governanca,
> privacidade e a finalidade territorial.
>
> Em resumo, o SUS Conecta usa dados agregados para tornar a priorizacao da
> busca ativa mais clara, justificavel e acompanhavel, sem expor dados
> individuais e sem substituir a decisao humana.
>
> Obrigado.

---

## Frases curtas para responder a banca

### Os dados sao reais?

> As bases e os numeros nacionais da analise sao reais, publicos e agregados.
> Os territorios e as acoes exibidos na demonstracao sao ficticios.

### O sistema prioriza pacientes?

> Nao. Ele prioriza territorios ou unidades por lacunas agregadas. Nao existe
> score individual, prontuario ou decisao clinica.

### O score usa inteligencia artificial?

> Nao nesta versao. A regra e deterministica e auditavel: prioridade e funcao
> da lacuna entre o indicador e a meta operacional.

### O produto prova impacto na saude?

> Nao. O MVP demonstra viabilidade tecnica e uma hipotese de ganho operacional.
> Impacto assistencial exige validacao em piloto controlado.

### Por que as competencias das bases sao diferentes?

> Usamos as publicacoes oficiais disponiveis e documentamos a competencia de
> cada fonte. Por isso, a analise e exploratoria e nao um retrato em tempo real.

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
