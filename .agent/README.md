# SUS-Connect — Agente de Implementação

Bem-vindo ao diretório de configuração do agente de implementação do projeto **SUS-Connect Triagem Inteligente**.

## Objetivo

Este diretório contém os arquivos de especificação que guiam a implementação do MVP SUS-Connect, um sistema de triagem clínica automatizada com Protocolo de Manchester v3.0.

## Índice de Arquivos

| Arquivo | Conteúdo |
| --- | --- |
| `instructions.md` | Diretivas de arquitetura, padrões de código e checklist por feature |
| `requirements.md` | Requisitos funcionais (RF01–RF10) e não-funcionais (RNF01–RNF07) |
| `features.md` | 8 Features mapeadas em sprints com tarefas granulares |
| `patterns.md` | Exemplos de código padronizados (Entity, DTO, Service, Controller, Tests) |

## Stack Técnica

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 17 LTS |
| Framework | Spring Boot 3.3.0 |
| ORM | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL 15 |
| Migrations | Flyway |
| Message Bus | Apache Kafka 7.5.0 |
| Documentação | OpenAPI 3.0 (Swagger UI) |
| Build | Maven 3.9+ |
| Testes | JUnit 5 + Mockito + TestContainers |
| Containers | Docker Compose |

## Estrutura de Microsserviços

```
tech-challenge-fase-05/
├── .agent/                  # Configuração do agente (este diretório)
├── service-triagem/         # Microsserviço de Triagem (Sprint 0–7)
│   ├── pom.xml
│   ├── docker-compose.yml
│   └── src/
│       ├── main/java/...
│       └── test/java/...
└── docs/
    ├── event-storm.md
    └── protocol_manchester.md
```

## Como Usar Este Diretório

1. Leia `instructions.md` para entender as regras arquiteturais e de codificação
2. Consulte `requirements.md` para mapear cada implementação a um requisito
3. Use `features.md` para saber o que implementar por sprint
4. Siga os exemplos em `patterns.md` para manter consistência no código

## Convenções de Commit

Usar [Conventional Commits](https://www.conventionalcommits.org/):

```
feat(triagem): adicionar endpoint POST /triagens
fix(triagem): corrigir cálculo de score Manchester
test(triagem): adicionar testes para ClassificacaoService
docs(triagem): atualizar README com novos endpoints
refactor(triagem): extrair logica de scoring para ValueObject
```
