/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutputMapper;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListAppointmentsByPatientUseCase {

  private final AppointmentGateway appointmentGateway;

  public ListAppointmentsByPatientUseCase(AppointmentGateway appointmentGateway) {
    this.appointmentGateway = appointmentGateway;
  }

  public List<AppointmentOutput> execute(UUID patientId) {
    log.info("Listing appointments for patientId={}", patientId);
    return appointmentGateway.findByPatientId(patientId).stream()
        .map(AppointmentOutputMapper::toOutput)
        .toList();
  }
}
