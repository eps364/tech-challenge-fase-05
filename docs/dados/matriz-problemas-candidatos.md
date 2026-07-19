# Matriz inicial de problemas candidatos

Data: 2026-07-18

Escala: 0 a 5, onde 5 e mais favoravel para o projeto. Esta matriz e inicial e nao define ainda o problema final.

## Candidatos

| Problema candidato | Usuario afetado | Evidencia disponivel | Dados oficiais possiveis | Impacto social | Viabilidade MVP | Inovacao possivel | Mensuracao | Risco/limitacao principal | Nota inicial |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | --- | ---: |
| Fragilidade integrada entre APS, regulacao e retaguarda hospitalar | Gestor de APS, regulador, secretaria municipal/regional, paciente referenciado | Cruzamento Python com UBS, SISAB, Hospitais e Leitos e IBGE sinalizou 654 municipios; 221 de alta prioridade | UBS/CNES, SISAB, Hospitais e Leitos, IBGE, regioes de saude | 5 | 4 | 5 | 5 | Nao mede fila real, ocupacao em tempo real nem fluxo de referencia | 4,8 |
| Desigualdade de oferta de leitos SUS e UTI SUS por populacao | Gestor municipal/regional, regulador, paciente referenciado | Analise Python ja iniciada com Hospitais e Leitos + IBGE | Hospitais e Leitos, CNES, IBGE, regioes de saude | 5 | 4 | 4 | 5 | Capacidade cadastrada nao prova fila, ocupacao ou necessidade real | 4,4 |
| Municipios populosos sem UTI SUS ou com baixa taxa de UTI SUS | Regulador, gestor, paciente grave | Analise Python sinalizou municipios 100k+ sem UTI SUS no arquivo | Hospitais e Leitos, CNES, IBGE, SIH/SUS, regioes de saude | 5 | 4 | 4 | 5 | Rede regional pode compensar ausencia municipal | 4,4 |
| Baixa transparencia sobre capacidade hospitalar regional | Gestor, regulador, profissional administrativo | Parcial, inferida pela necessidade de cruzar bases estaticas | Hospitais e Leitos, CNES, ocupacao hospitalar quando disponivel | 4 | 4 | 4 | 4 | Dados de ocupacao geral em tempo real podem nao estar publicos | 4,0 |
| Distribuicao desigual de UBS por populacao e territorio | Paciente, gestor de APS, agente comunitario | Analise Python encontrou media nacional de 2,24 UBS/10 mil hab. e 56 municipios 5k+ com zero UBS no arquivo | UBS/CNES, IBGE, regioes de saude | 5 | 4 | 3 | 4 | Quantidade de UBS nao mede qualidade, equipe ou agenda disponivel | 4,2 |
| Acompanhamento insuficiente de indicadores da atencao primaria | Gestor de APS, equipe de UBS, paciente cronico | Analise Python encontrou 1.091 municipios 20k+ com vinculo APS < 50% e 276 municipios 20k+ com media Previne < 40 | Previne Brasil indicadores, cadastro vinculado, SISAB/e-Gestor quando acessivel | 5 | 4 | 4 | 5 | Mudancas de modelo de financiamento/indicadores podem afetar comparabilidade | 4,5 |
| Falta de visibilidade de medicamentos e estoque | Paciente, farmaceutico, gestor | Fonte BNAFAR identificada, ainda nao explorada | BNAFAR, assistencia farmaceutica, dados municipais quando disponiveis | 5 | 3 | 4 | 4 | Dados de estoque podem ter granularidade, atraso ou cobertura incompleta | 3,8 |
| Preparacao insuficiente para picos sazonais de demanda respiratoria | Gestor, triagem, regulador, UPA/hospital | Fonte SRAG identificada, ainda nao analisada | SRAG 2019-2026, CNES, leitos, IBGE | 4 | 3 | 4 | 4 | SRAG mede agravo especifico, nao demanda total de urgencia | 3,8 |
| Falta de apoio a encaminhamento entre UBS, especialistas e hospitais | Paciente, medico de UBS, regulador | Problema plausivel, mas falta dado direto de fila/regulacao | CNES, SIA/SUS, SIH/SUS, dados municipais/estaduais de regulacao se disponiveis | 5 | 3 | 4 | 3 | Filas e encaminhamentos podem nao estar em dados abertos nacionais | 3,6 |

## Leitura inicial

- Mais promissor neste momento: fragilidade integrada entre APS, regulacao e retaguarda hospitalar.
- Melhor equilibrio atual entre evidencia, dados e MVP: um apoio de priorizacao operacional para municipios/territorios onde APS e retaguarda aparecem pressionadas ao mesmo tempo.
- Mais promissor em valor percebido pelo paciente, mas com maior dependencia de dados externos: encaminhamento/regulacao de filas.
- Mais promissor para saude preventiva: indicadores da atencao primaria.

## Hipoteses ainda nao validadas

- Municipios com baixa taxa de leitos SUS por habitante podem gerar maior dependencia de deslocamento ou referencia regional.
- Municipios populosos sem UTI SUS podem exigir melhor visibilidade de encaminhamento e capacidade regional.
- A baixa participacao SUS em leitos existentes pode indicar oportunidade de transparencia ou planejamento contratual, mas isso nao prova disponibilidade real para atendimento SUS.
- A distribuicao de UBS por populacao pode revelar gargalos de acesso primario, mas quantidade de unidade nao mede agenda, equipe, qualidade ou resolutividade.
- Municipios com baixo vinculo APS e baixo desempenho Previne podem se beneficiar de gestao ativa de carteira, busca ativa e priorizacao territorial.
- Onde APS fragil e retaguarda hospitalar baixa se sobrepoem, a dor pode estar menos em "um sistema de cadastro" e mais em coordenacao operacional entre prevencao, encaminhamento e capacidade regional.

## Proximo passo recomendado

Antes de escolher o problema final, validar com usuario real uma destas dores:

- Priorizacao de territorios/pacientes na APS.
- Visibilidade de gargalos entre UBS, regulacao e hospital.
- Validacao cadastral e mapa de oferta local/regional.
- Entrevistas curtas com profissional/gestor para validar se a dor e de fila, visibilidade, regulacao, comunicacao ou planejamento.
