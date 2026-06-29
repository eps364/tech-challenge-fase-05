/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentConflictDomainException;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentNotFoundDomainException;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentValidationDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOffer;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutputMapper;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentOfferGateway;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AcceptAppointmentOfferUseCase {

  private final AppointmentGateway appointmentGateway;
  private final AppointmentOfferGateway appointmentOfferGateway;

  public AcceptAppointmentOfferUseCase(
      AppointmentGateway appointmentGateway, AppointmentOfferGateway appointmentOfferGateway) {
    this.appointmentGateway = appointmentGateway;
    this.appointmentOfferGateway = appointmentOfferGateway;
  }

  public AppointmentOfferOutput execute(UUID offerId) {
    AppointmentOffer offer =
        appointmentOfferGateway
            .findById(offerId)
            .orElseThrow(
                () -> new AppointmentNotFoundDomainException("Appointment offer not found: " + offerId));

    if (!offer.isPending()) {
      throw new AppointmentValidationDomainException("Only pending offers can be accepted");
    }

    if (appointmentGateway.existsByDateTimeAndStatus(
        offer.getOfferedDateTime(), AppointmentStatus.CONFIRMED)) {
      throw new AppointmentConflictDomainException("The offered appointment slot is no longer free");
    }

    Appointment candidateAppointment =
        appointmentGateway
            .findById(offer.getCandidateAppointmentId())
            .orElseThrow(
                () ->
                    new AppointmentNotFoundDomainException(
                        "Candidate appointment not found: " + offer.getCandidateAppointmentId()));

    candidateAppointment.rescheduleTo(offer.getOfferedDateTime());
    appointmentGateway.update(candidateAppointment);
    offer.accept();
    appointmentOfferGateway.update(offer);
    log.info("Appointment offer {} accepted", offer.getId());
    return AppointmentOfferOutputMapper.toOutput(offer);
  }
}
