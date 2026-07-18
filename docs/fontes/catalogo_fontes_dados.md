# Catalogo inicial de fontes oficiais

Data de acesso: 2026-07-18

## Fontes usadas na primeira analise Python

### Hospitais e Leitos

- Orgao responsavel: Ministerio da Saude / Portal de Dados Abertos do SUS.
- Pagina do conjunto: https://dadosabertos.saude.gov.br/dataset/hospitais-e-leitos
- Recurso baixado: https://s3.sa-east-1.amazonaws.com/ckan.saude.gov.br/Leitos_SUS/Leitos_csv_2026.zip
- Pagina do recurso: https://dadosabertos.saude.gov.br/dataset/hospitais-e-leitos/resource/5ac78b13-649f-4b09-8a92-0ae829a56d50
- Formato: CSV compactado em ZIP.
- Arquivo preservado: `data/raw/leitos_sus_2026_csv.zip`
- SHA-256 local: `f213c2c7cc4d7391b24b4e33bfca8fd145ff76a2a1f602e09e852f9c2d213f53`
- Periodo encontrado no CSV: competencias `202601`, `202602`, `202603`, `202604`, `202605`.
- Competencia analisada: `202605`, maior competencia disponivel no arquivo.
- Abrangencia geografica: nacional, por municipio, UF e regiao.
- Variaveis principais usadas: `COMP`, `REGIAO`, `UF`, `CO_IBGE`, `MUNICIPIO`, `CNES`, `DS_TIPO_UNIDADE`, `LEITOS_EXISTENTES`, `LEITOS_SUS`, `UTI_TOTAL_EXIST`, `UTI_TOTAL_SUS`.
- Periodicidade indicada pelo portal: mensal.
- Limitacoes declaradas pelo portal: os dados dependem da forma de captura e envio pelos gestores locais de saude; a base representa registros agregados e nao deve permitir rastreio ou identificacao de cidadaos.
- Uso nesta fase: investigar desigualdade territorial de leitos SUS e UTI SUS por populacao.

### IBGE - Censo Demografico 2022 / SIDRA

- Orgao responsavel: IBGE.
- Pagina do Censo 2022: https://www.ibge.gov.br/estatisticas/sociais/populacao/22827-censo-demografico-2022.html?edicao=37225&t=resultados
- Endpoint UF usado: https://apisidra.ibge.gov.br/values/t/4714/n3/all/v/93/p/2022
- Endpoint municipio usado: https://apisidra.ibge.gov.br/values/t/4714/n6/all/v/93/p/2022
- Tabela: 4714.
- Variavel: 93, populacao residente.
- Periodo: 2022.
- Arquivos preservados: `data/raw/ibge_populacao_uf_2022_tabela_4714.json` e `data/raw/ibge_populacao_municipio_2022_tabela_4714.json`.
- Uso nesta fase: criar denominadores populacionais para indicadores proporcionais.
- Limitacao: a populacao e de 2022, enquanto os leitos analisados sao de maio/2026; os indicadores sao aproximacoes comparativas, nao taxas oficiais finalisticas.

### IBGE - API de Localidades

- Orgao responsavel: IBGE.
- Endpoint usado: https://servicodados.ibge.gov.br/api/v1/localidades/estados
- Arquivo preservado: `data/raw/ibge_localidades_estados.json`
- Uso nesta fase: compatibilizar codigo, sigla e regiao das UFs.

### Unidades Basicas de Saude - UBS

