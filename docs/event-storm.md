# 🏥 Event Storm - MVP SUS-Connect

Mapeamento completo de **eventos, agregados, comandos, políticas e projeções** para a plataforma SUS-Connect, estruturada em **7 serviços microservices** baseados em Event-Driven Architecture.

**Status de Implementação**:
- ✅ Estrutura de serviços e bancos de dados  
- ✅ Keycloak e autenticação
- ✅ Auth Service: endpoints básicos operacionais
- ✅ Triage Service: endpoint skeleton com entity layer
- ⚠️ Kafka topics: configurados mas sem publishers/consumers ativos ainda
- 🔲 Events: mapeamento completo definido, implementação em progresso

---

## 📊 1. Visão Macro - Fluxo End-to-End

```text
┌─────────────────────────────────────────────────────────────────────┐
│                     PLATAFORMA SUS-CONNECT                           │
│                    (API Gateway + Serviços)                          │
└─────────────────────────────────────────────────────────────────────┘
                                  │
                  ┌───────────────┼───────────────┐
                  │               │               │
                  ▼               ▼               ▼
        ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
        │   SERVIÇO 1  │ │   SERVIÇO 2  │ │   SERVIÇO 3  │
        │   TRIAGEM    │ │ AGENDAMENTO  │ │ PRONTUÁRIO   │
        │ (Manchester) │ │  (Fluxo)     │ │ (Histórico)  │
        └──────────────┘ └──────────────┘ └──────────────┘
              │ Events          │ Events         │ Events
              │                 │               │
         Eventos:           Eventos:        Eventos:
      - Chegada           - Redireção      - Persistência
      - Triagem           - Agendamento    - Evolução
      - Classif.          - Cancelamento   - Auditoria
      - Risk Score        - Repescagem     - Consulta
```

### Timeline Sequencial

```text
  t1: PacienteChegouNaUnidade
   └─> t2: TriagemIniciada
        └─> t3: SintomasCadastrados
             └─> t4: SinaisVitaisRegistrados
                  └─> t5: AlgoritmoBayesianoExecutado (Manchester)
                       └─> t6: ClassificacaoRiscoCalculada (COR)
                            │
                            ├─ Se Verde/Azul ─> t7: RedirecionamentoUbsSugerido
                            │                      └─> t8: ConsultaSolicitada
                            │                           └─> t9: AgendamentoConfirmado
                            │                                └─> t10: RegistroTriagemPersistido
                            │
                            └─ Se Vermelho/Laranja ─> t7b: AtendimentoImediatoSolicitado
```

---

## 🎭 2. Agregados / Atores Principais

| Agregado | Descrição | Responsabilidades |
| -------- | --------- | ------------------- |
| **Paciente** | Entidade central que passa por toda jornada | Dados demográficos, sintomas, sinais vitais, histórico clínico |
| **Recepção** | Primeiro contato do paciente | Validar dados de entrada, iniciar triagem |
| **Enfermeiro Triador** | Profissional que executa Manchester | Aplicar protocolo, fazer discriminadores, calcular score |
| **Médico Assistente** | Responsável pelo diagnóstico final | Confirmar classificação, prescrever, validar evolução |
| **Unidade de Saúde** | UBS ou UPA que atende | Gerir vagas, controlar capacidade, registrar auditoria |
| **Sistema de Agendamento** | Orquestrador de vagas | Buscar vagas livres, confirmar, processar cancelamentos |
| **Sistema de Prontuário** | Centraliza histórico clínico | Persistir evoluções, manter auditoria, consultar histórico |

---

## ⚡ 3. Serviço 1: TRIAGEM INTELIGENTE (Manchester Core)

### Objetivo

Automatizar triagem baseada no Protocolo de Manchester, classificando risco clínico em 5 cores e garantindo segurança pediátrica.

### Eventos de Triagem

#### **3.1 PacienteChegouNaUnidade**

