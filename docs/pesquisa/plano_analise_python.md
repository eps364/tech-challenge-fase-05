# Plano de analise Python

Data: 2026-07-18

## Objetivo

Construir uma trilha reproduzivel de coleta, limpeza, integracao, indicadores e visualizacoes para escolher conscientemente um problema do SUS a ser tratado no MVP.

## Estado atual

Primeira analise executada:

- Tema explorado: oferta de leitos SUS e UTI SUS.
- Bases: Hospitais e Leitos 2026, estimativas municipais IBGE 2025 e API de Localidades.
- Script: `analytics/scripts/analisar_leitos_sus.py`
- Relatorio: `analytics/reports/analise_leitos_sus_2026.md`
- Relatorio visual: `analytics/reports/analise_leitos_sus_2026.html`

## Etapas propostas

1. Coleta
   - Preservar arquivos originais em `data/raw`.
   - Registrar URL, data de acesso, hash e filtros usados.
   - Evitar dados pessoais identificaveis.

2. Inspecao
   - Conferir colunas, tipos, competencias, cobertura geografica e nulos.
   - Identificar duplicidade temporal, como ocorreu no arquivo de leitos 2026.
   - Documentar campos usados e descartados.

3. Limpeza
   - Padronizar codigos IBGE.
   - Converter campos numericos.
   - Selecionar competencia mais recente quando a base tiver snapshots mensais.
   - Separar dado bruto de dado processado.

4. Integracao
   - Cruzar dados do SUS com populacao IBGE.
   - Integrar regioes de saude quando o problema exigir leitura regional.
   - Integrar CNES/UBS se a analise migrar para atencao primaria.

5. Indicadores
   - Leitos SUS por 10 mil habitantes.
   - UTI SUS por 100 mil habitantes.
   - Percentual de leitos existentes disponiveis ao SUS.
   - Municipios populosos sem leito SUS ou sem UTI SUS no arquivo.
   - Ranking de UFs, regioes e municipios por taxa proporcional.
   - Gap heuristico em relacao a media nacional, apenas como sinal de oportunidade.

6. Visualizacoes
   - Ranking de UFs por taxa de leitos SUS.
   - Ranking de UFs por taxa de UTI SUS.
   - Comparacao regional.
   - Tabela de municipios sinalizados.
   - Futuro: mapa municipal/UF e dashboard com filtros.

7. Validacao
   - Verificar se os achados continuam quando analisados por regiao de saude.
   - Cruzar com dados de demanda/produção se disponiveis.
   - Entrevistar profissionais ou gestores para validar a dor operacional.

## Perguntas que a analise deve responder

- O problema aparece nos dados oficiais?
- O problema e concentrado em municipios, regioes ou UFs especificas?
- O indicador e compreensivel para pitch e relatorio?
- A evidencia sustenta uma oportunidade de melhoria ou apenas descreve desigualdade estrutural?
- Quais dados faltam para transformar hipotese em decisao?

## Riscos de interpretacao

- Comparar municipios sem considerar redes regionais de referencia.
- Usar uma populacao desatualizada para leitos 2026 sem declarar a diferenca temporal; nesta rodada, o denominador usa a estimativa municipal IBGE de 01/07/2025.
- Somar competencias mensais e inflar capacidade cadastrada.
- Confundir capacidade cadastrada com disponibilidade real ou ocupacao.
- Inferir fila de espera sem base de regulacao ou demanda.

## Proximas analises candidatas

- UBS/CNES por populacao e territorio.
- Indicadores de desempenho da atencao primaria.
- Regioes de saude e dependencia de referencia regional.
- Producao hospitalar/ambulatorial para aproximar demanda.
- SRAG/dengue para pressao sazonal de atendimento.
