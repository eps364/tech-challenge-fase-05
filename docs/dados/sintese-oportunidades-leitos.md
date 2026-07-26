# Sintese inicial - oportunidades em leitos e UTI SUS

Data: 2026-07-18

Base analisada: Ministerio da Saude, Hospitais e Leitos, recurso Leitos 2026 CSV.

Competencia analisada: `202605`.

## Objetivo da etapa

Investigar se dados oficiais indicam desigualdades de capacidade hospitalar SUS que possam revelar oportunidades de melhoria no atendimento, regulacao, planejamento ou transparencia operacional.

## Resultados encontrados

- A base de maio/2026 possui registros de leitos hospitalares por estabelecimento, municipio, UF e regiao.
- Foram integradas estimativas municipais de populacao 2025 do IBGE para criar indicadores proporcionais.
- Media nacional calculada nesta analise: 16,56 leitos SUS por 10 mil habitantes.
- Media nacional calculada nesta analise: 15,00 UTI SUS por 100 mil habitantes.
- Foram sinalizados 182 municipios com algum padrao de oportunidade pela heuristica inicial.
- Existem 14 municipios com 50 mil habitantes ou mais e zero leitos SUS no arquivo analisado.
- Existem 47 municipios com 100 mil habitantes ou mais e zero UTI SUS no arquivo analisado.

## Evidencias iniciais

UFs com menores taxas de leitos SUS por 10 mil habitantes:

- SP: 13,45.
- SE: 13,87.
- RJ: 14,03.
- AM: 14,29.
- PA: 14,76.

UFs com menores taxas de UTI SUS por 100 mil habitantes:

- AC: 9,16.
- AP: 9,80.
- PI: 9,87.
- AM: 10,20.
- BA: 11,39.

Municipios sinalizados no topo da heuristica:

- Palhoca - SC: 253.469 habitantes, zero leitos SUS e zero UTI SUS no arquivo.
- Almirante Tamandare - PR: 125.861 habitantes, zero leitos SUS e zero UTI SUS no arquivo.
- Jandira - SP: 121.550 habitantes, zero leitos SUS e zero UTI SUS no arquivo.
- Poa - SP: 106.355 habitantes, zero leitos SUS e zero UTI SUS no arquivo.

## Interpretacao

- Fato/dado: ha variacao relevante na oferta cadastrada de leitos SUS e UTI SUS por habitante entre UFs e municipios.
- Interpretacao: localidades populosas com baixa oferta local podem depender mais de referencia regional, regulacao e comunicacao eficiente.
- Hipotese: existe oportunidade para apoiar gestores/reguladores/pacientes com melhor visibilidade de capacidade, referencia ou priorizacao regional.
- Decisao ainda nao tomada: nao foi escolhido problema final nem solucao final.

## Limitacoes

- A base mede capacidade cadastrada, nao tempo de espera.
- A base nao mede ocupacao em tempo real.
- A base nao mede qualidade do atendimento.
- A base nao informa diretamente demanda reprimida, filas, deslocamentos ou desfecho clinico.
- Ausencia de UTI ou leito SUS em um municipio pode ser compensada por rede regional.
- A populacao usada e uma estimativa municipal de 01/07/2025, enquanto os leitos sao de maio/2026.
- Os dados disponiveis nao permitem afirmar causalidade entre baixa oferta local e pior atendimento.

## Arquivos gerados

- `analytics/reports/analise_leitos_sus_2026.md`
- `analytics/reports/analise_leitos_sus_2026.html`
- `data/processed/leitos_uf_indicadores_2026.csv`
- `data/processed/leitos_regiao_indicadores_2026.csv`
- `data/processed/leitos_municipio_indicadores_2026.csv`
- `data/processed/leitos_oportunidades_municipio_2026.csv`

## Proximo passo recomendado

Validar se a dor mais relevante e:

- falta de leito/capacidade local;
- falta de visibilidade sobre referencia regional;
- dificuldade de regulacao e encaminhamento;
- falta de transparencia para o paciente;
- dificuldade do gestor em identificar gargalos.

Essa validacao deve ocorrer antes de desenhar qualquer solucao tecnica.