- **Trigger**: Paciente fisicamente presente na UPA/UBS
- **Dados**: `pacienteId`, `unidadeSaudeId`, `horarioChegada`, `dataChegada`
- **Produtor**: Recepção (humana ou sistema de check-in)
- **Propósito**: Marcar entrada do paciente no sistema, inicializar jornada

#### **3.2 TriagemIniciada**
- **Trigger**: Enfermeiro acessa tela de triagem e inicia avaliação
- **Dados**: `triagemdId`, `pacienteId`, `enfermeiroCPF`, `horarioInicio`
- **Produtor**: Serviço de Triagem (após receber PacienteChegouNaUnidade)
- **Propósito**: Marcar que triagem começou, iniciar cronômetro de SLA

#### **3.3 SintomasPrincipaaisCadastrados**
- **Trigger**: Enfermeiro insere queixa principal do paciente
- **Dados**: `triagemdId`, `sintomaPrincipal` (string), `outrosSintomas[]`, `horaInicioDaSintomatologia`
- **Produtor**: Tela de entrada de sintomas
- **Propósito**: Capturar queixa inicial (ex: "dor no peito", "febre", "dificuldade respiratória")
- **Regra de Ouro**: Se paciente < 28 dias E febre → Flag para avaliação neonatal imediata

#### **3.4 SinaisVitaisRegistrados**
- **Trigger**: Enfermeiro mede e registra sinais vitais
- **Dados**: `triagemdId`, `temperature`, `pressaoSistolica`, `pressaoDiastolica`, `frequenciaCardiaca`, `frequenciaRespiratoria`, `nivelConsciencia`, `dor_EVA` (Escala Visual Analógica 0-10)
- **Produtor**: Equipamentos/Tela de entrada
- **Propósito**: Coletar discriminadores clínicos necessários para algoritmo Manchester

#### **3.5 AlgoritmoBayesianoExecutado** *(Protocolo Manchester)*
- **Trigger**: Após SinalisVitaisRegistrados, sistema executa fluxograma
- **Dados**: `triagemdId`, `sintomaPrincipal`, `discriminadoresAplicados[]`, `scoreParcial`, `algoritmoVersao` (ex: "manchester-v2.1")
- **Produtor**: Engine de Triagem (regras/IA)
- **Propósito**: Aplicar árvore de decisão do Manchester, coletar scores parciais
- **Regra de Ouro Pediátrica**: Se idade < 28 dias E temperatura ≥ 38°C → Score automático = LARANJA (risco grave)

#### **3.6 ClassificacaoRiscoCalculada**
- **Trigger**: Algoritmo finalizou cálculo de score
- **Dados**: `triagemdId`, `corClassificacao` (VERMELHO|LARANJA|AMARELO|VERDE|AZUL), `scoreTotal`, `tempoMaximoEsperaMinutos`, `recomendacaoFluxo` (ex: "atendimento imediato", "UBS", "observação"), `isNeonate` (boolean)
- **Produtor**: Engine de Triagem
- **Propósito**: Gerar classificação final com recomendação
- **Output Técnico**: Retornar ao frontend + publicar evento para fila de eventos

#### **3.7 ClassificacaoRiscoAlteradaManualmente**
- **Trigger**: Médico/Enfermeiro senior sobrescreve classificação automática
- **Dados**: `triagemdId`, `corAnterior`, `corNova`, `motivoAlteracao` (string), `profissionalCPF`, `dataAlteracao`
- **Produtor**: Tela de revisão de triagem
- **Propósito**: Permitir ajuste clínico quando necessário (segurança e flexibilidade)
- **Regra**: Registrar auditoria de quem alterou e por quê

#### **3.8 RegraOuroPediatricaAplicada**
- **Trigger**: Detectada criança < 28 dias com febre durante triagem
- **Dados**: `triagemdId`, `pacienteId`, `dataNascimento`, `idade_dias`, `temperatura`, `novaClassificacao` (LARANJA ou VERMELHO), `motivo` ("Neonato febril - risco automático")
- **Produtor**: Engine de Triagem (validação pediátrica)
- **Propósito**: Blind safety rule que força escalação automática para proteger neonatos
- **Nota**: Este evento marca que a "Regra de Ouro" foi aplicada (auditoria crítica)

