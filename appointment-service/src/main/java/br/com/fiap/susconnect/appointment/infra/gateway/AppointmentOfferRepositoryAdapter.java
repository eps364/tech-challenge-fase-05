/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.gateway;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOffer;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOfferStatus;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentOfferGateway;
import br.com.fiap.susconnect.appointment.infra.entity.AppointmentOfferJpa;
import br.com.fiap.susconnect.appointment.infra.repository.AppointmentOfferRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppointmentOfferRepositoryAdapter implements AppointmentOfferGateway {

  private final AppointmentOfferRepository appointmentOfferRepository;

  public AppointmentOfferRepositoryAdapter(AppointmentOfferRepository appointmentOfferRepository) {
    this.appointmentOfferRepository = appointmentOfferRepository;
  }

  @Override
  @Transactional
  public void save(AppointmentOffer offer) {
    appointmentOfferRepository.save(toJpa(offer));
  }

  @Override
  @Transactional
  public void update(AppointmentOffer offer) {
    appointmentOfferRepository.save(toJpa(offer));
  }

  @Override
  public Optional<AppointmentOffer> findById(UUID id) {
    return appointmentOfferRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<AppointmentOffer> findByCandidatePatientIdAndStatus(
      UUID patientId, AppointmentOfferStatus status) {
    return appointmentOfferRepository.findByCandidatePatientIdAndStatus(patientId, status).stream()
        .map(this::toDomain)
        .toList();
  }

  private AppointmentOfferJpa toJpa(AppointmentOffer offer) {
    return AppointmentOfferJpa.builder()
        .id(offer.getId())
        .openedAppointmentId(offer.getOpenedAppointmentId())
        .candidateAppointmentId(offer.getCandidateAppointmentId())
        .candidatePatientId(offer.getCandidatePatientId())
        .offeredDateTime(offer.getOfferedDateTime())
        .originalDateTime(offer.getOriginalDateTime())
        .status(offer.getStatus())
        .message(offer.getMessage())
        .createdAt(offer.getCreatedAt())
        .respondedAt(offer.getRespondedAt())
        .build();
  }

  private AppointmentOffer toDomain(AppointmentOfferJpa jpa) {
    return AppointmentOffer.reconstruct(
        jpa.getId(),
        jpa.getOpenedAppointmentId(),
        jpa.getCandidateAppointmentId(),
        jpa.getCandidatePatientId(),
        jpa.getOfferedDateTime(),
        jpa.getOriginalDateTime(),
        jpa.getStatus(),
        jpa.getMessage(),
        jpa.getCreatedAt(),
        jpa.getRespondedAt());
  }
}