- Orgao responsavel: Ministerio da Saude / Portal de Dados Abertos do SUS.
- Pagina do conjunto: https://dadosabertos.saude.gov.br/dataset/unidades-basicas-de-saude-ubs
- Recurso baixado: https://s3.sa-east-1.amazonaws.com/ckan.saude.gov.br/CNES/Unidades_Basicas_Saude-UBS_csv.zip
- Formato: CSV compactado em ZIP.
- Arquivo preservado: `data/raw/unidades_basicas_saude_ubs_csv.zip`
- SHA-256 local: `776a925ac32bd984db2453c365d18728d6643fd5ecfa893c3059243e5fdb3ac2`
- Atualizacao indicada no portal: julho/2026.
- Variaveis principais usadas: `CNES`, `UF`, `IBGE`, `NOME`, `LOGRADOURO`, `BAIRRO`, `LATITUDE`, `LONGITUDE`.
- Uso nesta fase: medir densidade de UBS por municipio e populacao.
- Limitacao: quantidade de UBS cadastrada nao mede equipe ativa, agenda disponivel, qualidade ou resolutividade.

### SISAB - Cadastro Vinculado do Programa Previne Brasil

- Orgao responsavel: Ministerio da Saude / Secretaria de Atencao Primaria a Saude.
- Pagina do conjunto: https://dadosabertos.saude.gov.br/dataset/cadastro_vinculado_sisab
- Recurso baixado: https://s3.sa-east-1.amazonaws.com/ckan.saude.gov.br/sisab/cadastro_vinculado/csv/sisab_cadastro_vinculado_202412.csv.zip
- Formato: CSV compactado em ZIP.
- Arquivo preservado: `data/raw/sisab_cadastro_vinculado_202412_csv.zip`
- SHA-256 local: `b2440cf898f1b8e0ebdcd40659240e7ffd410336371c1a6e87d3ac0b92de33bb`
- Competencia analisada: `202412`.
- Variaveis principais usadas: `competencia`, `uf`, `ibge`, `municipio`, `populacao`, `tipo_equipe`, `condicao_equipe`, `criterio_ponderacao`, `qt_pessoas_vinculadas`.
- Uso nesta fase: estimar percentual aproximado de populacao vinculada na APS por municipio.
- Limitacao: a metrica depende de regras do Previne Brasil, que foi descontinuado; deve ser usada como historico/proxy operacional.

### SISAB - Indicadores de Desempenho do Programa Previne Brasil

- Orgao responsavel: Ministerio da Saude / Secretaria de Atencao Primaria a Saude.
- Pagina do conjunto: https://dadosabertos.saude.gov.br/dataset/indicadores_desempenho_sisab
- Recurso baixado: https://s3.sa-east-1.amazonaws.com/ckan.saude.gov.br/sisab/indicador_desempenho/csv/sisab_indicador_desempenho_2024Q3.csv.zip
- Formato: CSV compactado em ZIP.
- Arquivo preservado: `data/raw/sisab_indicador_desempenho_2024Q3_csv.zip`
- SHA-256 local: `66feadf3e161ee4e7712b505acd793f9eb96e69960ffe0adf2ddaa23936a8017`
- Quadrimestre analisado: `2024Q3`.
- Campo percentual usado: `vl_perc_quad`, por ficar na escala 0 a 100 para comparacao entre indicadores.
- Indicadores encontrados no arquivo: pre-natal, sifilis/HIV em gestantes, atendimento odontologico em gestantes, citopatologico, vacinacao infantil e diabetes/hemoglobina glicada.
- Uso nesta fase: sinalizar municipios com baixo desempenho agregado nos indicadores historicos de APS.
- Limitacao: os indicadores nao explicam a causa do baixo desempenho e nao substituem dados locais de atendimento, agenda ou qualidade clinica.

## Portais e bases oficiais promissoras para proximas analises

### Portal de Dados Abertos do SUS

- URL: https://dadosabertos.saude.gov.br/
- API: https://apidadosabertos.saude.gov.br/
- Swagger: https://apidadosabertos.saude.gov.br/static/swagger.json
- Uso potencial: bases em CSV, JSON, XML e API para assistencia a saude, atencao primaria, CNES, SISVAN, BNAFAR e vigilancia.

### CNES - Cadastro Nacional de Estabelecimentos de Saude

