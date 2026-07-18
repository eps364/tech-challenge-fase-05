# Especificacao de requisitos - MVP de gestao ativa na APS

Data: 2026-07-18

## 1. Decisao de escopo

Este documento especifica um MVP para apoiar a **gestao territorial de busca ativa na Atencao Primaria a Saude (APS)**. O sistema nao pretende gerenciar toda a rede SUS, substituir prontuario, regular vagas ou diagnosticar pacientes.

O foco e responder a uma pergunta operacional simples:

> Em quais territorios a equipe gestora deve concentrar primeiro uma acao de busca ativa preventiva, e por qual motivo?

O recorte foi escolhido porque a analise inicial encontrou municipios com baixo vinculo na APS e desempenho inferior em indicadores de acompanhamento. Esses sinais indicam uma oportunidade de priorizacao, mas nao permitem concluir causa clinica, qualidade individual do atendimento ou situacao de um paciente especifico.

## 2. Problema a resolver

Coordenadores de APS precisam decidir onde concentrar busca ativa e acompanhamento preventivo. Quando os indicadores estao dispersos, a priorizacao depende de planilhas, consultas manuais e percepcao individual. Com tempo e equipe limitados, isso dificulta escolher o territorio que deve receber atencao primeiro e verificar se a acao realizada teve efeito.

## 3. Objetivo do MVP

Permitir que um coordenador de APS:

1. visualize os territorios/UBS sob sua responsabilidade ordenados por prioridade;
2. compreenda, de forma explicavel, os indicadores que justificam cada prioridade;
3. registre uma acao de busca ativa territorial; e
4. acompanhe se a acao foi planejada, realizada ou concluida.

O resultado esperado e uma rotina de priorizacao mais clara e rastreavel para atividades preventivas. O sistema apoia decisao administrativa; a decisao clinica e a abordagem aos usuarios continuam sob responsabilidade da equipe de saude.

## 3.1. Caso tangivel: Joao e a oportunidade de busca ativa

Joao, personagem ficticio de 58 anos, mora em um territorio atendido por uma UBS. Ha tres anos, em uma consulta, foram identificadas hipertensao e diabetes. Ele iniciou o acompanhamento, mas deixou de comparecer periodicamente por uma combinacao de mudancas em sua rotina, dificuldades de acesso e falta de uma acao de retorno organizada. O caso nao atribui culpa a Joao nem a equipe: ele ilustra uma lacuna de acompanhamento territorial.

Tres anos depois, Joao procura uma UPA com pressao muito elevada e mal-estar. Ele recebe atendimento de urgencia e pode precisar de internacao. Nao e correto afirmar que uma busca ativa teria evitado esse desfecho. A oportunidade esta antes da urgencia: identificar que muitas pessoas de um mesmo territorio podem ter perdido o acompanhamento preventivo e organizar uma acao de retorno.

No MVP, o sistema nao guarda o prontuario de Joao e nao decide seu tratamento. Ele mostra ao coordenador que o territorio da UBS apresenta baixo vinculo na APS e indicador fragil de acompanhamento de condicoes cronicas. A coordenacao cria uma acao territorial, por exemplo: "Busca ativa de pessoas com condicoes cronicas sem acompanhamento recente", define uma equipe, prazo e meta de contatos.

A equipe utiliza os canais e registros autorizados que ja possui para realizar a acao. No sistema, registra somente o resultado agregado, como "meta de 80 contatos; 54 realizados; 31 pessoas reconectadas ao acompanhamento". Joao representa uma pessoa que pode ser alcancada por essa iniciativa, mas o produto resolve a decisao de priorizar e acompanhar a acao no territorio, nao o tratamento individual de Joao.

## 4. Usuario e contexto de uso

### Usuario principal

**Coordenador(a) municipal de APS ou coordenador(a) de UBS.**

Essa pessoa acompanha indicadores de cobertura e cuidado, organiza atividades das equipes e precisa escolher onde concentrar esforco de busca ativa no curto prazo.

### Usuario secundario

**Equipe de Estrategia Saude da Familia (ESF) / agente comunitario de saude**, que consulta as acoes territoriais definidas pela coordenacao e atualiza sua execucao de forma agregada.

### Contexto

O MVP trabalha com uma cidade demonstrativa e suas UBS ou territorios de referencia. Os dados podem ser oficiais agregados, tratados previamente, e dados simulados coerentes com esses indicadores para a demonstracao. O MVP nao usara prontuarios, CPF, nome, endereco ou qualquer identificador de paciente.

## 5. Escopo funcional

### 5.1. Consulta de prioridades territoriais

O sistema deve apresentar uma lista de territorios ou UBS ordenada por prioridade de busca ativa.

Cada item da lista deve mostrar, no minimo:

- nome do territorio ou UBS;
- nivel de prioridade: alta, media ou baixa;
- percentual de populacao vinculada na APS;
- indicador preventivo com maior necessidade de atencao;
- quantidade de acoes abertas naquele territorio.

