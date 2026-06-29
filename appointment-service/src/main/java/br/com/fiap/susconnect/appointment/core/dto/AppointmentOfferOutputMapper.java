/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOffer;

public final class AppointmentOfferOutputMapper {

  private AppointmentOfferOutputMapper() {}

  public static AppointmentOfferOutput toOutput(AppointmentOffer offer) {
    return new AppointmentOfferOutput(
        offer.getId(),
        offer.getOpenedAppointmentId(),
        offer.getCandidateAppointmentId(),
        offer.getCandidatePatientId(),
        offer.getOfferedDateTime(),
        offer.getOriginalDateTime(),
        offer.getStatus().name(),
        offer.getMessage(),
        offer.getCreatedAt(),
        offer.getRespondedAt());
  }
}
