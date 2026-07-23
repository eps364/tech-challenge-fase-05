# Roteiro de demonstracao da API APS

Este roteiro foi feito para apresentar o fluxo pelo Bruno ou pelo Insomnia com
a aplicacao Docker em execucao. A historia deve caber em poucos minutos:
identificar o territorio prioritario, explicar a regra, criar uma acao,
registrar progresso agregado e voltar ao dashboard.

## Preparacao do ambiente

Suba o servico principal:

```powershell
docker compose up -d --build aps-prioritization-service
curl.exe http://localhost:8205/actuator/health
```

O health esperado e `{"status":"UP"}`.

Para repetir a apresentacao com a base limpa, remova o volume e suba de novo:

```powershell
docker compose down -v
docker compose up -d --build aps-prioritization-service
```

A massa demonstrativa so e carregada quando o banco esta vazio.

## Massa demonstrativa

Os territorios e equipes abaixo sao ficticios e agregados. Nao representam
pacientes, prontuarios, CPF, endereco, diagnostico ou risco clinico individual.

| Territorio | ID | O que mostrar |
| --- | --- | --- |
| Jardim Esperanca | `10000000-0000-0000-0000-000000000001` | Prioridade alta: vinculo APS de 42%, condicoes cronicas em 32%/60% e pre-natal em 72%/85%. |
| Vila Nova | `10000000-0000-0000-0000-000000000002` | Prioridade media e acao planejada vencida, util para mostrar alerta operacional. |
| Parque das Flores | `10000000-0000-0000-0000-000000000003` | Prioridade media porque o vinculo esta abaixo da meta, mas os indicadores cadastrados atendem as metas. |
| Centro | `10000000-0000-0000-0000-000000000004` | Prioridade baixa e acao concluida, servindo como contraste. |

| Acao inicial | ID | Situacao |
| --- | --- | --- |
| Jardim Esperanca, condicoes cronicas | `20000000-0000-0000-0000-000000000001` | Em andamento, 54 de 80 contatos agregados, progresso de 67,50%. |
| Vila Nova, condicoes cronicas | `20000000-0000-0000-0000-000000000002` | Planejada e vencida, sem contatos registrados. |
| Centro, vacinacao infantil | `20000000-0000-0000-0000-000000000003` | Concluida, 47 de 50 contatos agregados. |

As datas das acoes iniciais sao relativas ao dia em que o container carrega a
massa. Por isso, ao reiniciar com volume limpo, Vila Nova fica vencida e Jardim
Esperanca fica proxima do prazo.

## Sequencia principal da collection

No Bruno, abra a collection com **Open Collection** apontando para
`docs/tecnico/api/`, nao pelo importador de arquivo. Use o ambiente `aps-local`.
Se a interface permitir apenas arquivo, importe
`docs/tecnico/api/aps-prioritization-bruno.zip` ou arraste esse ZIP para a area
de importacao. O passo 5 captura automaticamente o ID da acao criada em
`apsCreatedActionId`, e o passo 6 usa esse ID.

| Ordem | Request | O que dizer | O que apontar no output |
| --- | --- | --- | --- |
| 1 | `01 - Health | servico no ar` | A API esta disponivel no Docker. | `status = UP`. |
| 2 | `02 - Dashboard inicial | fila territorial` | O painel transforma indicadores em uma fila de trabalho territorial. | `highPriorityTerritoryCount`, `openActionCount`, `topPriorities` e `attentionActions`. |
| 3 | `03 - Prioridades HIGH | escolher territorio` | A coordenacao escolhe por onde iniciar. | Jardim Esperanca aparece com `priority = HIGH` e foco de atencao em `CHRONIC_CONDITIONS`. |
| 4 | `04 - Detalhe Jardim Esperanca | explicar regra` | A prioridade e explicavel, nao uma caixa preta. | `linkedPopulationPercent = 42.00`, `priority.linkageTarget = 50.00`, `priority.reasons` e indicadores abaixo da meta. |
| 5 | `05 - Criar acao territorial | prioridade vira trabalho` | A decisao vira uma acao com foco, equipe, prazo e meta agregada. | HTTP `201`, `status = PLANNED`, `targetCount = 80`, `performedCount = 0` e o `id` retornado. |
| 6 | `06 - Atualizar progresso | execucao agregada` | A equipe registra execucao agregada, sem lista de pessoas. | `status = IN_PROGRESS`, `performedCount = 54` e `progressPercent = 67.50`. |
| 7 | `07 - Dashboard apos progresso | fechar ciclo` | O painel volta a mostrar o trabalho aberto e os alertas operacionais. | `openActionCount` aumenta por causa da nova acao e `attentionActions` segue destacando prazos. |

O request `90 - Opcional | resetar indicadores Jardim Esperanca` serve apenas
para restaurar os indicadores do territorio depois de testes manuais. Ele nao
limpa acoes criadas; para isso use `docker compose down -v`.

## Falas seguras para a banca

- "A prioridade e territorial ou por UBS, nunca individual."
- "O dado usado aqui e agregado ou ficticio para demonstracao."
- "A regra apoia uma decisao operacional: onde organizar a busca ativa primeiro."
- "O progresso mede execucao da acao, nao impacto clinico ou causalidade."
- "A decisao final continua com a coordenacao e a equipe de saude."

Evite dizer que a solucao diagnostica pessoas, preve agravamento clinico,
impede internacoes ou prova resultado assistencial.
