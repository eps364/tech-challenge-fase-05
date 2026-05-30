# Instruções de Implementação — SUS-Connect Triagem

## Objetivo do Projeto

Implementar o MVP **SUS-Connect "Triagem Inteligente"**, um sistema para gerenciamento de triagem clínica em unidades do SUS utilizando o Protocolo de Manchester v3.0. O foco é em: triagem automatizada, agendamento de consultas e histórico médico.

---

## Padrões Arquiteturais

### 1. Arquitetura em Camadas (Layered Architecture)

```
Controller (REST)
    ↓ DTO (entrada/saída)
Service (lógica de negócio)
    ↓ Model / Domain Objects
Repository (Spring Data JPA)
    ↓
Database (PostgreSQL via Flyway)
```

Regras:
- **Controller** nunca acessa Repository diretamente
- **Service** nunca expõe entidades JPA ao Controller (use DTOs)
- **Repository** contém apenas queries, sem lógica de negócio
- **Model** reflete o schema do banco; use validações na camada de Service

### 2. Convenções de Nomenclatura

| Tipo | Convenção | Exemplo |
| --- | --- | --- |
| Entidade JPA | `NomeDominio` | `Triagem`, `SinaisVitais` |
| DTO entrada | `NomeDominioRequest` | `TriagemRequest` |
| DTO saída | `NomeDominioResponse` | `TriagemResponse` |
| Service | `NomeDominioService` | `TriagemService` |
| Repository | `NomeDominioRepository` | `TriagemRepository` |
| Controller | `NomeDominioController` | `TriagemController` |
| Exception | `NomeDominioException` | `TriagemNaoEncontradaException` |
| Kafka Event | `NomeDominioEvento` | `ClassificacaoRiscoCalculadaEvento` |

### 3. Tratamento de Erros

Toda exceção de negócio deve:
1. Estender `TriagemException` (que possui `httpStatus` e `errorCode`)
2. Ser capturada por `GlobalExceptionHandler`
3. Retornar `ErrorResponse` com `code`, `message`, `timestamp` e `path`

---

## Stack Técnica

| Responsabilidade | Tecnologia | Versão |
| --- | --- | --- |
| Framework | Spring Boot | 3.3.0 |
| Linguagem | Java | 17 LTS |
| ORM | Spring Data JPA | (gerenciado pelo parent) |
| Banco | PostgreSQL | 15 |
| Migrations | Flyway | (gerenciado pelo parent) |
| Mensageria | Apache Kafka | 7.5.0 |
| Documentação | OpenAPI / Swagger | springdoc 2.1.0 |
| Testes unitários | JUnit 5 + Mockito | (gerenciados pelo parent) |
| Testes integração | TestContainers | 1.19.3 |
| Boilerplate | Lombok | (gerenciado pelo parent) |
| Formatação | Spotless (Google Java Format) | 1.18.1 |

---

## Diretivas de Desenvolvimento

### Obrigatórias

- **TDD**: Escrever testes antes da implementação quando possível
- **Flyway**: Toda alteração de schema via migration versionada (`V{n}__{descricao}.sql`)
- **Validação**: Usar `@Valid` + annotations de Bean Validation nos DTOs de entrada
- **Documentação de API**: Anotar controllers com `@Operation`, `@Tag`, `@ApiResponse`
- **Transações**: Usar `@Transactional` em métodos de escrita no Service
- **Logging**: Usar `@Slf4j` do Lombok; logar entrada/saída de operações críticas
- **Commits**: Seguir Conventional Commits (`feat`, `fix`, `test`, `docs`, `refactor`)

### Recomendadas

- Usar `@Builder` do Lombok nos DTOs
- Usar `Optional<T>` no Repository e tratar com `.orElseThrow()`
- Evitar `null` explícito; preferir `Optional` ou exceções claras
- Manter métodos de Service com responsabilidade única (< 30 linhas)
- Nomear métodos de Repository com verbos descritivos (`findByPacienteIdAndStatus`)

---

## Checklist por Feature

Para cada Feature implementada, verificar:

- [ ] Entidades JPA criadas com anotações corretas (`@Entity`, `@Table`, `@Column`)
- [ ] DTOs de entrada com validações (`@NotBlank`, `@NotNull`, `@Size`, `@Email`)
- [ ] DTOs de saída com campos necessários para o cliente
- [ ] Repository com queries customizadas (quando necessário)
- [ ] Service com lógica de negócio e `@Transactional`
- [ ] Controller com endpoints `@RestController`, `@Operation`, `@Valid`
- [ ] Testes unitários (Service) com Mockito — cobertura mínima 80%
- [ ] Testes de integração (Controller) com TestContainers
- [ ] Migration Flyway para novas tabelas ou alterações
- [ ] Documentação OpenAPI completa para todos os endpoints
- [ ] Auditoria de dados sensíveis (quando aplicável)

---

## Regras de Negócio Críticas

### Protocolo de Manchester v3.0

1. **5 cores de risco**: VERMELHO (imediato), LARANJA (muito urgente), AMARELO (urgente), VERDE (pouco urgente), AZUL (não urgente)
2. **Regra de Ouro Pediátrica**: Neonatos (< 28 dias) com febre ≥ 38°C → classificação automática LARANJA (independente do score)
3. **Score calculado** a partir de: sintoma principal + sinais vitais + nível de consciência (escala de Glasgow) + EVA
4. **Tempo máximo de espera** determinado pela cor:
   - VERMELHO: atendimento imediato (0 min)
   - LARANJA: ≤ 10 min
   - AMARELO: ≤ 60 min
   - VERDE: ≤ 120 min
   - AZUL: ≤ 240 min

### Event Sourcing

- Todo estado da triagem deve ser derivável dos eventos de domínio
- Tabela `eventos_dominio` é append-only (nunca atualizar/deletar)
- Versão do agregado incrementa a cada evento

---

## Kafka Topics

| Tópico | Produzido por | Consumido por | Evento |
| --- | --- | --- | --- |
| `triagem.events` | service-triagem | service-agendamento | Todos os eventos de triagem |
| `triagem.classificacao` | service-triagem | service-notificacao | `ClassificacaoRiscoCalculadaEvento` |
| `agendamento.commands` | service-agendamento | service-triagem | Comandos de agendamento |
