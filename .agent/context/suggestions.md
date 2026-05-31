# Suggestions — SUS-Connect Triagem Inteligente

## Observability

- **Log Management**: Implementar ELK Stack (Elasticsearch, Logstash, Kibana) ou Prometheus/Grafana para logging centralizado e métricas por serviço.
- **Distributed Tracing**: Adicionar Spring Cloud Sleuth/Zipkin para rastrear requisições entre microsserviços (triagem → agendamento → prontuário).
- **Health Dashboard**: Expor métricas via Spring Actuator e consolidar no Grafana para monitoramento da disponibilidade SUS.

## Architecture & DevOps

- **CQRS**: Separar modelos de leitura e escrita no `triage-service` para suportar alta concorrência de classificações.
- **Saga Pattern**: Avaliar orquestração via Saga para o fluxo triagem → agendamento → prontuário em caso de falha parcial.
- **CI/CD**: Implementar pipelines GitHub Actions para build, testes e push de imagens Docker automatizados.
- **Kubernetes**: Migrar de Docker Compose para Kubernetes para escalonamento horizontal independente por serviço.

## Security

- **Vault**: Usar HashiCorp Vault para gestão segura de segredos e variáveis de ambiente (credenciais de banco, chaves JWT).
- **mTLS**: Implementar mutual TLS para comunicação entre microsserviços dentro do cluster.
- **LGPD Compliance**: Implementar anonimização automática de dados após período de retenção e endpoint de exportação de dados pessoais (RF09).

## Domain Features

- **Notificações**: Implementar serviço de notificações por email/push para confirmações de agendamento e alertas de triagem.
- **Circuit Breaker**: Adicionar Resilience4j em todas as chamadas OpenFeign entre serviços para tolerância a falhas.
- **Event Sourcing completo**: Completar a implementação da tabela `eventos_dominio` append-only no `triage-service`.
- **Pediatric Rule**: Implementar e testar a Regra de Ouro Pediátrica (neonatos < 28 dias com febre ≥ 38°C → LARANJA automático).
