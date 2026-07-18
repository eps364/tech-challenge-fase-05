# SUS-Connect Appointment Service

Projeto reduzido para desenvolver a feature de agendamentos, consultas, exames,
lembretes ao paciente e reaproveitamento de vagas quando alguem informa que nao
podera comparecer.

## Escopo Atual

- `appointment-service`: API principal de agendamentos.
- `aps-prioritization-service`: priorizacao territorial e acompanhamento de busca ativa na APS.
- `common-lib`: suporte compartilhado de erros RFC 9457 e validacao.
- PostgreSQL local para dados de agendamentos e para a massa agregada de APS.

Os demais modulos permanecem no repositorio para historico, mas foram removidos
do build Maven e do Docker Compose principal.

## Funcionalidades

- Agendar consulta ou exame.
- Informar unidade, profissional, tipo de atendimento e notas de preparo.
- Guardar a notificacao do paciente no proprio agendamento.
- Gerar lembretes para agendamentos futuros em uma janela configuravel.
- Marcar que o paciente nao podera comparecer.
- Cancelar o horario aberto e oferecer a vaga para outro paciente com
  agendamento futuro equivalente.
- Aceitar ou recusar ofertas de antecipacao.
- Carregar massa local de pacientes e agendamentos via Flyway.

## Rodando Localmente

Subir banco e servico:

```bash
docker compose up --build
```

Subir apenas o banco para rodar o servico pela IDE/Maven:

```bash
docker compose -f docker-compose.dev.yml up -d
mvn -pl appointment-service -am spring-boot:run
```

## Priorizacao APS

O `aps-prioritization-service` apoia o coordenador de APS a decidir onde iniciar
uma busca ativa preventiva. Ele trabalha somente com dados territoriais
agregados: vinculo da populacao, indicadores preventivos e progresso agregado
das acoes. Nao armazena prontuarios, dados pessoais ou decisoes clinicas.

O primeiro fluxo demonstravel usa o caso ficticio de Joao: um territorio com
baixo vinculo e acompanhamento fragil de condicoes cronicas recebe prioridade,
a coordenacao registra uma acao e a equipe acompanha o numero agregado de
contatos realizados.

Rodar somente o banco do novo servico e iniciar pela IDE/Maven:

```bash
docker compose -f docker-compose.dev.yml up -d aps-prioritization-postgres
mvn -pl aps-prioritization-service -am spring-boot:run
```

URLs:

- API: `http://localhost:8205/api/v1`
- Swagger: `http://localhost:8205/swagger-ui/index.html`
- Health: `http://localhost:8205/actuator/health`

A massa de demonstracao e carregada automaticamente, com quatro territorios e
tres acoes territoriais. O territorio `Jardim Esperanca` inicia em prioridade
alta porque combina vinculo APS de 42% com indicadores preventivos abaixo da
meta. Os IDs de teste e os passos de execucao estao em
[`docs/API/aps-prioritization-service.md`](docs/API/aps-prioritization-service.md).

Testes e cobertura:

```bash
mvn -pl aps-prioritization-service -am verify
```

O build verifica cobertura de linha minima de 90% para o modulo e para cada
classe de producao. O relatorio fica em
`aps-prioritization-service/target/site/jacoco/index.html`.

URLs principais:

- API: `http://localhost:8202`
- Swagger: `http://localhost:8202/swagger-ui/index.html`
- Health: `http://localhost:8202/actuator/health`

## Massa Local

O Flyway cria cinco pacientes e cinco agendamentos futuros. IDs uteis:

| Pessoa | ID |
| --- | --- |
| Ana Souza | `11111111-1111-1111-1111-111111111111` |
| Bruno Lima | `22222222-2222-2222-2222-222222222222` |
| Carla Santos | `33333333-3333-3333-3333-333333333333` |
| Diego Martins | `44444444-4444-4444-4444-444444444444` |
| Elisa Rocha | `55555555-5555-5555-5555-555555555555` |

Para testar reaproveitamento de vaga:

1. Cancele/informe ausencia do exame `aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1`.
2. O sistema deve gerar uma oferta para Diego Martins, que possui um
   `Hemograma completo` em data mais distante.
3. Aceite a oferta retornada para antecipar o agendamento dele.

## Endpoints

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `GET` | `/api/v1/patients` | Lista pacientes da massa local. |
| `POST` | `/api/v1/appointments` | Cria consulta ou exame. |
| `GET` | `/api/v1/appointments/{id}` | Consulta agendamento por ID. |
| `GET` | `/api/v1/appointments?patientId={id}` | Lista agendamentos por paciente. |
| `PATCH` | `/api/v1/appointments/{id}/cancel` | Cancela agendamento. |
| `PATCH` | `/api/v1/appointments/{id}/cannot-attend` | Cancela e tenta gerar oferta da vaga. |
| `POST` | `/api/v1/appointments/notifications/reminders?hoursAhead=48` | Gera lembretes. |
| `GET` | `/api/v1/appointments/offers?patientId={id}` | Lista ofertas pendentes. |
| `PATCH` | `/api/v1/appointments/offers/{offerId}/accept` | Aceita oferta e antecipa agendamento. |
| `PATCH` | `/api/v1/appointments/offers/{offerId}/decline` | Recusa oferta. |

## Exemplo de Agendamento

```json
{
  "patientId": "11111111-1111-1111-1111-111111111111",
  "professionalId": "99999999-9999-9999-9999-999999999991",
  "dateTime": "2026-07-10T10:00:00",
  "appointmentType": "EXAM",
  "serviceName": "Hemograma completo",
  "facilityName": "UBS Central",
  "preparationNotes": "Jejum de 8 horas. Beber agua normalmente."
}
```

## Testes

```bash
mvn -q -pl appointment-service -am test
```
