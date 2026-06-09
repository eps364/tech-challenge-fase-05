/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentNotFoundDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CancelAppointmentUseCase {

  private final AppointmentGateway appointmentGateway;

  public CancelAppointmentUseCase(AppointmentGateway appointmentGateway) {
    this.appointmentGateway = appointmentGateway;
  }

  public AppointmentOutput execute(UUID id) {
    log.info("Cancelling appointment id={}", id);
    Appointment appointment =
        appointmentGateway
            .findById(id)
            .orElseThrow(
                () -> new AppointmentNotFoundDomainException("Appointment not found: " + id));

    if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
      log.warn("Appointment {} is already cancelled", id);
      return toOutput(appointment);
    }

    appointment.setStatus(AppointmentStatus.CANCELLED);
    appointmentGateway.update(appointment);
    log.info("Appointment {} cancelled successfully", id);
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