- Pagina: https://dadosabertos.saude.gov.br/dataset/cnes-cadastro-nacional-de-estabelecimentos-de-saude
- Formatos indicados: API, CSV, JSON, XML.
- Atualizacao indicada no portal: diaria.
- Uso potencial: mapear oferta de estabelecimentos, tipo de unidade, status e estrutura disponivel.
- Problemas investigaveis: falta de oferta local, distribuicao de tipos de unidade, referencia regional.

### Unidades Basicas de Saude - UBS

- Pagina: https://dadosabertos.saude.gov.br/dataset/unidades-basicas-de-saude-ubs
- API indicada: `/assistencia-a-saude/unidade-basicas-de-saude`
- Uso potencial: medir distribuicao de UBS por populacao e territorio.
- Problemas investigaveis: acesso a atencao primaria, deslocamento, cobertura territorial, concentracao de UBS.

### Previne Brasil - Indicadores de desempenho

- API indicada no Swagger: `/atencao-primaria/indicador-desempenho-programa-previne-brasil`
- Filtros indicados: UF, quadrimestre, codigo de municipio e competencia.
- Uso potencial: avaliar desempenho da atencao primaria por municipio.
- Problemas investigaveis: acompanhamento de pacientes cronicos, cuidado preventivo, indicadores de APS.

### Previne Brasil - Cadastro vinculado

- API indicada no Swagger: `/atencao-primaria/cadastro-vinculado-programa-previne-brasil`
- Uso potencial: investigar cobertura/cadastro na APS.
- Problemas investigaveis: vinculo do paciente com a equipe, continuidade do cuidado, planejamento territorial.

### BNAFAR - Assistencia Farmaceutica

- API indicada no Swagger: grupo BNAFAR.
- Uso potencial: investigar disponibilidade/estoque de medicamentos, quando houver recurso adequado.
- Problemas investigaveis: falta de medicamento, transparencia de estoque, planejamento de reposicao.
- Observacao em 2026-07-18: o conjunto "BNAFAR - Posicao de Estoque" aparece no portal como atualizado em junho/2026, com granularidade municipal e descricao "Em manutencao"; por isso foi catalogado como oportunidade futura, mas nao entrou na analise Python desta rodada.

### SISVAN - Estado nutricional

- API indicada no Swagger: `/sisvan/estado-nutricional`
- Uso potencial: investigar acompanhamento nutricional e grupos vulneraveis.
- Problemas investigaveis: saude preventiva, acompanhamento de criancas, gestantes, idosos e populacoes vulneraveis.

### Registro de Ocupacao Hospitalar COVID-19

- API indicada no Swagger: `/assistencia-a-saude/registro-de-ocupacao-hospitalar-covid-19`
- Uso potencial: historico de ocupacao hospitalar em contexto de pandemia.
- Problemas investigaveis: ocupacao, resposta a picos de demanda, disponibilidade de leitos em situacoes criticas.
- Limitacao: contexto especifico de COVID-19; nao representa ocupacao hospitalar geral atual.

### SRAG - 2019 a 2026

- Pagina: https://dadosabertos.saude.gov.br/dataset/srag-2019-a-2026
- Formatos indicados: CSV, JSON, PARQUET, XML.
- Uso potencial: analisar sazonalidade e pressao assistencial por sindromes respiratorias.
- Problemas investigaveis: triagem, previsao de demanda, preparacao de unidades em periodos de pico.

## Observacao tecnica da coleta local

O ambiente local apresentou problema de validacao de certificado SSL com alguns endpoints oficiais. Para baixar os dados do IBGE foi usado fallback local com contexto SSL sem verificacao, restrito aos hosts oficiais. O ZIP de leitos foi baixado pelo endpoint HTTP publico do S3 indicado pelo portal oficial. Essa limitacao deve ser revisada se o projeto exigir trilha de auditoria mais forte.
