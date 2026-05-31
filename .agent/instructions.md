# Instruções do Agente — SUS-Connect Triagem Inteligente

Este arquivo define as diretrizes obrigatórias para a atuação do agente neste repositório. Para facilitar o reuso e a organização, o contexto foi separado em arquivos específicos na pasta `.agent/context/`.

## Fontes de Contexto Obrigatórias

Sempre consulte os arquivos abaixo para garantir que todas as interações e gerações de código estejam alinhadas com as definições do projeto:

1. **Regras de Negócio**: [.agent/context/business_rules.md](file:///.agent/context/business_rules.md) — Protocolo de Manchester, fluxos de triagem e eventos de domínio.
2. **Arquitetura**: [.agent/context/architecture.md](file:///.agent/context/architecture.md) — Estrutura de microsserviços, Clean Architecture e bancos de dados.
3. **Boas Práticas**: [.agent/context/best_practices.md](file:///.agent/context/best_practices.md) — Padrões de código, naming, commits e testes.
4. **Tecnologias**: [.agent/context/technologies.md](file:///.agent/context/technologies.md) — Stack tecnológica detalhada.
5. **Histórico Técnico**: [.agent/context/tech_history.md](file:///.agent/context/tech_history.md) — Decisões arquiteturais e suas justificativas.
6. **Sugestões e Evolução**: [.agent/context/suggestions.md](file:///.agent/context/suggestions.md) — Próximos passos e melhorias sugeridas.

## Regras de Código

Consulte também:

- [.agent/rules/clean-architecture.md](file:///.agent/rules/clean-architecture.md) — Estrutura obrigatória Core/Infra por serviço.
- [.agent/rules/coding_rules.md](file:///.agent/rules/coding_rules.md) — Convenções de nomenclatura e validação de código.

## Workflow de Ambiente

Para executar o projeto localmente:

- [.agent/workflows/run-environment.md](file:///.agent/workflows/run-environment.md) — Passos para subir o ambiente completo com Docker Compose.

## Artefatos do Projeto

- [.agent/requirements.md](file:///.agent/requirements.md) — Requisitos funcionais (RF01–RF10) e não-funcionais.
- [.agent/features.md](file:///.agent/features.md) — Features mapeadas em sprints com tarefas granulares.
- [.agent/patterns.md](file:///.agent/patterns.md) — Exemplos de código padronizados (Entity, DTO, Service, Controller, Tests).

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
