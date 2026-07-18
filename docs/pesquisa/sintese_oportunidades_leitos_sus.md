# Sintese inicial - oportunidades em leitos e UTI SUS

Data: 2026-07-18

Base analisada: Ministerio da Saude, Hospitais e Leitos, recurso Leitos 2026 CSV.

Competencia analisada: `202605`.

## Objetivo da etapa

Investigar se dados oficiais indicam desigualdades de capacidade hospitalar SUS que possam revelar oportunidades de melhoria no atendimento, regulacao, planejamento ou transparencia operacional.

## Resultados encontrados

- A base de maio/2026 possui registros de leitos hospitalares por estabelecimento, municipio, UF e regiao.
- Foram integrados dados populacionais do Censo 2022 do IBGE para criar indicadores proporcionais.
- Media nacional calculada nesta analise: 17,41 leitos SUS por 10 mil habitantes.
- Media nacional calculada nesta analise: 15,77 UTI SUS por 100 mil habitantes.
- Foram sinalizados 172 municipios com algum padrao de oportunidade pela heuristica inicial.
- Existem 12 municipios com 50 mil habitantes ou mais e zero leitos SUS no arquivo analisado.
- Existem 40 municipios com 100 mil habitantes ou mais e zero UTI SUS no arquivo analisado.

## Evidencias iniciais

UFs com menores taxas de leitos SUS por 10 mil habitantes:

- SP: 13,96.
- SE: 14,43.
- RJ: 15,05.
- AM: 15,66.
- PA: 15,83.

UFs com menores taxas de UTI SUS por 100 mil habitantes:

- AC: 9,76.
- PI: 10,21.
- AP: 10,77.
- AM: 11,19.
- MA: 11,92.

Municipios sinalizados no topo da heuristica:

- Palhoca - SC: 222.598 habitantes, zero leitos SUS e zero UTI SUS no arquivo.
- Almirante Tamandare - PR: 119.825 habitantes, zero leitos SUS e zero UTI SUS no arquivo.
- Jandira - SP: 118.045 habitantes, zero leitos SUS e zero UTI SUS no arquivo.
- Poa - SP: 103.765 habitantes, zero leitos SUS e zero UTI SUS no arquivo.

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
- A populacao usada e de 2022, enquanto os leitos sao de maio/2026.
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
