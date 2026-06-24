/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.messaging;

import br.com.fiap.common.events.TriageClassifiedEvent;
import br.com.fiap.susconnect.medicalrecord.core.usecase.CreateMedicalRecordUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** Kafka consumer for triage risk classification events. */
@Slf4j
@Component
public class TriageClassifiedEventConsumer {

  private final CreateMedicalRecordUseCase createMedicalRecordUseCase;

  public TriageClassifiedEventConsumer(CreateMedicalRecordUseCase createMedicalRecordUseCase) {
    this.createMedicalRecordUseCase = createMedicalRecordUseCase;
  }

  @KafkaListener(
      topics = "triage.risk-classification",
      groupId = "medicalrecord-service",
      containerFactory = "kafkaListenerFactory")
  public void consume(TriageClassifiedEvent event) {
    log.info(
        "Received triage classified event, creating medical record for patient: {}",
        event.patientId());
    createMedicalRecordUseCase.execute(event.triageId(), event.patientId(), null, null, null);
  }
}
