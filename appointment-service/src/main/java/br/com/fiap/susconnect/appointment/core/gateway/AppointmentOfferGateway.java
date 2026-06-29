/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.gateway;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOffer;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOfferStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentOfferGateway {

  void save(AppointmentOffer offer);

  void update(AppointmentOffer offer);

  Optional<AppointmentOffer> findById(UUID id);

  List<AppointmentOffer> findByCandidatePatientIdAndStatus(
      UUID patientId, AppointmentOfferStatus status);
}
