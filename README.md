# SUS-Connect APS

MVP de priorizacao territorial de busca ativa preventiva na Atencao Primaria a
Saude (APS). Ele ajuda a coordenacao a responder: **qual territorio ou UBS
deve receber uma acao primeiro, e por qual motivo?**

O sistema trabalha exclusivamente com indicadores e resultados agregados. Nao
armazena pacientes, prontuarios, diagnosticos, prescricoes, agendamentos ou
risco clinico individual. A prioridade e um sinal operacional explicavel, nao
uma decisao assistencial automatica.

## Fluxo do MVP

1. A coordenacao consulta o painel e identifica territorios prioritarios.
2. Abre o detalhe e entende os sinais agregados e metas que justificam a
   prioridade.
3. Cria uma acao territorial de busca ativa com foco, equipe, prazo e meta.
4. A equipe atualiza apenas o progresso agregado da acao.
5. O painel devolve a visao do trabalho aberto, concluido e proximo do prazo.

## Estrutura ativa

```text
aps-prioritization-service/  # API Java 21 / Spring Boot 3.4.5
analytics/                   # Processamento e relatorios de dados agregados
data/                        # Fontes preservadas e saidas processadas
docs/                        # Contexto, produto, dados, tecnica e apresentacao
scripts/                     # Automacao de demonstracao E2E
```

O build Maven e o Compose principal possuem somente o
`aps-prioritization-service` e seu PostgreSQL.

## Executar localmente

Suba o banco e a API:

```bash
docker compose up -d --build aps-prioritization-service
```

Ou suba apenas o banco para executar a API pela IDE ou Maven:

```bash
docker compose up -d aps-prioritization-postgres
mvn -pl aps-prioritization-service -am spring-boot:run
```

| Recurso | Endereco |
| --- | --- |
| API | `http://localhost:8205/api/v1` |
| Swagger | `http://localhost:8205/swagger-ui/index.html` |
| Health | `http://localhost:8205/actuator/health` |
| PostgreSQL | `localhost:5434` |

## Validacao

```bash
# Testes unitarios e de integracao HTTP
mvn -q -pl aps-prioritization-service -am test

# Cobertura minima de 90% por modulo e classe de producao
mvn -q -pl aps-prioritization-service jacoco:check@coverage-check

# Fluxo HTTP real com Docker e PostgreSQL dedicado
powershell -ExecutionPolicy Bypass -File .\scripts\run-e2e.ps1
```

## Documentacao

O [indice da documentacao](docs/README.md) organiza os materiais por objetivo:

- [Contexto do hackathon](docs/contexto/README.md)
- [Produto, requisitos e caso tangivel](docs/produto/README.md)
- [Dados, fontes e analises](docs/dados/README.md)
- [Contexto tecnico, API, Bruno, Insomnia e E2E](docs/tecnico/README.md)
- [Deck e roteiro de pitch](docs/apresentacao/README.md)

## Contexto para agentes

[AGENTS.md](AGENTS.md) e o ponto de entrada para colaboradores automatizados.
Ele descreve os limites de produto, a arquitetura limpa, os comandos de
qualidade e a regra de commits atomicos.
