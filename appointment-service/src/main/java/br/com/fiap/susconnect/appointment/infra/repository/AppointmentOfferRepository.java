/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.repository;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOfferStatus;
import br.com.fiap.susconnect.appointment.infra.entity.AppointmentOfferJpa;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentOfferRepository extends JpaRepository<AppointmentOfferJpa, UUID> {

  List<AppointmentOfferJpa> findByCandidatePatientIdAndStatus(
      UUID candidatePatientId, AppointmentOfferStatus status);
}
