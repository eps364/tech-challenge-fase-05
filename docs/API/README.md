# Appointment Service API Collection

Colecao Bruno reduzida para o escopo atual de agendamentos.

## Ambiente Local

Use `docs/API/environments/local.bru`.

Variaveis principais:

| Variavel | Valor |
| --- | --- |
| `appointmentBaseUrl` | `http://localhost:8202` |
| `appointmentId` | ID de agendamento para testes |
| `offerId` | ID de oferta gerada pelo fluxo `Cannot Attend` |
| `patientId` | Paciente principal da massa local |
| `futurePatientId` | Paciente que pode receber oferta de antecipacao |

## Fluxo Recomendado

1. `List Patients`
2. `Get Appointment By Id`
3. `Cannot Attend`
4. `List Offers By Patient`
5. `Accept Offer` ou `Decline Offer`
6. `Notify Upcoming`

O caso de massa mais direto e cancelar o agendamento
`aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1`; ele abre uma vaga de
`Hemograma completo` e deve gerar oferta para o paciente
`44444444-4444-4444-4444-444444444444`.

## Cenario Guiado: Duas Pessoas Para o Mesmo Exame

A pasta `Reallocation Scenario` cria um caso isolado, sem depender da massa
seedada:

1. `01 Schedule Closer Exam`
2. `02 Schedule Later Exam`
3. `03 Closer Cannot Attend`
4. `04 List Later Patient Offers`
5. `05 Accept Offer`
6. `06 Get Later Appointment After Accept`

As datas e pacientes ficam no environment `local`:

| Variavel | Uso |
| --- | --- |
| `scenarioCloserPatientId` | Paciente com exame mais proximo |
| `scenarioLaterPatientId` | Paciente com exame mais distante |
| `scenarioCloserDateTime` | Horario que sera liberado |
| `scenarioLaterDateTime` | Horario original do segundo paciente |
| `scenarioServiceName` | Nome do exame usado para casar a oferta |

Se rodar o mesmo cenario mais de uma vez e houver conflito de horario, altere
`scenarioCloserDateTime` e `scenarioLaterDateTime` no environment.
