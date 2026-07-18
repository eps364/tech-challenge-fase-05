# SUS-Connect APS - Priorizacao de Busca Ativa

MVP do hackathon para apoiar coordenadores de Atencao Primaria a Saude (APS) a
decidir em quais territorios ou UBS iniciar uma acao de busca ativa preventiva.

O produto usa somente indicadores territoriais agregados. Ele nao gerencia
pacientes, prontuarios, agendamentos, prescricoes ou decisoes clinicas.

## O Fluxo Demonstravel

1. O coordenador abre o painel e identifica os territorios prioritarios.
2. Ele consulta o detalhe de um territorio e entende os indicadores que geraram
   a prioridade.
3. Ele cria uma acao territorial de busca ativa com foco, equipe, prazo e meta.
4. A equipe atualiza apenas o progresso agregado da acao.
5. O painel mostra prioridades, acoes abertas, concluidas e alertas de prazo.

O caso ficticio de Joao e os requisitos completos estao em
[`docs/pesquisa/especificacao_requisitos_mvp_gestao_ativa_aps.md`](docs/pesquisa/especificacao_requisitos_mvp_gestao_ativa_aps.md).

## Modulos Ativos

- `aps-prioritization-service`: API do MVP de priorizacao territorial e busca
  ativa.
- `common-lib`: respostas de erro RFC 9457 e suporte compartilhado.

Os demais materiais do repositorio pertencem a iteracoes anteriores e nao fazem
parte do build Maven ou do Docker Compose atual.

## Rodando Localmente

Subir o banco e a API do MVP:

```bash
docker compose up -d --build aps-prioritization-service
```

Ou subir apenas o banco para executar a API na IDE/Maven:

```bash
docker compose -f docker-compose.dev.yml up -d aps-prioritization-postgres
mvn -pl aps-prioritization-service -am spring-boot:run
```

URLs locais:

- API: `http://localhost:8205/api/v1`
- Swagger: `http://localhost:8205/swagger-ui/index.html`
- Health: `http://localhost:8205/actuator/health`
- PostgreSQL: `localhost:5434`

A massa demonstrativa e carregada automaticamente: quatro territorios
ficticios e tres acoes agregadas. `Jardim Esperanca` inicia em alta prioridade
por combinar vinculo APS de 42% com indicadores abaixo da meta.

## API e Testes

A documentacao detalhada e as massas de teste estao em
[`docs/API/aps-prioritization-service.md`](docs/API/aps-prioritization-service.md).

```bash
# Testes unitarios e de integracao HTTP
mvn -q -pl aps-prioritization-service -am test

# Garante no minimo 90% de cobertura de linhas no modulo e em cada classe
mvn -q -pl aps-prioritization-service jacoco:check@coverage-check
```

As colecoes de demonstracao estao em:

- Bruno: `docs/API/Aps-Prioritization/`, usando o ambiente `aps-local`.
- Insomnia: `docs/API/aps-prioritization-insomnia.json`.

## Contexto para Agentes

O ponto de entrada para agentes de IA e colaboradores automatizados e
[`AGENTS.md`](AGENTS.md). O contexto atual do MVP, as habilidades especializadas,
os fluxos de entrega e os modelos de verificacao estao em
[`.agents/`](.agents/README.md). A pasta `.agent/` permanece apenas como
referencia historica da versao anterior de triagem e nao deve orientar novos
desenvolvimentos.