O usuario deve poder filtrar a lista por nivel de prioridade e por indicador preventivo.

### 5.2. Explicacao da prioridade

Ao abrir um territorio, o sistema deve explicar por que ele foi priorizado.

O detalhamento deve mostrar:

- valor atual de vinculo/cobertura usado no MVP;
- indicadores preventivos considerados;
- metas ou faixas de referencia configuradas;
- regra que levou ao nivel de prioridade;
- historico resumido de acoes abertas e concluidas no territorio.

O sistema nao deve apresentar o resultado como diagnostico, previsao clinica ou certeza de risco individual.

### 5.3. Criacao de acao de busca ativa

O coordenador deve poder registrar uma nova acao territorial contendo:

- territorio ou UBS alvo;
- indicador ou foco preventivo da acao;
- objetivo da acao;
- periodo planejado;
- equipe responsavel;
- meta agregada de contatos/atendimentos a realizar;
- observacao opcional.

Exemplos de foco preventivo para demonstracao: acompanhamento de gestantes, vacinacao infantil, pessoas com hipertensao ou diabetes e exame citopatologico. A lista final deve conter apenas os focos representados pelos dados demonstrativos do sistema.

### 5.4. Acompanhamento da acao

Para cada acao, o usuario deve poder registrar:

- situacao: planejada, em andamento, concluida ou cancelada;
- quantidade agregada de contatos/atendimentos realizados;
- data de atualizacao;
- observacao de resultado.

O sistema deve exibir o progresso comparando a quantidade realizada com a meta planejada. Nenhum registro individual de pessoa atendida e necessario para o MVP.

### 5.5. Painel de acompanhamento

O painel inicial deve exibir, no minimo:

- quantidade de territorios em alta prioridade;
- quantidade de acoes planejadas ou em andamento;
- quantidade de acoes concluidas no periodo selecionado;
- lista das cinco prioridades atuais;
- resumo das acoes que estao vencidas ou proximas do termino.

### 5.6. Base demonstrativa e atualizacao

O sistema deve disponibilizar dados demonstrativos para permitir uma apresentacao ponta a ponta sem depender de sistemas externos em tempo real.

O operador deve conseguir carregar ou substituir um conjunto agregado de indicadores territoriais. A atualizacao deve informar a competencia dos dados carregados e validar campos obrigatorios antes de disponibilizar o resultado.

## 6. Regras de negocio

### RN01 - Unidade de priorizacao

A unidade de priorizacao do MVP sera o territorio ou UBS, nunca um paciente individual.

### RN02 - Prioridade explicavel

Toda prioridade deve ser produzida por regras simples e visiveis. A classificacao deve informar quais indicadores contribuiram para o resultado.

### RN03 - Regra inicial de prioridade

A classificacao inicial deve considerar somente dois sinais agregados:

- baixo vinculo/cobertura da populacao na APS; e
- desempenho abaixo da faixa de referencia em pelo menos um indicador preventivo.

Regra sugerida para demonstracao:

| Nivel | Condicao |
| --- | --- |
| Alta | Vinculo abaixo da meta e ao menos um indicador preventivo abaixo da meta |
| Media | Apenas um dos dois sinais esta abaixo da meta |
| Baixa | Os dois sinais estao na meta ou acima dela |

As metas devem ser parametrizaveis pelo gestor demonstrativo. O sistema deve guardar quais metas estavam vigentes quando uma prioridade foi calculada.

### RN04 - Acao obrigatoriamente vinculada

Toda acao de busca ativa deve estar vinculada a um territorio/UBS e a um foco preventivo.

### RN05 - Medicao agregada

O progresso de uma acao e medido apenas pela quantidade agregada realizada em relacao a sua meta. Isso nao comprova melhora clinica ou impacto causal no indicador; apenas informa a execucao da acao.

### RN06 - Encerramento

Uma acao so pode ser marcada como concluida quando possuir uma quantidade realizada registrada. A quantidade realizada nao pode ser negativa.

## 7. Requisitos funcionais

| ID | Requisito | Prioridade |
| --- | --- | --- |
| RF01 | Exibir painel com resumo de prioridades e acoes de busca ativa. | Essencial |
| RF02 | Listar territorios/UBS ordenados por nivel de prioridade. | Essencial |
| RF03 | Filtrar territorios por nivel de prioridade e foco preventivo. | Essencial |
| RF04 | Exibir explicacao dos indicadores e regras de cada prioridade. | Essencial |
| RF05 | Permitir criar uma acao territorial de busca ativa. | Essencial |
| RF06 | Permitir atualizar situacao, quantidade realizada e observacao de uma acao. | Essencial |
| RF07 | Mostrar progresso de cada acao em relacao a sua meta. | Essencial |
| RF08 | Destacar acoes proximas do prazo ou vencidas. | Importante |
| RF09 | Permitir carregar conjunto agregado de indicadores demonstrativos. | Importante |
| RF10 | Permitir alterar metas usadas na classificacao de prioridade. | Desejavel |
| RF11 | Exportar relatorio ou lista de acoes. | Fora do MVP |
| RF12 | Notificar usuarios por e-mail, WhatsApp ou SMS. | Fora do MVP |

