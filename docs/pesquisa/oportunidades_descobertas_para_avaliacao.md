# Oportunidades descobertas para avaliacao da dupla

Data: 2026-07-18

Este documento resume, de forma direta, as oportunidades descobertas a partir das bases oficiais analisadas com Python. Ele nao escolhe a solucao final; serve para a dupla avaliar qual dor vale validar com usuarios reais.

## Resumo executivo

A oportunidade mais forte encontrada nao e simplesmente "faltam leitos" ou "faltam UBS". O padrao mais relevante e uma falha de coordenacao entre tres pontos do atendimento:

1. Atencao Primaria a Saude, onde o paciente deveria ser acompanhado cedo.
2. Regulacao e encaminhamento, onde o paciente precisa ser direcionado para o servico correto.
3. Retaguarda hospitalar, onde a rede precisa ter leitos/UTI suficientes ou referencia regional clara.

O cruzamento dos dados mostrou municipios onde esses sinais aparecem juntos: baixa densidade de UBS, baixo vinculo APS, baixo desempenho em indicadores de cuidado e baixa disponibilidade relativa de leitos/UTI SUS.

## Oportunidade 1 - Priorizacao integrada APS, regulacao e hospital

Problema observado:

Gestores e reguladores podem ter dificuldade para enxergar, em uma unica visao, quais municipios ou territorios combinam fragilidade na porta de entrada com baixa retaguarda hospitalar.

Evidencia encontrada:

- 654 municipios apresentaram algum sinal integrado no cruzamento APS + leitos.
- 221 municipios ficaram em alta prioridade heuristica.
- Esses 221 municipios representam 61.899.822 habitantes.
- 468 municipios combinam APS fragil com leitos SUS baixos, representando 48.281.213 habitantes.

Oportunidade de melhoria:

Criar um apoio de decisao que ajude gestores a priorizar territorios, referencias, pactuacoes, encaminhamentos, reforco preventivo e alocacao de recursos onde APS e retaguarda hospitalar estao pressionadas ao mesmo tempo.

Por que parece forte para o hackathon:

- Usa dados oficiais.
- Tem impacto social claro.
- Permite MVP factivel com painel, score territorial e explicabilidade.
- Nao depende, no primeiro MVP, de dados sensiveis de pacientes.

## Oportunidade 2 - Gestao ativa de carteira na APS

Problema observado:

Equipes de UBS podem ter dificuldade para identificar quem ou onde priorizar quando ha baixo vinculo populacional e baixo desempenho de acompanhamento.

Evidencia encontrada:

- Percentual aproximado nacional de populacao vinculada na APS: 38,11%.
- 1.091 municipios com 20 mil habitantes ou mais aparecem com vinculo APS abaixo de 50%.
- 276 municipios com 20 mil habitantes ou mais aparecem com media dos indicadores Previne abaixo de 40.

Oportunidade de melhoria:

Apoiar busca ativa e acompanhamento de carteira, priorizando territorios com sinais de baixo vinculo, baixo acompanhamento preventivo ou indicadores historicos sensiveis a continuidade do cuidado.

Casos de uso possiveis:

- Identificar municipios/territorios com baixo vinculo APS.
- Sugerir prioridades para agentes comunitarios e equipes de UBS.
- Alertar baixa cobertura de indicadores como gestantes, vacinacao, citopatologico e diabetes.

## Oportunidade 3 - Validacao de oferta cadastrada e mapa CNES

Problema observado:

Alguns municipios aparecem com zero UBS, zero leitos SUS ou zero UTI SUS nas bases analisadas. Isso pode significar falta real de oferta local, dependencia de rede regional ou problema/incompletude cadastral.

Evidencia encontrada:

- 56 municipios com 5 mil habitantes ou mais aparecem com zero UBS no arquivo de UBS.
- Na analise atualizada de leitos, 14 municipios com 50 mil habitantes ou mais apareceram com zero leitos SUS.
- Na analise atualizada de leitos, 47 municipios com 100 mil habitantes ou mais apareceram com zero UTI SUS.

