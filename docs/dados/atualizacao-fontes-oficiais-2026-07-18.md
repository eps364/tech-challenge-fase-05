# Atualizacao de fontes oficiais e reanalise

Data da validacao: 2026-07-18

## Decisao sobre populacao

Nao existe um Censo Demografico 2026 publicado pelo IBGE. O Censo de mesma
natureza mais recente continua sendo o de 2022. Para atualizar os
denominadores municipais sem inventar uma fonte inexistente, esta rodada usa a
fonte oficial mais recente disponivel: as Estimativas da Populacao 2025 do
IBGE, com referencia em 01/07/2025 e revisao divulgada em 13/01/2026.

Fonte oficial: https://www.ibge.gov.br/estatisticas/sociais/populacao/9103-estimativas-de-populacao.html

Arquivos preservados:

- `data/raw/ibge_estimativa_populacao_municipio_2025.ods`: arquivo usado pelo processamento, SHA-256 `33dc6f79def9522e282cd69b87a9ce75327a81239d6060d9c8f9f5a49bd2a1b5`.
- `data/raw/ibge_estimativa_populacao_municipio_2025.xls`: copia publicada em formato alternativo, SHA-256 `af63966d2ca32b285fac81994e243b822c096f3ad51106adc7689b6f74cabd05`.

## Auditoria das demais fontes

| Base | Fonte oficial | Competencia mais recente localizada | Decisao |
| --- | --- | --- | --- |
| UBS | Portal de Dados Abertos do SUS | Arquivo atualizado em julho/2026 | Mantida; ja e a versao atual disponivel. |
| Hospitais e Leitos | Portal de Dados Abertos do SUS | Leitos ate `202605` | Mantida; o arquivo de 2026 possui competencias de janeiro a maio. |
| Cadastro Vinculado SISAB | Portal de Dados Abertos do SUS | `202412` | Mantida como serie historica; nao ha competencia posterior publicada nesse conjunto. |
| Indicadores SISAB/Previne | Portal de Dados Abertos do SUS | `2024Q3` | Mantida como serie historica; o conjunto cobre o periodo do Previne Brasil e nao retrata indicadores assistenciais atuais. |

As paginas oficiais consultadas foram:

- https://dadosabertos.saude.gov.br/dataset/unidades-basicas-de-saude-ubs
- https://dadosabertos.saude.gov.br/dataset/hospitais-e-leitos
- https://dadosabertos.saude.gov.br/dataset/cadastro_vinculado_sisab
- https://dadosabertos.saude.gov.br/dataset/indicadores_desempenho_sisab

## Efeito da reanalise

| Indicador | Base anterior, populacao 2022 | Base atual, estimativa 2025 |
| --- | ---: | ---: |
| Municipios considerados | 5.570 | 5.571 |
| Populacao usada nos denominadores | 203.080.756 | 213.421.037 |
| UBS por 10 mil habitantes | 2,36 | 2,24 |
| Leitos SUS por 10 mil habitantes | 17,41 | 16,56 |
| UTI SUS por 100 mil habitantes | 15,77 | 15,00 |
| Municipios com sinal integrado | 645 | 654 |
| Municipios em alta prioridade heuristica | 214 | 221 |

Os arquivos processados passaram a usar o campo `populacao_2025`. A nova lista
inclui 5.571 municipios; Boa Esperanca do Norte (MT), criado depois da base
historica de 2022 usada anteriormente, tem estimativa de 5.877 habitantes e
ainda nao possui correspondencia nas series SISAB de 2024. Isso e tratado como
lacuna temporal, nao como desempenho zero.

## Limites de interpretacao

- Os cruzamentos usam dados territoriais agregados, sem dados pessoais ou prontuarios.
- As competencias nao sao simultaneas: populacao 2025, leitos maio/2026, UBS julho/2026 e SISAB 2024.
- A atualizacao melhora a comparabilidade dos denominadores, mas nao transforma os indicadores historicos do SISAB em medida atual da qualidade assistencial.
- Os resultados apontam sinais para validacao com a gestao local; nao provam causalidade, fila, ocupacao hospitalar ou risco clinico individual.
