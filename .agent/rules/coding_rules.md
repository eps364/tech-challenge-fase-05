# Coding Rules — SUS-Connect Triagem Inteligente

Estas regras devem ser seguidas ao gerar ou modificar código neste projeto.

## Naming Conventions

### Use Cases

- **Pattern**: `<Acao><Entidade>UseCase.java`
- **Example**: `ClassifyTriageUseCase`, `ScheduleAppointmentUseCase`, `CreateMedicalRecordUseCase`
- **Método principal**: deve ser `execute()`

### Gateways (Interfaces no Core)

- **Pattern**: `<Entidade>Gateway.java`
- **Example**: `TriageGateway`, `AppointmentGateway`, `MedicalRecordGateway`

### Adapters (Implementações na Infra)

- **Pattern**: `<Entidade>RepositoryAdapter.java` ou `<Recurso>GatewayImpl.java`
- **Example**: `TriageRepositoryAdapter`, `AppointmentFeingGatewayImpl`

### DTOs (Core)

- **Pattern**: Java `record` com sufixo `Input` (entrada) e `Output` (saída)
- **Example**: `TriageInput`, `TriageOutput`, `RiskClassificationOutput`

### DTOs (Infra/Web)

- **Pattern**: `<Entidade>Request.java` e `<Entidade>Response.java`
- **Example**: `TriageRequest`, `TriageResponse`

### Value Objects

- **Pattern**: Nome descritivo do conceito, sem sufixo
- **Example**: `RiskLevel`, `VitalSigns`, `ManchesterScore`, `GlasgowScale`

### Kafka Events

- **Pattern**: `<EntidadeEvento>.java` (substantivo do evento)
- **Example**: `RiskClassificationCalculatedEvent`, `AppointmentConfirmedEvent`

---

## Validation Rules

1. **No Frameworks in Core**: Nunca importe `org.springframework.*`, `jakarta.persistence.*` ou `com.fasterxml.jackson.*` no pacote `core`.
2. **Rich Domain**: Lógica de negócio (ex: cálculo do score Manchester, regra pediátrica) deve estar na Entity de domínio ou Value Object, **nunca** no Use Case ou Service de infra.
3. **Manual Mapping**: O mapeamento de `Domain Entity` ↔ `JPA Entity` deve ser feito manualmente nos Adapters (sem MapStruct no `core`).
4. **Validation Messages**: Mensagens de erro de validação devem ser em **inglês** e indicar o campo com problema. Ex: `"The patient ID cannot be null"`, `"The risk level must be one of: RED, ORANGE, YELLOW, GREEN, BLUE"`.
5. **Flyway Migrations**: Toda alteração de schema deve ser via migration versionada `V{major}.{minor}__{descricao}.sql`. Nunca usar DDL automático do Hibernate.

---

## Code Standards

### Checklist por Feature

Para cada feature implementada, verificar:

- [ ] Entidade de domínio no `core.domain.entity` com lógica de negócio encapsulada
- [ ] Value Objects imutáveis no `core.domain.valueobject` quando aplicável
- [ ] Use Case no `core.usecase` com método `execute()` e responsabilidade única
- [ ] Gateway interface no `core.gateway`
- [ ] JPA Entity no `infra.entity` com mapeamento manual para/de Domain Entity
- [ ] Adapter no `infra.gateway` implementando a interface do `core`
- [ ] Controller no `infra.web.controller` com `@Operation`, `@Tag`, `@ApiResponse`
- [ ] DTOs de entrada com `@Valid` + Bean Validation
- [ ] Testes unitários no `core` (sem Spring) com Mockito — cobertura mínima 80%
- [ ] Testes de integração no `infra` com TestContainers
- [ ] Migration Flyway para novas tabelas ou alterações de schema
- [ ] Documentação OpenAPI completa

### Obrigatório

- **Logging**: Usar `@Slf4j` do Lombok; logar entrada/saída de operações críticas no Use Case
- **Transações**: Usar `@Transactional` em métodos de escrita nos Adapters (não no Use Case do `core`)
- **Optional**: Usar `Optional<T>` no Gateway e tratar com `.orElseThrow(DomainException::new)`
- **Commits**: Seguir Conventional Commits — `feat`, `fix`, `test`, `docs`, `refactor`