Oportunidade de melhoria:

Criar uma ferramenta de validacao e transparencia de oferta, separando:

- ausencia real de servico local;
- dependencia de municipio vizinho ou referencia regional;
- possivel inconsistencia cadastral;
- necessidade de revisao pelo gestor.

## Oportunidade 4 - Monitoramento territorial preventivo

Problema observado:

Quando indicadores historicos de APS estao baixos e a retaguarda de UTI tambem aparece critica, o territorio precisa de investigacao local e pode demandar organizacao preventiva da rede.

Evidencia encontrada:

- 28 municipios combinam desempenho APS baixo com UTI SUS critica.
- A populacao nesses municipios soma 6.201.548 habitantes.

Oportunidade de melhoria:

Criar uma fila territorial para validacao e acao preventiva: busca ativa, teleorientacao, priorizacao de grupos definidos pela equipe local e articulacao da referencia quando necessario. O dado agregado nao classifica risco clinico individual nem prediz agravamento.

## Oportunidade 5 - Planejamento regional e pactuacao entre municipios

Problema observado:

Um municipio pode nao ter leito, UTI ou estrutura suficiente localmente, mas isso nao prova falta de atendimento se houver referencia regional bem organizada. A dor pode estar na falta de visibilidade dessa dependencia.

Evidencia encontrada:

- O cruzamento mostra varios municipios grandes com baixa oferta local relativa e baixo desempenho APS.
- A analise de leitos mostra variacao importante entre UFs e municipios.

Oportunidade de melhoria:

Apoiar secretarias municipais/regionais na pactuacao de rede: para onde encaminhar, quais municipios dependem de outros, quais territorios precisam de reforco preventivo e onde investir primeiro.

## Municipios que apareceram no topo do cruzamento integrado

- Rio das Ostras - RJ.
- Santana de Parnaiba - SP.
- Jandira - SP.
- Sao Joao de Meriti - RJ.
- Hortolandia - SP.
- Paranagua - PR.
- Ribeirao Pires - SP.
- Cariacica - ES.
- Salto - SP.
- Duque de Caxias - RJ.

Esses nomes nao devem ser tratados como conclusao final sobre qualidade do atendimento. Eles sao pontos de investigacao porque concentraram sinais nas bases analisadas.

## Recomendacao para avaliacao

Minha leitura e que a dupla deveria avaliar primeiro a oportunidade 1:

"Como ajudar gestores do SUS a priorizar municipios/territorios onde a APS esta fragil e a retaguarda hospitalar tambem esta pressionada?"

Essa dor conversa bem com o tema do hackathon porque conecta acesso, eficiencia, dados oficiais, planejamento e impacto no atendimento. Tambem permite um MVP enxuto: score territorial, mapa/painel, explicacao dos motivos de prioridade e recomendacoes operacionais.

## Cuidados de interpretacao

- Os dados nao medem fila real, tempo de espera, absenteismo, ocupacao hospitalar em tempo real, deslocamento ou satisfacao do paciente.
- Os indicadores do Previne Brasil sao historicos e foram afetados por mudancas no financiamento da APS.
- Zero UBS, zero leito ou zero UTI em arquivo oficial deve ser validado com CNES e gestores locais.
- A populacao vem da estimativa municipal IBGE de 2025; as demais bases usam competencias diferentes.
- A analise aponta oportunidades e hipoteses, nao prova causalidade.

## Arquivos de apoio

- `analytics/reports/sintese_oportunidades_integradas_sus.md`
- `analytics/reports/analise_aps_sus.md`
- `analytics/reports/analise_leitos_sus_2026.md`
- `docs/pesquisa/sintese_oportunidades_aps_sus.md`
- `docs/pesquisa/sintese_oportunidades_leitos_sus.md`
- `docs/pesquisa/matriz_problemas_candidatos.md`
