# Requisitos — SUS-Connect Triagem

## Status de Implementação

- ✅ Implementado
- ⚠️ Parcialmente Implementado / Em Progresso
- 🔲 Não Iniciado

## Requisitos Funcionais

### RF01 — Autenticação de Usuário ✅

- ✅ O sistema deve permitir login com email e senha
- ✅ Gerar JWT com validade de 24 horas
- ✅ Suportar refresh token para renovação sem novo login
- ✅ Bloquear acesso a todos os endpoints sem token válido

**Status**: ✅ **Implementado** — Auth Service endpoints: POST /auth/login, POST /auth/refresh, GET /auth/test/private

### RF02 — Cadastro de Usuário ⚠️

- ✅ Aceitar CPF, nome completo, email, senha e perfil (paciente, profissional, admin)
- ⚠️ Validar CPF único e formato válido (básico implementado, validação CPF completa pendente)
- ⚠️ Hash da senha com bcrypt (implementado mas custo não configurável)
- 🔲 Enviar email de confirmação após cadastro

**Status**: ⚠️ **Parcial** — Auth Service endpoint: POST /auth/register (sem validação avançada e email)

### RF03 — Busca de Serviços de Saúde 🔲

- 🔲 Listar unidades de saúde com filtros: especialidade, localização, disponibilidade
- 🔲 Suportar paginação (page, size, sort)
- 🔲 Retornar: nome, endereço, telefone, horários, avaliação média

**Status**: 🔲 **Não Iniciado**

### RF04 — Detalhes do Serviço 🔲

- 🔲 Exibir horários de funcionamento por dia da semana
- 🔲 Listar profissionais de saúde vinculados
- 🔲 Mostrar avaliações e comentários dos usuários
- 🔲 Informar disponibilidade de vagas em tempo real

**Status**: 🔲 **Não Iniciado**

### RF05 — Agendamento de Consulta ⚠️

- ⚠️ Validar disponibilidade da vaga antes de confirmar (entity layer only)
- ⚠️ Impedir agendamento duplicado para o mesmo paciente/horário (entity layer only)
- 🔲 Enviar confirmação por email com dados do agendamento
- 🔲 Liberar vaga automaticamente se não confirmada em 30 minutos

**Status**: ⚠️ **Estrutura** — Appointment Service entity layer implementada, endpoints e lógica pendentes

### RF06 — Cancelamento de Consulta 🔲

- 🔲 Permitir cancelamento com no mínimo 24 horas de antecedência
- 🔲 Liberar a vaga imediatamente após cancelamento
- 🔲 Notificar o profissional de saúde sobre o cancelamento

**Status**: 🔲 **Não Iniciado**

### RF07 — Histórico de Consultas 🔲

- 🔲 Listar consultas passadas e futuras do paciente
- 🔲 Filtrar por data, status e especialidade
- 🔲 Permitir remarcação de consulta cancelada
- 🔲 Exibir resumo clínico se disponível

**Status**: 🔲 **Não Iniciado**

### RF08 — Avaliação de Serviço 🔲

- 🔲 Permitir avaliação de 1 a 5 estrelas após consulta realizada
- 🔲 Aceitar comentário de até 500 caracteres
- 🔲 Limitar a uma avaliação por consulta por usuário
- 🔲 Calcular e atualizar média da unidade em tempo real

**Status**: 🔲 **Não Iniciado**

### RF09 — Gerenciamento de Perfil 🔲

- 🔲 Permitir atualização de nome, email, telefone e foto
- 🔲 Configurar preferências de notificação (email, push)
- 🔲 Exportar dados pessoais em formato JSON (conformidade LGPD)
- 🔲 Permitir exclusão de conta com anonimização de dados históricos

**Status**: 🔲 **Não Iniciado**

### RF10 — Dashboard Pessoal 🔲

- 🔲 Exibir próximas consultas (máx. 5)
- 🔲 Mostrar histórico resumido dos últimos 30 dias
- 🔲 Apresentar recomendações baseadas no histórico
- 🔲 Exibir métricas: total de consultas, média de avaliações dadas

**Status**: 🔲 **Não Iniciado**

---

## Requisitos Não-Funcionais

### RNF01 — Performance

- 90% das requisições respondidas em menos de 500ms
- Endpoints de busca com cache de 5 minutos (Redis ou in-memory)
- Queries de relatório com timeout máximo de 5 segundos

### RNF02 — Disponibilidade

- SLA de 99.5% de uptime (máx. 3.6 horas de downtime/mês)
- Health check em `/actuator/health` com retorno < 200ms
- Graceful shutdown: finalizar requisições em andamento antes de parar

### RNF03 — Segurança

- HTTPS obrigatório em produção
- JWT com expiração de 24h e refresh token com 7 dias
- Senhas armazenadas com bcrypt (custo 10)
- Campos sensíveis (CPF, dados médicos) criptografados em repouso
- Rate limiting: máx. 100 requisições/minuto por IP

### RNF04 — Escalabilidade

- Suportar 10.000 usuários simultâneos
- Pool de conexões PostgreSQL: mínimo 5, máximo 20
- Stateless: nenhum estado de sessão no servidor (somente JWT)
- Kafka para desacoplamento de operações assíncronas

### RNF05 — Usabilidade da API

- Documentação OpenAPI 3.0 completa e atualizada
- Swagger UI acessível em `/swagger-ui.html`
- Mensagens de erro descritivas com `code`, `message` e `details`
- Versionamento de API via path (`/api/v1/...`)

### RNF06 — Confiabilidade

- Backup automático do banco de dados a cada 6 horas
- Flyway migrations versionadas e reversíveis
- Retry automático para publicação de eventos Kafka (máx. 3 tentativas)
- Dead Letter Queue para mensagens Kafka não processadas

### RNF07 — Manutenibilidade

- Cobertura de testes mínima de 80% (linhas + branches)
- Relatório de cobertura gerado em cada build (`mvn test jacoco:report`)
- Código formatado com Spotless (Google Java Format)
- Zero warnings de compilação no build Maven

---

## Mapeamento Feature × Requisito × Status

| Feature | Sprint | Requisitos Atendidos | Status |
| --- | --- | --- | --- |
| F1: Autenticação | Sprint 0 | RF01, RF02, RNF03 | ⚠️ Parcial |
| F2: Perfil de Usuário | Sprint 1 | RF09, RNF03 | 🔲 Não Iniciado |
| F3: Busca de Serviços | Sprint 2 | RF03, RF04, RNF01 | 🔲 Não Iniciado |
| F4: Agendamento | Sprint 3 | RF05, RF06, RF07, RNF02 | ⚠️ Estrutura |
| F5: Avaliações | Sprint 4 | RF08, RNF01 | 🔲 Não Iniciado |
| F6: Histórico Médico | Sprint 5 | RF07, RNF03 | ⚠️ Estrutura |
| F7: Notificações | Sprint 6 | RF02, RF05, RF06, RNF06 | 🔲 Não Iniciado |
| F8: Dashboard | Sprint 7 | RF10, RNF01 | 🔲 Não Iniciado |
