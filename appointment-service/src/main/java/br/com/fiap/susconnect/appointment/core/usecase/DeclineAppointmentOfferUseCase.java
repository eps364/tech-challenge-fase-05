/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentNotFoundDomainException;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentValidationDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOffer;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutputMapper;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentOfferGateway;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeclineAppointmentOfferUseCase {

  private final AppointmentOfferGateway appointmentOfferGateway;

  public DeclineAppointmentOfferUseCase(AppointmentOfferGateway appointmentOfferGateway) {
    this.appointmentOfferGateway = appointmentOfferGateway;
  }

  public AppointmentOfferOutput execute(UUID offerId) {
    AppointmentOffer offer =
        appointmentOfferGateway
            .findById(offerId)
            .orElseThrow(
                () -> new AppointmentNotFoundDomainException("Appointment offer not found: " + offerId));

    if (!offer.isPending()) {
      throw new AppointmentValidationDomainException("Only pending offers can be declined");
    }

    offer.decline();
    appointmentOfferGateway.update(offer);
    log.info("Appointment offer {} declined", offer.getId());
    return AppointmentOfferOutputMapper.toOutput(offer);
  }
}
