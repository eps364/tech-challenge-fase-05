# Analise exploratoria - Hospitais e Leitos SUS 2026

Data de acesso: 2026-07-18

## Objetivo

Investigar, com dados publicos oficiais, sinais de desigualdade de capacidade hospitalar SUS que possam indicar oportunidades de melhoria no atendimento, regulacao, transparencia operacional ou planejamento de capacidade. Esta analise nao escolhe ainda o problema final nem propoe solucao fechada.

## Bases utilizadas

- Ministerio da Saude / Portal de Dados Abertos do SUS: Hospitais e Leitos, recurso Leitos 2026 CSV.
- IBGE / SIDRA: Censo Demografico 2022, tabela 4714, variavel 93, populacao residente por municipio e UF.
- IBGE / API de Localidades: lista de estados para compatibilizar codigos e siglas.

## Escopo dos dados

- Competencias encontradas no CSV de leitos: 202601, 202602, 202603, 202604, 202605.
- Competencia analisada: 202605 (maior competencia disponivel no arquivo).
- Populacao usada como denominador: Censo 2022.
- Abrangencia geografica: Brasil, UFs e municipios.

## Indicadores principais

- Populacao 2022 considerada: 203.080.756 pessoas.
- Estabelecimentos hospitalares com registro no arquivo de leitos: 7.183.
- Leitos SUS: 353.520.
- UTI SUS: 32.022.
- Leitos SUS por 10 mil habitantes: 17,41.
- UTI SUS por 100 mil habitantes: 15,77.
- Municipios com 50 mil habitantes ou mais e zero leitos SUS no arquivo: 12.
- Municipios com 100 mil habitantes ou mais e zero UTI SUS no arquivo: 40.

## UFs com menor taxa de leitos SUS por 10 mil habitantes

| uf | uf_nome | populacao_2022 | leitos_sus | leitos_sus_por_10k_hab |
| --- | --- | --- | --- | --- |
| SP | São Paulo | 44.411.238 | 61.991 | 13,96 |
| SE | Sergipe | 2.210.004 | 3.189 | 14,43 |
| RJ | Rio de Janeiro | 16.055.174 | 24.166 | 15,05 |
| AM | Amazonas | 3.941.613 | 6.174 | 15,66 |
| PA | Pará | 8.120.131 | 12.857 | 15,83 |
| MG | Minas Gerais | 20.539.989 | 33.808 | 16,46 |
| MS | Mato Grosso do Sul | 2.757.013 | 4.543 | 16,48 |
| ES | Espírito Santo | 3.833.712 | 6.701 | 17,48 |
| SC | Santa Catarina | 7.610.361 | 13.324 | 17,51 |
| GO | Goiás | 7.056.495 | 13.049 | 18,49 |

## UFs com menor taxa de UTI SUS por 100 mil habitantes

| uf | uf_nome | populacao_2022 | uti_total_sus | uti_sus_por_100k_hab |
| --- | --- | --- | --- | --- |
| AC | Acre | 830.018 | 81 | 9,76 |
| PI | Piauí | 3.271.199 | 334 | 10,21 |
| AP | Amapá | 733.759 | 79 | 10,77 |
| AM | Amazonas | 3.941.613 | 441 | 11,19 |
| MA | Maranhão | 6.776.699 | 808 | 11,92 |
| BA | Bahia | 14.141.626 | 1.694 | 11,98 |
| PA | Pará | 8.120.131 | 1.005 | 12,38 |
| SE | Sergipe | 2.210.004 | 290 | 13,12 |
| MT | Mato Grosso | 3.658.649 | 484 | 13,23 |
| TO | Tocantins | 1.511.460 | 202 | 13,36 |

## Comparacao regional