---

## 📅 4. Serviço 2: AGENDAMENTO INTEGRADO (Fluxo Dinâmico)

### Objetivo
Gerenciar vagas de consultas, integrar redirecionamento de não-urgentes para UBS e implementar fila de repescagem.

### Eventos de Agendamento

#### **4.1 RedirecionamentoParaUbsSugerido**
- **Trigger**: ClassificacaoRiscoCalculada com cor = VERDE ou AZUL
- **Dados**: `pacienteId`, `triagemdId`, `ubsRecomendada` (geolocalização), `distanciaKm`, `proximasVagas[]` (lista com datas/horários)
- **Produtor**: Policy que escuta ClassificacaoRiscoCalculada
- **Propósito**: Oferecer redirecionamento automático para UBS ao invés de UPA
- **Fluxo**: Se paciente confirmar → ConsultaSolicitada; Se rejeitar → RegistroTriagemPersistido (sem agendamento)

#### **4.2 ConsultaSolicitada**
- **Trigger**: Paciente confirma intenção de agendar (após redirecionamento ou por iniciativa)
- **Dados**: `agendamentoId`, `pacienteId`, `tipoConsulta` (clínica geral, pediátrica, etc.), `ubsPreferida`, `periodoPreferido` (manhã/tarde), `telefonePaciente`
- **Produtor**: Serviço de Agendamento (endpoint POST /consultas)
- **Propósito**: Formalizar solicitação de agendamento no sistema

#### **4.3 VagaConsultaDisponibilidadeVerificada**
- **Trigger**: Após ConsultaSolicitada, sistema busca vagas aplicando geolocalização
- **Dados**: `agendamentoId`, `ubsId`, `proximas5Vagas[]`, `tempoEsperaEstimado_dias`, `lotacaoAtual_%`
- **Produtor**: Serviço de Agendamento (query no banco)
- **Propósito**: Validar se há capacidade, caso contrário offerecer lista de alternativas
- **Regra**: Pacientes VERDE/AZUL só agendados em UBS; AMARELO pode ir para UPA se necessário; VERMELHO/LARANJA apenas atendimento imediato

#### **4.4 GuiaEncaminhamentoValidada**
- **Trigger**: Consulta solicitada para especialista OU exame laboratorial
- **Dados**: `agendamentoId`, `guiaEncaminhamentoId`, `tipoEspecialista`, `crmProfissionalEncaminhador`, `diasValidadeGuia`
- **Produtor**: Serviço de Agendamento (validação pré-agendamento)
- **Propósito**: Garantir que especialista tem guia válida (regra SUS)
- **Regra**: Se guia inválida/expirada → Rejeitar agendamento com mensagem clara

#### **4.5 AgendamentoConfirmado**
- **Trigger**: Vaga validada, guia (se necessário) aprovada, e paciente confirma data/hora
- **Dados**: `agendamentoId`, `pacienteId`, `ubsId`, `datConsulta`, `horaConsulta`, `profissionalAtribuido` (CRM), `salaAtendimento`, `instrucoesPaciente` (texto)
- **Produtor**: Serviço de Agendamento (endpoint PUT /agendamentos/{id}/confirmar)
- **Propósito**: Confirmar agendamento, marcar vaga como ocupada
- **Output**: Enviar SMS/email com dados da consulta, gerar ID de presença

#### **4.6 AgendamentoCancelado**
- **Trigger**: Paciente cancela OU sistema cancela por inatividade
- **Dados**: `agendamentoId`, `motivo` (paciente | timeout | profissional ausente), `dataHoraCancelamento`, `usuarioCancelador`
- **Produtor**: Serviço de Agendamento (endpoint DELETE ou PATCH)
- **Propósito**: Liberar vaga e acionar política de repescagem
- **Regra**: Cancelamento dentro de 24h antes = taxa de multa (para controlar no-shows)

