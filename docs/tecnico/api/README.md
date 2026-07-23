# Colecoes da API APS

As colecoes ativas demonstram o fluxo de priorizacao territorial e busca ativa
preventiva. Elas nao usam pacientes, prontuarios ou agendamentos.

Para apresentar o fluxo com massa, fala sugerida e campos de output para
mostrar na tela, use `docs/tecnico/api/roteiro-demonstracao-aps.md`.

## Bruno

No Bruno, use **Open Collection**, nao **Import Collection**. Selecione a pasta
`docs/tecnico/api/`, que contem `bruno.json` e `collection.bru`. Depois abra a
pasta `Aps-Prioritization` dentro da collection e selecione o ambiente
`aps-local`.

Se a sua tela permitir apenas selecionar arquivo, use **Import Collection** com
`docs/tecnico/api/aps-prioritization-bruno.zip`. Caso o seletor esteja filtrando
somente `.bru`, arraste o ZIP para a area de importacao.

Ordem principal:

1. `01 - Health | servico no ar`
2. `02 - Dashboard inicial | fila territorial`
3. `03 - Prioridades HIGH | escolher territorio`
4. `04 - Detalhe Jardim Esperanca | explicar regra`
5. `05 - Criar acao territorial | prioridade vira trabalho`
6. `06 - Atualizar progresso | execucao agregada`
7. `07 - Dashboard apos progresso | fechar ciclo`

A requisicao de criacao guarda o ID retornado em `apsCreatedActionId` para que
a atualizacao de progresso execute o fluxo ponta a ponta. O request
`90 - Opcional | resetar indicadores Jardim Esperanca` e apenas de apoio para
testes manuais.

## Insomnia

Importe `docs/tecnico/api/aps-prioritization-insomnia.json`. A collection aponta para
`http://localhost:8205/api/v1` e usa os mesmos IDs demonstrativos do Bruno.
No Insomnia, caso queira atualizar exatamente a acao criada no passo 5, copie o
`id` retornado para a variavel `apsCreatedActionId` antes de executar o passo 6.

Consulte `docs/tecnico/api/aps-prioritization-service.md` para contratos e massa de
demonstracao.