| regiao | populacao_2022 | leitos_sus | leitos_sus_por_10k_hab | uti_total_sus | uti_sus_por_100k_hab |
| --- | --- | --- | --- | --- | --- |
| SUDESTE | 84.840.113 | 126.666 | 14,93 | 14.329 | 16,89 |
| NORTE | 17.354.884 | 31.453 | 18,12 | 2.198 | 12,67 |
| CENTRO-OESTE | 16.289.538 | 30.036 | 18,44 | 2.626 | 16,12 |
| SUL | 29.937.706 | 58.201 | 19,44 | 5.545 | 18,52 |
| NORDESTE | 54.658.515 | 107.164 | 19,61 | 7.324 | 13,40 |

## Municipios priorizados pela heuristica de oportunidade

| uf | municipio | populacao_2022 | leitos_sus | leitos_sus_por_10k_hab | uti_total_sus | uti_sus_por_100k_hab | sinais_de_oportunidade | score_heuristico_oportunidade |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| SC | Palhoça - SC | 222.598 | 0 | 0,00 | 0 | 0,00 | municipio_50k_mais_sem_leitos_sus;municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional;baixa_participacao_sus_na_capacidade_existente | 17 |
| PR | Almirante Tamandaré - PR | 119.825 | 0 | 0,00 | 0 | 0,00 | municipio_50k_mais_sem_leitos_sus;municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 15 |
| SP | Jandira - SP | 118.045 | 0 | 0,00 | 0 | 0,00 | municipio_50k_mais_sem_leitos_sus;municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 15 |
| SP | Poá - SP | 103.765 | 0 | 0,00 | 0 | 0,00 | municipio_50k_mais_sem_leitos_sus;municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 15 |
| PE | Camaragibe - PE | 147.771 | 41 | 2,77 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional;baixa_participacao_sus_na_capacidade_existente | 12 |
| SP | Ribeirão Pires - SP | 115.559 | 43 | 3,72 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional;baixa_participacao_sus_na_capacidade_existente | 12 |
| RJ | Belford Roxo - RJ | 483.087 | 359 | 7,43 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| PR | Colombo - PR | 232.212 | 39 | 1,68 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| MA | São José de Ribamar - MA | 244.579 | 77 | 3,15 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| GO | Valparaíso de Goiás - GO | 198.861 | 23 | 1,16 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| GO | Águas Lindas de Goiás - GO | 225.693 | 91 | 4,03 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| SE | Nossa Senhora do Socorro - SE | 192.330 | 57 | 2,96 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| MA | Paço do Lumiar - MA | 145.643 | 26 | 1,79 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| SP | Santana de Parnaíba - SP | 154.105 | 74 | 4,80 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| MG | Vespasiano - MG | 129.246 | 48 | 3,71 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |

## Interpretacao inicial

- Fato/dado: a distribuicao de leitos SUS e UTI SUS por habitante varia bastante entre UFs e municipios.
- Interpretacao: localidades com populacao relevante, baixa taxa per capita e/ou ausencia de UTI SUS podem representar gargalos de acesso, regulacao, referenciamento ou transparencia para pacientes e gestores.
- Hipotese: uma solucao focada em visibilidade de capacidade, priorizacao ou apoio ao encaminhamento poderia gerar valor onde ha maior assimetria entre demanda populacional e oferta SUS registrada.
- Limitacao: a base de leitos mede capacidade cadastrada, nao mede fila, tempo de espera, ocupacao em tempo real, qualidade assistencial ou deslocamento efetivo do paciente.
- Risco de interpretacao: baixa taxa de leitos em um municipio pode ser compensada por rede regional de referencia; portanto, a analise municipal precisa ser cruzada com regioes de saude e fluxos de atendimento antes de virar conclusao.

## Arquivos gerados

- data/processed/leitos_uf_indicadores_2026.csv
- data/processed/leitos_regiao_indicadores_2026.csv
- data/processed/leitos_municipio_indicadores_2026.csv
- data/processed/leitos_oportunidades_municipio_2026.csv
- analytics/reports/analise_leitos_sus_2026.html