#### **4.7 FilaRepescagemAcionada**
- **Trigger**: AgendamentoCancelado é publicado
- **Dados**: `agendamentoId`, `vagaLiberada` (ubsId, data, hora), `fila_espera_ids[]` (próximos 5 pacientes prioritários)
- **Produtor**: Policy que escuta AgendamentoCancelado
- **Propósito**: Buscar fila de espera e atribuir próximo paciente automaticamente
- **Regra**: Ordenar por (classificacao_risco DESC, tempo_espera DESC) e oferecer primeira posição

#### **4.8 ProximoPacienteRepescagemNotificado**
- **Trigger**: FilaRepescagemAcionada encontrou paciente elegível
- **Dados**: `fila_repescagem_id`, `pacienteId`, `vagaNova`, `prazoResposta_minutos` (ex: 15 min para confirmar)
- **Produtor**: Policy de Repescagem
- **Propósito**: Avisar paciente de que vaga foi liberada para ele
- **Output**: SMS/WhatsApp com "Vaga disponível em X! Confirme em 15min"

#### **4.9 FilaEsperaAtualizada**
- **Trigger**: A cada mudança no status de agendamentos (confirmação, cancelamento, repescagem)
- **Dados**: `fila_id`, `pacientes_aguardando[]`, `posicao_paciente`, `tempoMedioEspera_dias`
- **Produtor**: Serviço de Agendamento
- **Propósito**: Manter projeção de fila atualizada em tempo real para dashboards

---

## 📂 5. Serviço 3: PRONTUÁRIO UNIFICADO (Interoperabilidade)

### Objetivo
Centralizar histórico clínico do paciente com suporte a dados flexíveis (NoSQL/JSON) e auditoria rigorosa.

### Eventos de Prontuário

#### **5.1 RegistroTriagemPersistido**
- **Trigger**: ClassificacaoRiscoCalculada + AgendamentoConfirmado (ou fim de triagem sem agendamento)
- **Dados**: `prontuario_id`, `pacienteId`, `triagemdId`, `sintomaPrincipal`, `sinaisVitais_snapshot`, `corClassificacao`, `tempoEsperaRecomendado`, `dataPersistencia`, `enfermeiroCPF_auditoria`
- **Produtor**: Serviço de Prontuário (listener de eventos da triagem)
- **Propósito**: Guardar snapshot imutável da triagem no histórico clínico

#### **5.2 EvolucaoMedicaPersistida**
- **Trigger**: Médico registra diagnóstico + conduta após atender paciente
- **Dados**: `evolucao_id`, `pacienteId`, `unidadeSaudeId`, `crmMedico`, `diagnosticoCID10`, `descricaoClinica` (JSON schema flexível), `conducaoTerapeutica`, `dataEvolucao`, `statusClinico` (alta, internação, observação)
- **Produtor**: Serviço de Prontuário (endpoint POST /prontuarios/{pacienteId}/evolucoes)
- **Propósito**: Registrar atendimento médico com dados de auditoria obrigatória
- **Nota**: Suporta schema flexível para diferentes tipos de consulta

#### **5.3 HistoricoClinicoConsultado**
- **Trigger**: Profissional acessa histórico do paciente (antes/durante atendimento)
- **Dados**: `pacienteId`, `profissionalCPF`, `dataConsulta`, `registrosRetornados` (count), `ultimasEvoluções[]` (últimas 5)
- **Produtor**: Serviço de Prontuário (endpoint GET /prontuarios/{pacienteId})
- **Propósito**: Rastrear acessos ao prontuário (compliance LGPD)
- **Regra**: Cada acesso gera log de auditoria

#### **5.4 ProntuarioUnificadoAtualizado**
- **Trigger**: Qualquer novo evento de evolução é persistido
- **Dados**: `prontuario_id`, `pacienteId`, `versao_prontuario` (versionamento), `ultimaAtualizacao`, `totalEvoluções`
- **Produtor**: Serviço de Prontuário
- **Propósito**: Marcar que prontuário foi atualizado, disparar replicação para cache/sincronização
- **Padrão**: Event Sourcing - todo evento é imutável

