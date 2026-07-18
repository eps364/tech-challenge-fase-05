# Sintese complementar - APS e oportunidades integradas SUS

Data: 2026-07-18

## Bases analisadas

- Ministerio da Saude: Unidades Basicas de Saude - UBS, arquivo CSV atualizado em julho/2026.
- Ministerio da Saude / SISAB: Cadastro Vinculado do Programa Previne Brasil, competencia `202412`.
- Ministerio da Saude / SISAB: Indicadores de Desempenho do Programa Previne Brasil, quadrimestre `2024Q3`.
- Ministerio da Saude: Hospitais e Leitos, competencia `202605`, ja analisada na etapa anterior.
- IBGE: Censo Demografico 2022, populacao residente por municipio.

## Fatos e dados encontrados

- Foram analisados 47.839 registros brutos de UBS, 46.086 registros de cadastro vinculado e 100.242 registros de indicadores de desempenho.
- Foram avaliados 5.570 municipios, com populacao total de 203.080.756 habitantes no Censo 2022.
- Media nacional calculada: 2,36 UBS por 10 mil habitantes.
- 56 municipios com 5 mil habitantes ou mais aparecem com zero UBS no arquivo analisado.
- Percentual aproximado nacional de populacao vinculada na APS: 38,11%.
- Media ponderada nacional dos indicadores Previne analisados: 47,38 em escala de 0 a 100.
- 1.071 municipios com 20 mil habitantes ou mais aparecem com vinculo APS abaixo de 50%.
- 273 municipios com 20 mil habitantes ou mais aparecem com media dos indicadores Previne abaixo de 40.
- No cruzamento APS + leitos, 645 municipios apresentaram algum sinal integrado.
- 214 municipios ficaram em alta prioridade heuristica, representando 58.607.042 habitantes.
- 459 municipios combinaram APS fragil com leitos SUS baixos, representando 45.418.673 habitantes.
- 25 municipios combinaram desempenho APS baixo com UTI SUS critica, representando 5.390.795 habitantes.

## Oportunidades identificadas

### 1. Regulacao integrada APS e retaguarda hospitalar

Problema candidato: gestores e reguladores precisam enxergar, no mesmo painel, onde a APS esta fragil e a retaguarda hospitalar tambem esta limitada.

Evidencia: 459 municipios aparecem com sinais de APS fragil e baixa disponibilidade relativa de leitos SUS.

Hipotese de valor: uma ferramenta de priorizacao poderia orientar encaminhamentos, pactuacao regional, abertura temporaria de agenda, teleconsultoria, transporte sanitario ou reforco preventivo antes do agravamento dos casos.

### 2. Gestao ativa de carteira na APS

Problema candidato: equipes de UBS podem ter dificuldade para identificar grupos e territorios com baixo vinculo e baixo acompanhamento.

Evidencia: 1.071 municipios com 20 mil habitantes ou mais aparecem com vinculo APS abaixo de 50%, e 273 aparecem com media Previne abaixo de 40.

Hipotese de valor: um motor simples de busca ativa poderia priorizar pacientes/territorios para cadastro, acompanhamento de gestantes, vacinacao, citopatologico, diabetes e outros indicadores sensiveis a acompanhamento longitudinal.

### 3. Mapa de oferta e qualidade cadastral CNES

Problema candidato: municipios com zero UBS, zero leitos ou zero UTI no arquivo exigem validacao antes de qualquer conclusao operacional.

Evidencia: 56 municipios de 5 mil habitantes ou mais aparecem com zero UBS no arquivo; na etapa de leitos, 12 municipios de 50 mil habitantes ou mais apareceram com zero leitos SUS e 40 municipios de 100 mil habitantes ou mais com zero UTI SUS.

Hipotese de valor: um validador de oferta cadastrada poderia apontar inconsistencias, dependencia regional e lacunas de transparencia para gestores e populacao.

### 4. Monitoramento preventivo para evitar agravamento

Problema candidato: baixo desempenho em APS pode antecipar maior risco de agravamento e demanda evitavel por urgencia/hospital.

Evidencia: 25 municipios combinam desempenho APS baixo com UTI SUS critica no cruzamento.

Hipotese de valor: alertas de risco territorial poderiam apoiar cuidado precoce, acompanhamento remoto, roteiros de agentes comunitarios e comunicacao ativa com pacientes.

### 5. Planejamento regional e pactuacao entre municipios

Problema candidato: a ausencia de oferta local pode ser compensada por rede regional, mas isso exige visibilidade de fluxos, referencias e capacidade.

Evidencia: varios municipios priorizados no cruzamento possuem baixa oferta local de leitos/UTI, o que nao necessariamente significa falta de atendimento, mas aumenta a dependencia de referencia regional.

Hipotese de valor: uma camada regional de decisao pode ajudar secretarias a negociar pactuacoes, transporte, referencia e distribuicao de investimento.

## Municipios que apareceram no topo do cruzamento

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

## Leitura para o hackathon

A oportunidade mais forte neste momento nao parece ser apenas "mais leitos" ou "mais UBS". O padrao mais defensavel e uma dor de coordenacao: identificar territorios onde a porta de entrada esta fragil, o acompanhamento preventivo esta baixo e a retaguarda hospitalar tambem esta pressionada.

Isso abre espaco para um MVP de apoio a decisao e priorizacao operacional, desde que a solucao final ainda seja validada com usuario real: gestor de APS, regulador, coordenador de UBS ou secretaria municipal/regional.

## Limites

- As bases nao medem fila, tempo de espera, absenteismo, ocupacao hospitalar em tempo real, deslocamento real, qualidade clinica ou satisfacao do paciente.
- Indicadores do Previne Brasil sao historicos e foram afetados pela mudanca do modelo de financiamento da APS.
- Zero unidade/leito/UTI em arquivo oficial deve ser tratado como sinal para validacao cadastral e regional, nao como conclusao isolada.
- As competencias das bases sao diferentes: populacao 2022, SISAB 2024, leitos 202605 e UBS julho/2026.

## Arquivos gerados

- `analytics/reports/analise_aps_sus.md`
- `analytics/reports/analise_aps_sus.html`
- `analytics/reports/sintese_oportunidades_integradas_sus.md`
- `analytics/reports/sintese_oportunidades_integradas_sus.html`
- `data/processed/aps_municipio_indicadores.csv`
- `data/processed/aps_uf_indicadores.csv`
- `data/processed/aps_regiao_indicadores.csv`
- `data/processed/aps_oportunidades_municipio.csv`
- `data/processed/oportunidades_integradas_municipio.csv`
- `data/processed/oportunidades_integradas_uf.csv`
