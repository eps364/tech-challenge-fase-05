# Contexto tecnico

O runtime ativo possui um unico servico Java 21 com Spring Boot 3.4.5:
`aps-prioritization-service`. Ele segue Clean Architecture e persiste
territorios, indicadores agregados e acoes de busca ativa em PostgreSQL.

| Material | Uso |
| --- | --- |
| [Ambiente local](ambiente-local.md) | Portas, URLs e comandos do Compose ativo. |
| [API](api/README.md) | Contratos REST, colecoes Bruno e export Insomnia. |
| [E2E](e2e/README.md) | Execucao HTTP real contra Docker e PostgreSQL dedicado. |

Para regras de arquitetura, configuracao de dependencias e convencoes de
contribuicao, consulte [AGENTS.md](../../AGENTS.md) e
[`.agents/context/architecture-and-runtime.md`](../../.agents/context/architecture-and-runtime.md).