#### **5.5 DadosFlexiveisArmazenadosEmJSON**
- **Trigger**: EvolucaoMedicaPersistida com schema complexo/variável
- **Dados**: `evolucao_id`, `payload_json` (schema dinâmico), `tipoDocumento` (ex: "triagem_manchester", "evolucao_pediatrica", "atestado_saude")
- **Produtor**: Serviço de Prontuário
- **Propósito**: Suportar NoSQL/JSON para casos com estrutura variável
- **Exemplo Payload**:
  ```json
  {
    "tipoPaciente": "pediatrico",
    "idade_meses": 14,
    "queixa_principal": "tosse",
    "antecedentes": ["asma", "rinite"],
    "avaliacaoFisica": {
      "ausculta": "sibilos bilaterais",
      "frequenciaRespiratoria": 45
    }
  }
  ```

---

## 🎮 6. Comandos (Mapeamento Comando → Evento)

| Comando          | Agregado     | Evento Disparado                           | Descrição                          |
| ----------       | ------------ | ---------------------------------------------- | ----------------------------------- |
| `InitiarTriagem` | Triagem | `TriagemIniciada` | Enfermeiro clica "começar triagem" |
| `RegistrarSintomas` | Triagem | `SintomasPrincipaaisCadastrados` | Inserir queixa do paciente |
| `MedirSinaisVitais` | Triagem | `SinaisVitaisRegistrados` | Equipamento/teclado entra vitais |
| `CalcularClassificacao` | Triagem | `AlgoritmoBayesianoExecutado` + `ClassificacaoRiscoCalculada` | Sistema calcula Manchester |
| `AlterarClassificacaoManualmente` | Triagem | `ClassificacaoRiscoAlteradaManualmente` | Médico sobrescreve cor |
| `SolicitarConsulta` | Agendamento | `ConsultaSolicitada` | Paciente/Recepção clica "agendar" |
| `VerificaDisponibilidadeVagas` | Agendamento | `VagaConsultaDisponibilidadeVerificada` | Backend busca vagas livres |
| `ValidarGuiaEncaminhamento` | Agendamento | `GuiaEncaminhamentoValidada` | Validar guia antes de agendar |
| `ConfirmarAgendamento` | Agendamento | `AgendamentoConfirmado` | Paciente confirma data/hora |
| `CancelarAgendamento` | Agendamento | `AgendamentoCancelado` | Paciente/Admin cancela consulta |
| `AcionarRepescagem` | Agendamento | `FilaRepescagemAcionada` | Policy automática após cancelamento |
| `PersistirEvolucaoMedica` | Prontuário | `EvolucaoMedicaPersistida` | Médico salva evolução clínica |
| `ConsultarHistoricoClinico` | Prontuário | `HistoricoClinicoConsultado` | Profissional acessa histórico |

---

## 🔄 7. Políticas e Sagas (Regras de Negócio)

### Política 1: Triagem → Redirecionamento (Evento-Driven)
```
TRIGGER: ClassificacaoRiscoCalculada
CONDIÇÃO: corClassificacao IN (VERDE, AZUL)
AÇÃO:
  1. Publicar RedirecionamentoParaUbsSugerido
  2. Buscar UBS mais próxima (geolocalização)
  3. Listar 3 próximas vagas disponíveis
  4. Notificar paciente com opções
RESULTADO: Reduzir pressão em UPAs com casos leves
```

### Política 2: Neonato Febril (Regra de Ouro Pediátrica)
```
TRIGGER: SinaisvVitaisRegistrados
CONDIÇÃO: idade < 28 dias AND temperatura >= 38°C
AÇÃO:
  1. Publicar RegraOuroPediatricaAplicada
  2. Forçar corClassificacao = LARANJA ou VERMELHO
  3. Alertar médico imediatamente (bypass triagem)
  4. Registrar auditoria com motivo "Neonato febril"
RESULTADO: Blind safety rule - nenhum neonato febril fica em espera
```

