# Colecoes da API APS

As colecoes ativas demonstram o fluxo de priorizacao territorial e busca ativa
preventiva. Elas nao usam pacientes, prontuarios ou agendamentos.

## Bruno

Abra a pasta `docs/API/Aps-Prioritization/` no Bruno e selecione o ambiente
`docs/API/environments/aps-local.bru`.

Ordem sugerida:

1. `01-Health`
2. `02-Get-Dashboard`
3. `03-List-High-Priority-Territories`
4. `04-Get-Territory-Details`
5. `05-Create-Search-Action`
6. `06-Update-Search-Action-Progress`
7. `07-Replace-Territory-Indicators`

A requisicao de criacao guarda o ID retornado para que a atualizacao de
progresso execute o fluxo ponta a ponta.

## Insomnia

Importe `docs/API/aps-prioritization-insomnia.json`. A collection aponta para
`http://localhost:8205/api/v1` e usa os mesmos IDs demonstrativos do Bruno.

Consulte `docs/API/aps-prioritization-service.md` para contratos e massa de
demonstracao.
