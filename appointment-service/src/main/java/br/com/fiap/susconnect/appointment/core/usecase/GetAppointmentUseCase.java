/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentNotFoundDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetAppointmentUseCase {

  private final AppointmentGateway appointmentGateway;

  public GetAppointmentUseCase(AppointmentGateway appointmentGateway) {
    this.appointmentGateway = appointmentGateway;
  }

  public AppointmentOutput execute(UUID id) {
    log.info("Fetching appointment id={}", id);
    Appointment appointment =
        appointmentGateway
            .findById(id)
            .orElseThrow(
                () -> new AppointmentNotFoundDomainException("Appointment not found: " + id));
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