### Política 3: Caso Crítico (Vermelho/Laranja)
```
TRIGGER: ClassificacaoRiscoCalculada
CONDIÇÃO: corClassificacao IN (VERMELHO, LARANJA)
AÇÃO:
  1. Publicar AtendimentoImediatoSolicitado
  2. Notificar equipe médica (via broadcast)
  3. Alocar médico + sala de emergência
  4. Pular fila de agendamento
RESULTADO: Emergências são atendidas sem delay
```

### Saga 1: Cancelamento com Repescagem (Compensação)
```
TRIGGER: AgendamentoCancelado
FLUXO:
  1. AgendamentoCancelado é publicado
  2. Policy escuta e publica FilaRepescagemAcionada
  3. Buscar fila_espera ordenada por (risco DESC, tempo_espera DESC)
  4. Se houver pacientes → ProximoPacienteRepescagemNotificado
  5. Paciente tem 15 min para confirmar
     ├─ SE confirma → Nova consulta agendada
     └─ SE não confirma → Próximo paciente da fila
  6. FilaEsperaAtualizada publicado
RESULTADO: Nenhuma vaga desperdiçada; máxima otimização
COMPENSAÇÃO: Se repescagem falha → Vaga fica aberta por X tempo antes de ser destruída
```

### Saga 2: Fluxo Completo de Atendimento (Orquestração)
```
INÍCIO: PacienteChegouNaUnidade
  ↓
  Triagem (Serviço 1)
  ├─ TriagemIniciada
  ├─ SintomasCadastrados
  ├─ SinaisVitaisRegistrados
  ├─ ClassificacaoRiscoCalculada
  │
  ├─ [CASO VERDE/AZUL]
  │  └─ RedirecionamentoParaUbsSugerido
  │     ↓
  │     Agendamento (Serviço 2)
  │     ├─ ConsultaSolicitada
  │     ├─ VagaConsultaDisponibilidadeVerificada
  │     ├─ AgendamentoConfirmado
  │     │
  │     └─ Prontuário (Serviço 3)
  │        ├─ RegistroTriagemPersistido
  │        └─ ProntuarioUnificadoAtualizado
  │
  └─ [CASO VERMELHO/LARANJA]
     └─ AtendimentoImediatoSolicitado
        ↓
        Médico atende direto
        ↓
        Prontuário (Serviço 3)
        └─ EvolucaoMedicaPersistida
           └─ ProntuarioUnificadoAtualizado

FIM: Paciente alta/internação/observação
```

---

## 📊 8. Projeções / Views de Leitura (CQRS)

### Projeção 1: Dashboard Triagem
```
Métrica | Realtime | SLA
--------|----------|------
Pacientes em triagem (agora) | 12 | 
Tempo médio triagem | 8 min | < 10 min
% Vermelho | 2% | < 5%
% Laranja | 8% | < 15%
% Amarelo | 25% | 20-30%
% Verde | 40% | 30-50%
% Azul | 25% | 20-30%
Regras Ouro Pediátrica aplicadas (hoje) | 3 | auditoria
```

### Projeção 2: Dashboard Agendamento
```
Métrica | Value | Target
--------|-------|--------
Vagas disponíveis (próximos 7 dias) | 145 | >= 100
Taxa de ocupação UBS | 78% | 70-85%
Taxa de ocupação UPA | 92% | <= 90%
Agendamentos cancelados (semana) | 12 | < 10
Taxa de repescagem bem-sucedida | 85% | > 80%
Tempo médio de espera (VERDE) | 3 dias | <= 7 dias
Tempo médio de espera (AZUL) | 7 dias | <= 15 dias
```

### Projeção 3: Dashboard Prontuário
```
Métrica | Count | Trend
--------|-------|-------
Acessos auditados (hoje) | 342 | ↑ 5%
Evoluções registradas (hoje) | 87 | ↓ 2%
Neonatos atendidos (semana) | 15 | ↔
Especialidades mais comuns | Pediátrica (35%), Clínica (28%) | histórico
Compliance LGPD | 100% | manter
```

