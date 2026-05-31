# Regra: Clean Architecture (Core/Infra) — SUS-Connect

Neste projeto de microsserviços de saúde, a arquitetura deve ser dividida rigorosamente entre **CORE** e **INFRA** em cada serviço.

## 1. Módulo Core

O `core` é o coração da aplicação, livre de frameworks externos e focado nas regras clínicas e de negócio.

- **`core.domain.entity`**: Entidades ricas com comportamento de domínio (ex: `Triage`, `Appointment`, `MedicalRecord`). Toda lógica do Protocolo de Manchester reside aqui.
- **`core.domain.valueobject`**: Objetos de valor imutáveis (ex: `RiskLevel`, `VitalSigns`, `ManchesterScore`).
- **`core.usecase`**: Casos de uso que orquestram a lógica de aplicação (ex: `ClassifyTriageUseCase`, `ScheduleAppointmentUseCase`).
- **`core.gateway`**: Interfaces (Portas de Saída) que definem como o sistema interage com o externo (ex: `TriageGateway`, `AppointmentGateway`).
- **`core.dto`**: DTOs de aplicação usando Java `record` para entrada/saída dos use cases.

## 2. Módulo Infra

O `infra` contém as implementações técnicas e integrações com frameworks.

- **`infra.entity`**: Entidades JPA com anotações de persistência (`@Entity`, `@Table`, `@Column`).
- **`infra.repository`**: Interfaces Spring Data JPA.
- **`infra.gateway`**: Implementações (Adapters) das interfaces do `core.gateway`.
- **`infra.web.controller`**: Controllers REST Spring Boot com `@RestController`.
- **`infra.messaging`**: Produtores e consumidores Kafka.
- **`infra.config`**: Configurações Spring Security, Feign, Beans.

## 3. Estrutura de Pacotes por Serviço

```text
src/main/java/br/com/fiap/<service>/
├── core/
│   ├── domain/
│   │   ├── entity/        ← Entidades ricas (Java puro)
│   │   └── valueobject/   ← Objetos de valor imutáveis
│   ├── usecase/           ← Orquestração de regras de negócio
│   ├── gateway/           ← Interfaces (portas de saída)
│   └── dto/               ← Records para I/O dos use cases
└── infra/
    ├── entity/            ← JPA Entities (@Entity)
    ├── repository/        ← Spring Data JPA
    ├── gateway/           ← Adapters que implementam core.gateway
    ├── messaging/         ← Kafka producers/consumers
    └── web/
        └── controller/    ← Spring RestControllers
```

## 4. Diretrizes de Injeção de Dependência

- O `core` **não deve conhecer** classes do `infra`.
- O `infra` conhece as interfaces do `core.gateway` para implementá-las.
- Beans de injeção devem ser declarados em classes `@Configuration` dentro do `infra`.
- Use construtores para injeção (nunca `@Autowired` em campo) nas implementações do `infra`.

## 5. Regra de Ouro

**Nunca importe** `org.springframework.*`, `jakarta.persistence.*`, `com.fasterxml.jackson.*` ou qualquer biblioteca externa dentro do pacote `core`.
