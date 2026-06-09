/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.messaging;

import br.com.fiap.susconnect.triage.core.dto.TriageClassifiedEvent;
import br.com.fiap.susconnect.triage.core.gateway.TriageEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** Kafka adapter for publishing triage domain events. */
@Slf4j
@Component
public class TriageEventProducer implements TriageEventPublisher {

  public static final String TOPIC = "triage.risk-classification";

  private final KafkaTemplate<String, TriageClassifiedEvent> kafkaTemplate;

  public TriageEventProducer(KafkaTemplate<String, TriageClassifiedEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void publishClassified(TriageClassifiedEvent event) {
    log.info("Publishing TriageClassifiedEvent for triage: {}", event.triageId());
    kafkaTemplate.send(TOPIC, event.triageId().toString(), event);
  }
}