### Projeção 4: Visualização Territorial (Geolocalização)
```
Mapa com:
- Posição do paciente (geoloc)
- UBS mais próximas com vagas
- Distância + tempo estimado
- Capacidade de cada UBS (% ocupação)
- Triagem em progresso em tempo real
```

### Projeção 5: Fila de Espera Virtual
```
Posição | Paciente ID | Risco | Tempo Espera | Status Notificação
--------|-------------|-------|--------------|-------------------
1 | PAC-001 | Amarelo | 45 min | SMS enviado
2 | PAC-002 | Verde | 2 dias | Aguardando vaga
3 | PAC-003 | Verde | 3 dias | Aguardando vaga
```

---

## ❓ 9. Pink Stickies (Questões Abertas / Decisões Futuras)

### Questões Técnicas
- [ ] **Persistência de Eventos**: Usar PostgreSQL (Event Store) ou MongoDB (Event Log)? Considerar CQRS completo?
- [ ] **Orquestração de Sagas**: Usar Orchestrator pattern (serviço centralizado) ou Choreography (eventos)? Se Orchestrator, qual tecnologia (Temporal, Axon)?
- [ ] **Auditoria LGPD**: Criptografar CPF/cartão SUS em repouso? Implementar direito ao esquecimento?
- [ ] **Geolocalização**: Integrar com Google Maps API ou usar aproximação por CEP?
- [ ] **Real-time**: Usar WebSockets (SSE) para notificações de repescagem ou polling a cada X segundos?
- [ ] **Escalabilidade**: Serviços rodarem como microsserviços (Docker + K8s) ou monólito modular (single app, múltiplos módulos)?

### Questões de Negócio
- [ ] **Validação Guia Encaminhamento**: Integrar com sistema de guias do SUS? Qual API/serviço?
- [ ] **Regras de Classificação Manchester**: Validar com enfermeiros reais? Testar com casos clínicos reais?
- [ ] **Repescagem SLA**: Se paciente não confirma repescagem em 15min, vai para terceiro? Qual é o SLA máximo?
- [ ] **Taxa de Multa**: Aplicar multa por cancelamento <24h? Qual valor? Como registrar?
- [ ] **UBS Referência**: Paciente sempre agendado em UBS do seu bairro ou pode escolher qualquer UBS?
- [ ] **Histórico Transacional**: Por quanto tempo manter histórico de "triagens sem agendamento"? 6 meses? 1 ano?

### Questões de Domínio
- [ ] **CID-10 vs CID-11**: Usar CID-10 (SUS usa) ou migrar para CID-11 aos poucos?
- [ ] **Protocolo Manchester v2 vs v3**: Qual versão será implementada? Diferenças nos discriminadores?
- [ ] **Pacientes Recorrentes**: Se paciente chega 3x/semana, há limite? Encaminhar para acompanhamento contínuo?
- [ ] **Dados Flexíveis Prontuário**: Qual JSON schema validar para evoluções? Ter um schema por especialidade?

---

## 🎯 10. Próximos Passos

1. **Validação de Eventos**: Apresentar este mapa para stakeholders (médicos, enfermeiros, gestores)
2. **Definição de Agregados**: Criar bounded contexts (DDD) para cada serviço
3. **Tecnologias**: Escolher stack técnico (ex: Spring Boot + Kafka + PostgreSQL)
4. **Implementação**: Começar pelo Serviço 1 (Triagem) como MVP
5. **Testes**: Casos clínicos reais para validar fluxos
6. **Video Demonstração**: Gravar fluxo completo no Postman para banca

---

## 📚 Referências

- **Protocolo Manchester**: http://emergencynurse.ru/articles/2018/manchester.pdf
- **Domain-Driven Design**: Evans (2003)
- **Event Sourcing + CQRS**: Microsoft docs
- **SUS - Sistema Único de Saúde**: http://portalarquivos.saude.gov.br/