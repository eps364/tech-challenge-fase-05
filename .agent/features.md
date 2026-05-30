# Features e Sprints — SUS-Connect Triagem

## Visão Geral

| Feature | Sprint | Tema | Status |
| --- | --- | --- | --- |
| F1 | Sprint 0 | Autenticação e Autorização | 🔲 Pendente |
| F2 | Sprint 1 | Gerenciamento de Perfil | 🔲 Pendente |
| F3 | Sprint 2 | Busca de Serviços de Saúde | 🔲 Pendente |
| F4 | Sprint 3 | Gerenciamento de Agendamentos | 🔲 Pendente |
| F5 | Sprint 4 | Avaliações e Notas | 🔲 Pendente |
| F6 | Sprint 5 | Histórico Médico | 🔲 Pendente |
| F7 | Sprint 6 | Notificações | 🔲 Pendente |
| F8 | Sprint 7 | Dashboard e Relatórios | 🔲 Pendente |

---

## F1 — Autenticação e Autorização (Sprint 0)

**Objetivo**: Implementar autenticação JWT com Spring Security para proteger todos os endpoints.

**Requisitos**: RF01, RF02, RNF03

### Tarefas

- T1.1 — Criar entidade `User` (id, cpf, name, email, passwordHash, role, createdAt, updatedAt)
- T1.2 — Criar `UserRepository` com método `findByEmail` e `existsByCpf`
- T1.3 — Criar `UserRequest` (campos: cpf, name, email, password, role) com validações Bean Validation
- T1.4 — Criar `UserResponse` (campos: id, cpf, name, email, role, createdAt)
- T1.5 — Criar `AuthRequest` (campos: email, password) e `AuthResponse` (campos: token, refreshToken, expiresIn)
- T1.6 — Implementar `JwtService` (geração, validação e extração de claims do JWT)
- T1.7 — Implementar `AuthService` (signup com bcrypt, authenticate com JWT)
- T1.8 — Configurar `SecurityConfig` (Spring Security 6: filtros, CSRF, permitAll em /auth/**)
- T1.9 — Criar `JwtAuthFilter` (OncePerRequestFilter que valida token e seta contexto de segurança)
- T1.10 — Criar `AuthController` (POST /api/v1/auth/signup, POST /api/v1/auth/login, POST /api/v1/auth/refresh)
- T1.11 — Testes unitários para `JwtService` e `AuthService` (Mockito)
- T1.12 — Testes de integração para `AuthController` (TestContainers + MockMvc)

**Migrações necessárias**: `V1.1__users_table.sql`

---

## F2 — Gerenciamento de Perfil (Sprint 1)

**Objetivo**: Permitir que usuários atualizem seus dados e configurem preferências.

**Requisitos**: RF09, RNF03

### Tarefas

- T2.1 — Criar entidade `UserProfile` (id, userId, phone, avatarUrl, notificationPreferences, createdAt)
- T2.2 — Criar `UserProfileRepository`
- T2.3 — Criar `UserProfileRequest` e `UserProfileResponse`
- T2.4 — Criar `NotificationPreferences` (Value Object: receiveEmail, receivePush, receiveWhatsApp)
- T2.5 — Implementar `UserProfileService` (getProfile, updateProfile, updateNotifications)
- T2.6 — Implementar exportação de dados pessoais em JSON (conformidade LGPD)
- T2.7 — Implementar soft-delete com anonimização de dados
- T2.8 — Criar `UserProfileController` (GET/PUT /api/v1/users/me, DELETE /api/v1/users/me)
- T2.9 — Validar que usuário só acessa/edita o próprio perfil (autorização por subject JWT)
- T2.10 — Criar endpoint de export (GET /api/v1/users/me/export) retornando JSON
- T2.11 — Testes unitários para `UserProfileService`
- T2.12 — Testes de integração para `UserProfileController`

**Migrações necessárias**: `V1.2__user_profiles_table.sql`

---

## F3 — Busca de Serviços de Saúde (Sprint 2)

**Objetivo**: Permitir busca e visualização de unidades de saúde.

**Requisitos**: RF03, RF04, RNF01

### Tarefas

- T3.1 — Criar entidade `HealthService` (id, name, address, phone, specialties, openingHours, averageRating)
- T3.2 — Criar entidade `Professional` (id, healthServiceId, name, specialty, crm, bio)
- T3.3 — Criar `HealthServiceRepository` com query de busca por filtros (Specification ou @Query)
- T3.4 — Criar `HealthServiceResponse` e `ProfessionalResponse`
- T3.5 — Criar `HealthServiceFilterRequest` (specialty, city, state, availableOnly)
- T3.6 — Implementar `HealthServiceService` (search, findById, getDetails)
- T3.7 — Implementar paginação com `Pageable` e `Page<HealthServiceResponse>`
- T3.8 — Implementar cache in-memory (Spring Cache + Caffeine, TTL 5 min)
- T3.9 — Criar `HealthServiceController` (GET /api/v1/services, GET /api/v1/services/{id})
- T3.10 — Criar endpoint de profissionais (GET /api/v1/services/{id}/professionals)
- T3.11 — Testes unitários para `HealthServiceService`
- T3.12 — Testes de integração para `HealthServiceController`

**Migrações necessárias**: `V1.3__health_services_table.sql`, `V1.4__professionals_table.sql`

---

## F4 — Gerenciamento de Agendamentos (Sprint 3)

**Objetivo**: Implementar fluxo completo de agendamento, cancelamento e histórico de consultas.

**Requisitos**: RF05, RF06, RF07, RNF02

### Tarefas

- T4.1 — Criar entidade `Appointment` (id, patientId, professionalId, healthServiceId, scheduledAt, status, notes)
- T4.2 — Criar entidade `TimeSlot` (id, professionalId, startTime, endTime, isAvailable)
- T4.3 — Criar `AppointmentRepository` com queries por paciente, status e período
- T4.4 — Criar `AppointmentRequest` e `AppointmentResponse`
- T4.5 — Implementar `AppointmentService` (book, cancel, reschedule, history)
- T4.6 — Validar disponibilidade do slot antes de confirmar (lock otimista)
- T4.7 — Publicar evento Kafka `ConsultaAgendadaEvento` ao confirmar agendamento
- T4.8 — Publicar evento Kafka `ConsultaCanceladaEvento` ao cancelar
- T4.9 — Criar `AppointmentController` (POST /api/v1/appointments, DELETE /api/v1/appointments/{id})
- T4.10 — Criar endpoint de histórico (GET /api/v1/appointments/history)
- T4.11 — Testes unitários para `AppointmentService`
- T4.12 — Testes de integração para `AppointmentController`

**Migrações necessárias**: `V1.5__appointments_table.sql`, `V1.6__time_slots_table.sql`

---

## F5 — Avaliações e Notas (Sprint 4)

**Objetivo**: Permitir avaliações de consultas e cálculo de nota média por unidade.

**Requisitos**: RF08, RNF01

### Tarefas

- T5.1 — Criar entidade `Review` (id, patientId, appointmentId, healthServiceId, rating, comment, createdAt)
- T5.2 — Criar `ReviewRepository` com query de média por `healthServiceId`
- T5.3 — Criar `ReviewRequest` (rating, comment) e `ReviewResponse`
- T5.4 — Implementar `ReviewService` (createReview, getByService, getByPatient, calculateAverage)
- T5.5 — Validar que paciente só avalia consulta realizada
- T5.6 — Validar unicidade: um paciente, uma avaliação por consulta
- T5.7 — Atualizar `averageRating` em `HealthService` após nova avaliação (evento ou direto)
- T5.8 — Criar `ReviewController` (POST /api/v1/reviews, GET /api/v1/services/{id}/reviews)
- T5.9 — Criar endpoint de avaliações do paciente (GET /api/v1/users/me/reviews)
- T5.10 — Paginação para listagem de avaliações
- T5.11 — Testes unitários para `ReviewService`
- T5.12 — Testes de integração para `ReviewController`

**Migrações necessárias**: `V1.7__reviews_table.sql`

---

## F6 — Histórico Médico (Sprint 5)

**Objetivo**: Armazenar e exportar histórico médico de forma segura (LGPD).

**Requisitos**: RF07, RNF03

### Tarefas

- T6.1 — Criar entidade `MedicalHistory` (id, patientId, appointmentId, diagnosis, prescription, notes, createdBy)
- T6.2 — Criar entidade `Attachment` (id, historyId, fileName, filePath, fileType, uploadedAt)
- T6.3 — Criar `MedicalHistoryRepository`
- T6.4 — Criar `MedicalHistoryRequest` e `MedicalHistoryResponse`
- T6.5 — Implementar `MedicalHistoryService` (create, getByPatient, getById, exportPdf)
- T6.6 — Criptografar campos sensíveis (diagnosis, prescription) em repouso com AES-256
- T6.7 — Implementar exportação de PDF com iText ou Apache PDFBox
- T6.8 — Criar `MedicalHistoryController` (GET /api/v1/medical-history, GET /api/v1/medical-history/{id}/export)
- T6.9 — Garantir que apenas o próprio paciente ou profissional responsável acessa o histórico
- T6.10 — Registrar acesso ao histórico na tabela de auditoria
- T6.11 — Testes unitários para `MedicalHistoryService`
- T6.12 — Testes de integração para `MedicalHistoryController`

**Migrações necessárias**: `V1.8__medical_history_table.sql`, `V1.9__attachments_table.sql`

---

## F7 — Notificações (Sprint 6)

**Objetivo**: Enviar notificações por email nos eventos de agendamento e triagem.

**Requisitos**: RF02, RF05, RF06, RNF06

### Tarefas

- T7.1 — Configurar Spring Mail (SMTP via application properties)
- T7.2 — Criar templates de email com Freemarker (confirmacao, cancelamento, lembrete)
- T7.3 — Criar entidade `Notification` (id, recipientId, type, subject, body, sentAt, status)
- T7.4 — Criar `NotificationRepository`
- T7.5 — Implementar `EmailService` (sendEmail com template, retry em falhas)
- T7.6 — Implementar `NotificationService` (create, send, listByUser)
- T7.7 — Consumir eventos Kafka: `ConsultaAgendadaEvento`, `ConsultaCanceladaEvento`, `ClassificacaoRiscoCalculadaEvento`
- T7.8 — Implementar Dead Letter Queue para eventos não processados
- T7.9 — Criar `NotificationController` (GET /api/v1/notifications, PUT /api/v1/notifications/{id}/read)
- T7.10 — Lembrete automático 24h antes da consulta (Spring @Scheduled)
- T7.11 — Testes unitários para `EmailService` e `NotificationService`
- T7.12 — Testes de integração para consumidores Kafka

**Migrações necessárias**: `V2.0__notifications_table.sql`

---

## F8 — Dashboard e Relatórios (Sprint 7)

**Objetivo**: Fornecer dashboard pessoal e relatórios exportáveis.

**Requisitos**: RF10, RNF01

### Tarefas

- T8.1 — Criar DTO `DashboardResponse` (nextAppointments, recentHistory, totalAppointments, avgRating)
- T8.2 — Criar DTO `ReportFilters` (startDate, endDate, status, specialty)
- T8.3 — Implementar `DashboardService` (getDashboard, getRecommendations)
- T8.4 — Implementar `ReportService` (generateCsvReport, getAnalytics)
- T8.5 — Query otimizada para próximas consultas (máx. 5, indexada por `scheduledAt`)
- T8.6 — Query para resumo dos últimos 30 dias
- T8.7 — Implementar recomendações simples baseadas em histórico de especialidades
- T8.8 — Exportar relatório em CSV (usando Apache Commons CSV ou OpenCSV)
- T8.9 — Criar `DashboardController` (GET /api/v1/dashboard, GET /api/v1/dashboard/export)
- T8.10 — Cache de 5 minutos no dashboard (Spring Cache)
- T8.11 — Testes unitários para `DashboardService` e `ReportService`
- T8.12 — Testes de integração para `DashboardController`

**Migrações necessárias**: Nenhuma nova (apenas queries sobre tabelas existentes)
