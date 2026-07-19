# Analise exploratoria - Hospitais e Leitos SUS 2026

Data de acesso: 2026-07-18

## Objetivo

Investigar, com dados publicos oficiais, sinais de desigualdade de capacidade hospitalar SUS que possam indicar oportunidades de melhoria no atendimento, regulacao, transparencia operacional ou planejamento de capacidade. Esta analise nao escolhe ainda o problema final nem propoe solucao fechada.

## Bases utilizadas

- Ministerio da Saude / Portal de Dados Abertos do SUS: Hospitais e Leitos, recurso Leitos 2026 CSV.
- IBGE: Estimativas da Populacao Residente para municipios, referencia em 01/07/2025, revisadas em 13/01/2026.
- IBGE / API de Localidades: lista de estados para compatibilizar codigos e siglas.

## Escopo dos dados

- Competencias encontradas no CSV de leitos: 202601, 202602, 202603, 202604, 202605.
- Competencia analisada: 202605 (maior competencia disponivel no arquivo).
- Populacao usada como denominador: estimativa municipal IBGE com referencia em 01/07/2025.
- Abrangencia geografica: Brasil, UFs e municipios.

## Indicadores principais

- Populacao 2025 considerada: 213.421.037 pessoas.
- Estabelecimentos hospitalares com registro no arquivo de leitos: 7.183.
- Leitos SUS: 353.520.
- UTI SUS: 32.022.
- Leitos SUS por 10 mil habitantes: 16,56.
- UTI SUS por 100 mil habitantes: 15,00.
- Municipios com 50 mil habitantes ou mais e zero leitos SUS no arquivo: 14.
- Municipios com 100 mil habitantes ou mais e zero UTI SUS no arquivo: 47.

## UFs com menor taxa de leitos SUS por 10 mil habitantes

| uf | uf_nome | populacao_2025 | leitos_sus | leitos_sus_por_10k_hab |
| --- | --- | --- | --- | --- |
| SP | São Paulo | 46.081.801 | 61.991 | 13,45 |
| SE | Sergipe | 2.299.425 | 3.189 | 13,87 |
| RJ | Rio de Janeiro | 17.223.547 | 24.166 | 14,03 |
| AM | Amazonas | 4.321.616 | 6.174 | 14,29 |
| PA | Pará | 8.711.196 | 12.857 | 14,76 |
| MS | Mato Grosso do Sul | 2.924.631 | 4.543 | 15,53 |
| MG | Minas Gerais | 21.393.441 | 33.808 | 15,80 |
| ES | Espírito Santo | 4.126.854 | 6.701 | 16,24 |
| SC | Santa Catarina | 8.187.029 | 13.324 | 16,27 |
| MT | Mato Grosso | 3.893.659 | 6.807 | 17,48 |

## UFs com menor taxa de UTI SUS por 100 mil habitantes

| uf | uf_nome | populacao_2025 | uti_total_sus | uti_sus_por_100k_hab |
| --- | --- | --- | --- | --- |
| AC | Acre | 884.372 | 81 | 9,16 |
| AP | Amapá | 806.517 | 79 | 9,80 |
| PI | Piauí | 3.384.547 | 334 | 9,87 |
| AM | Amazonas | 4.321.616 | 441 | 10,20 |
| BA | Bahia | 14.870.907 | 1.694 | 11,39 |
| MA | Maranhão | 7.018.211 | 808 | 11,51 |
| PA | Pará | 8.711.196 | 1.005 | 11,54 |
| RR | Roraima | 738.772 | 90 | 12,18 |
| MT | Mato Grosso | 3.893.659 | 484 | 12,43 |
| SE | Sergipe | 2.299.425 | 290 | 12,61 |

## Comparacao regional

| regiao | populacao_2025 | leitos_sus | leitos_sus_por_10k_hab | uti_total_sus | uti_sus_por_100k_hab |
| --- | --- | --- | --- | --- | --- |
| SUDESTE | 88.825.643 | 126.666 | 14,26 | 14.329 | 16,13 |
| NORTE | 18.801.282 | 31.453 | 16,73 | 2.198 | 11,69 |
| CENTRO-OESTE | 17.238.818 | 30.036 | 17,42 | 2.626 | 15,23 |
| SUL | 31.310.809 | 58.201 | 18,59 | 5.545 | 17,71 |
| NORDESTE | 57.244.485 | 107.164 | 18,72 | 7.324 | 12,79 |

## Municipios priorizados pela heuristica de oportunidade

| uf | municipio | populacao_2025 | leitos_sus | leitos_sus_por_10k_hab | uti_total_sus | uti_sus_por_100k_hab | sinais_de_oportunidade | score_heuristico_oportunidade |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| SC | Palhoça | 253.469 | 0 | 0,00 | 0 | 0,00 | municipio_50k_mais_sem_leitos_sus;municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional;baixa_participacao_sus_na_capacidade_existente | 17 |
| PR | Almirante Tamandaré | 125.861 | 0 | 0,00 | 0 | 0,00 | municipio_50k_mais_sem_leitos_sus;municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 15 |
| SP | Jandira | 121.550 | 0 | 0,00 | 0 | 0,00 | municipio_50k_mais_sem_leitos_sus;municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 15 |
| SP | Poá | 106.355 | 0 | 0,00 | 0 | 0,00 | municipio_50k_mais_sem_leitos_sus;municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 15 |
| PE | Camaragibe | 156.112 | 41 | 2,63 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional;baixa_participacao_sus_na_capacidade_existente | 12 |
| PE | Abreu e Lima | 104.248 | 4 | 0,38 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional;baixa_participacao_sus_na_capacidade_existente | 12 |
| SP | Ribeirão Pires | 118.954 | 43 | 3,61 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional;baixa_participacao_sus_na_capacidade_existente | 12 |
| RJ | Belford Roxo | 518.384 | 359 | 6,93 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| PR | Colombo | 241.672 | 39 | 1,61 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| MA | São José de Ribamar | 259.164 | 77 | 2,97 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| GO | Valparaíso de Goiás | 218.416 | 23 | 1,05 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| GO | Águas Lindas de Goiás | 245.352 | 91 | 3,71 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| SE | Nossa Senhora do Socorro | 204.081 | 57 | 2,79 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| MA | Paço do Lumiar | 153.158 | 26 | 1,70 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |
| SP | Santana de Parnaíba | 163.787 | 74 | 4,52 | 0 | 0,00 | municipio_100k_mais_sem_uti_sus;leitos_sus_por_10k_abaixo_de_50pct_media_nacional;uti_sus_por_100k_abaixo_de_50pct_media_nacional | 10 |

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
