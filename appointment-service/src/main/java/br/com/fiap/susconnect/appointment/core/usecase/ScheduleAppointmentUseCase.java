/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentConflictDomainException;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentValidationDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleAppointmentUseCase {

  private final AppointmentGateway appointmentGateway;

  public ScheduleAppointmentUseCase(AppointmentGateway appointmentGateway) {
    this.appointmentGateway = appointmentGateway;
  }

  public AppointmentOutput execute(UUID triageId, UUID patientId, LocalDateTime dateTime) {
    log.info("Scheduling appointment for patientId={}, triageId={}", patientId, triageId);

    if (dateTime.isBefore(LocalDateTime.now().minusSeconds(30))) {
      throw new AppointmentValidationDomainException(
          "The appointment date and time cannot be in the past");
    }

    if (appointmentGateway.existsByDateTimeAndStatus(dateTime, AppointmentStatus.CONFIRMED)) {
      throw new AppointmentConflictDomainException(
          "There is already a confirmed appointment at the requested time slot");
    }

    Appointment appointment = Appointment.create(triageId, patientId, dateTime);
    appointmentGateway.save(appointment);
    log.info("Appointment scheduled: id={}", appointment.getId());
    return toOutput(appointment);
  }

  private AppointmentOutput toOutput(Appointment a) {
    return new AppointmentOutput(
        a.getId(),
        a.getTriageId(),
        a.getPatientId(),
        a.getProfessionalId(),
        a.getDateTime(),
        a.getStatus().name(),
        a.getCreatedAt());
  }
}