## 8. Requisitos nao funcionais e restricoes

| ID | Requisito/Restricao |
| --- | --- |
| RNF01 | A demonstracao deve permitir concluir o fluxo principal sem dependencia de acesso a uma fonte externa em tempo real. |
| RNF02 | A interface deve usar linguagem simples, apresentar prioridade com texto e nao depender apenas de cor. |
| RNF03 | O sistema deve registrar a data de competencia/origem dos indicadores exibidos. |
| RNF04 | O MVP deve trabalhar exclusivamente com dados agregados ou simulados, sem dados pessoais ou clinicos identificaveis. |
| RNF05 | O sistema deve deixar claro que os dados sao apoio a decisao e possuem limitacoes de atualizacao e cobertura. |
| RNF06 | A operacao principal deve ser demonstravel em poucos passos e em menos de oito minutos no video do MVP. |
| RNF07 | O sistema nao deve afirmar que uma acao evitou internacao, melhorou desfecho clinico ou causou alteracao de indicador sem uma avaliacao apropriada. |

## 9. Fluxo principal de demonstracao

1. O coordenador acessa o painel e identifica quantos territorios estao em alta prioridade.
2. Ele abre o territorio no topo da lista e entende que a prioridade decorre de baixo vinculo e de um indicador preventivo abaixo da meta.
3. Ele cria uma acao de busca ativa para aquele territorio, com foco, equipe, prazo e meta agregada.
4. A equipe atualiza a acao com quantidade realizada e muda sua situacao para em andamento ou concluida.
5. O coordenador retorna ao painel e acompanha a evolucao operacional da acao.

## 10. Criterios de aceite do fluxo principal

- Dado um conjunto de territorios com indicadores agregados, quando o painel for aberto, entao os territorios devem aparecer ordenados por prioridade.
- Dado um territorio em alta prioridade, quando o usuario abrir seus detalhes, entao deve ver os dois ou mais sinais que justificam a classificacao e as metas usadas.
- Dado um territorio selecionado, quando o coordenador preencher os campos obrigatorios de uma nova acao, entao a acao deve ser criada com situacao planejada.
- Dada uma acao planejada, quando a equipe registrar quantidade realizada e situacao em andamento, entao o progresso deve ser recalculado e exibido.
- Dada uma acao marcada como concluida, quando nao houver quantidade realizada registrada, entao o sistema deve impedir o encerramento e informar o campo necessario.
- Dado um filtro de prioridade ou foco preventivo, quando o usuario aplica-lo, entao a lista deve mostrar apenas os territorios correspondentes.

## 11. Fora de escopo explicito

Para manter o MVP pequeno e demonstravel, nao fazem parte desta versao:

- prontuario eletronico, cadastro individual ou historico clinico;
- agenda de consultas e agendamento de pacientes;
- envio de mensagens ou contato automatico com cidadaos;
- prescricoes, protocolos clinicos ou recomendacao medica;
- integracao em tempo real com e-SUS APS, SISAB, CNES, hospital, central de regulacao ou prontuarios;
- controle de fila, leitos, ambulancias ou encaminhamento especializado;
- geolocalizacao de pacientes;
- gestao regional entre municipios;
- medicao de impacto clinico ou financeiro.

## 12. Dados e limites de interpretacao

O contexto inicial usa dados oficiais agregados de UBS/CNES, SISAB e IBGE, processados para identificar sinais de baixo vinculo e desempenho preventivo. Para o MVP, esses dados devem ser tratados como insumo territorial e ter sua competencia informada.

As bases analisadas nao medem diretamente agenda disponivel, qualidade do atendimento, ausencia do paciente, prontuario, risco individual ou fila de espera. Por isso, o sistema deve apresentar priorizacao como uma hipotese operacional para o gestor validar com sua equipe, e nao como uma verdade automatica.

## 13. Hipoteses a validar com usuarios

- Coordenadores de APS realmente gastam tempo consolidando indicadores antes de decidir onde realizar busca ativa.
- A explicacao da prioridade por territorio e mais util do que uma lista geral de indicadores municipais.
- O registro agregado de acoes e suficiente para o primeiro ciclo de acompanhamento.
- Os focos preventivos selecionados correspondem a atividades que a equipe consegue organizar no periodo definido.

## 14. Indicadores de sucesso do MVP

Durante a demonstracao ou validacao inicial, o MVP sera considerado util se o usuario conseguir:

- identificar o territorio prioritario sem consultar planilhas externas;
- explicar por que aquele territorio foi selecionado;
- criar uma acao de busca ativa em poucos passos;
- verificar o andamento da acao depois de uma atualizacao;
- reconhecer os limites dos dados usados na decisao.

Esses indicadores avaliam clareza e operacao do produto. Eles nao representam, ainda, uma comprovacao de impacto assistencial real.
