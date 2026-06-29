/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOfferStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutputMapper;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentOfferGateway;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListAppointmentOffersByPatientUseCase {

  private final AppointmentOfferGateway appointmentOfferGateway;

  public ListAppointmentOffersByPatientUseCase(AppointmentOfferGateway appointmentOfferGateway) {
    this.appointmentOfferGateway = appointmentOfferGateway;
  }

  public List<AppointmentOfferOutput> execute(UUID patientId) {
    log.info("Listing pending appointment offers for patientId={}", patientId);
    return appointmentOfferGateway
        .findByCandidatePatientIdAndStatus(patientId, AppointmentOfferStatus.PENDING)
        .stream()
        .map(AppointmentOfferOutputMapper::toOutput)
        .toList();
  }
}
