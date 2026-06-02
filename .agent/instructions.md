# Instruções do Agente — SUS-Connect Triagem Inteligente

Este arquivo define as diretrizes obrigatórias para a atuação do agente neste repositório. Para facilitar o reuso e a organização, o contexto foi separado em arquivos específicos na pasta `.agent/context/`.

## Fontes de Contexto Obrigatórias

Sempre consulte os arquivos abaixo para garantir que todas as interações e gerações de código estejam alinhadas com as definições do projeto:

1. **Regras de Negócio**: [`context/business_rules.md`](context/business_rules.md) — Protocolo de Manchester, fluxos de triagem e eventos de domínio.
2. **Arquitetura**: [`context/architecture.md`](context/architecture.md) — Estrutura de 7 microsserviços, Clean Architecture e bancos de dados.
3. **Boas Práticas**: [`context/best_practices.md`](context/best_practices.md) — Padrões de código, naming, commits e testes.
4. **Tecnologias**: [`context/technologies.md`](context/technologies.md) — Stack tecnológica detalhada (Java 21, Spring Boot 3.4.5).
5. **Histórico Técnico**: [`context/tech_history.md`](context/tech_history.md) — Decisões arquiteturais e suas justificativas.
6. **Sugestões e Evolução**: [`context/suggestions.md`](context/suggestions.md) — Próximos passos e melhorias sugeridas.

## Regras de Código

Consulte também:

- [`rules/clean-architecture.md`](rules/clean-architecture.md) — Estrutura obrigatória Core/Infra por serviço.
- [`rules/coding_rules.md`](rules/coding_rules.md) — Convenções de nomenclatura e validação de código.

## Workflow de Ambiente

Para executar o projeto localmente:

- [`workflows/run-environment.md`](workflows/run-environment.md) — Passos para subir o ambiente completo com Docker Compose.

## Artefatos do Projeto

- [`requirements.md`](requirements.md) — Requisitos funcionais (RF01–RF10) e não-funcionais com status de implementação.
- [`features.md`](features.md) — Features mapeadas em sprints com tarefas granulares e status de implementação.
- [`patterns.md`](patterns.md) — Exemplos de código padronizados (Entity, DTO, Service, Controller, Tests).

---

## Princípios Mandatórios

1. **Clean Architecture**: A lógica de negócio **deve** residir no `core` (entidades e use cases). Evite services anêmicos que apenas delegam ao repositório.
2. **Pure Java Core**: O pacote `core` deve ser **estritamente Java puro**. Nenhuma anotação de framework (JPA, Spring, Jakarta, Jackson) ou dependência de infraestrutura é permitida.
3. **SOLID e DRY**: Aplique rigorosamente os princípios SOLID. Use interfaces (gateways/portas) para desacoplar o `core` de implementações externas (adaptadores).
4. **Protocolo de Manchester**: Todo cálculo de risco clínico deve residir nas entidades de domínio — nunca em controllers ou services de infraestrutura.

## Regra de Ouro

Toda sugestão de código deve preservar a rastreabilidade do status da triagem e a clareza arquitetural dos microsserviços. Não introduzir dependências que quebrem a execução via Docker Compose.

---

**Nota**: Em caso de conflito, os arquivos de contexto em `.agent/context/` têm prioridade sobre resumos gerais. Siga as regras em `.agent/rules/` ao gerar qualquer código Java.
