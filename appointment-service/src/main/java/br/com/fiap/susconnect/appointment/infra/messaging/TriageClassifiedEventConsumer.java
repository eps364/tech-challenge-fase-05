/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.messaging;

import br.com.fiap.common.events.TriageClassifiedEvent;
import br.com.fiap.susconnect.appointment.core.usecase.ScheduleAppointmentUseCase;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TriageClassifiedEventConsumer {

  private final ScheduleAppointmentUseCase scheduleAppointmentUseCase;

  public TriageClassifiedEventConsumer(ScheduleAppointmentUseCase scheduleAppointmentUseCase) {
    this.scheduleAppointmentUseCase = scheduleAppointmentUseCase;
  }

  @KafkaListener(
      topics = "triage.risk-classification",
      groupId = "appointment-service",
      containerFactory = "kafkaListenerContainerFactory")
  public void consume(TriageClassifiedEvent event) {
    log.info("Received triage classified event for patient: {}", event.patientId());
    LocalDateTime scheduledTime = calculateScheduledTime(event.riskLevel());
    scheduleAppointmentUseCase.execute(event.triageId(), event.patientId(), scheduledTime);
  }

  private LocalDateTime calculateScheduledTime(String riskLevel) {
    return switch (riskLevel) {
      case "RED" -> LocalDateTime.now().plusSeconds(30);
      case "ORANGE" -> LocalDateTime.now().plusMinutes(10);
      case "YELLOW" -> LocalDateTime.now().plusMinutes(60);
      case "GREEN" -> LocalDateTime.now().plusMinutes(120);
      default -> LocalDateTime.now().plusMinutes(240);
    };
  }
}
