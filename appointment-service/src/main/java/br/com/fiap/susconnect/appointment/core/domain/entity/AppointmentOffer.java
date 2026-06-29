/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.domain.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/** Offer sent to a patient when an earlier appointment slot becomes available. */
public class AppointmentOffer {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private UUID id;
  private UUID openedAppointmentId;
  private UUID candidateAppointmentId;
  private UUID candidatePatientId;
  private LocalDateTime offeredDateTime;
  private LocalDateTime originalDateTime;
  private AppointmentOfferStatus status;
  private String message;
  private LocalDateTime createdAt;
  private LocalDateTime respondedAt;

  private AppointmentOffer() {}

  public static AppointmentOffer create(Appointment openedAppointment, Appointment candidate) {
    var offer = new AppointmentOffer();
    offer.id = UUID.randomUUID();
    offer.openedAppointmentId = openedAppointment.getId();
    offer.candidateAppointmentId = candidate.getId();
    offer.candidatePatientId = candidate.getPatientId();
    offer.offeredDateTime = openedAppointment.getDateTime();
    offer.originalDateTime = candidate.getDateTime();
    offer.status = AppointmentOfferStatus.PENDING;
    offer.message = buildMessage(openedAppointment, candidate);
    offer.createdAt = LocalDateTime.now();
    return offer;
  }

  public void accept() {
    status = AppointmentOfferStatus.ACCEPTED;
    respondedAt = LocalDateTime.now();
  }

  public void decline() {
    status = AppointmentOfferStatus.DECLINED;
    respondedAt = LocalDateTime.now();
  }

  public boolean isPending() {
    return status == AppointmentOfferStatus.PENDING;
  }

  public UUID getId() {
    return id;
  }

  public UUID getOpenedAppointmentId() {
    return openedAppointmentId;
  }

  public UUID getCandidateAppointmentId() {
    return candidateAppointmentId;
  }

  public UUID getCandidatePatientId() {
    return candidatePatientId;
  }

  public LocalDateTime getOfferedDateTime() {
    return offeredDateTime;
  }

  public LocalDateTime getOriginalDateTime() {
    return originalDateTime;
  }

  public AppointmentOfferStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getRespondedAt() {
    return respondedAt;
  }

  public static AppointmentOffer reconstruct(
      UUID id,
      UUID openedAppointmentId,
      UUID candidateAppointmentId,
      UUID candidatePatientId,
      LocalDateTime offeredDateTime,
      LocalDateTime originalDateTime,
      AppointmentOfferStatus status,
      String message,
      LocalDateTime createdAt,
      LocalDateTime respondedAt) {
    var offer = new AppointmentOffer();
    offer.id = id;
    offer.openedAppointmentId = openedAppointmentId;
    offer.candidateAppointmentId = candidateAppointmentId;
    offer.candidatePatientId = candidatePatientId;
    offer.offeredDateTime = offeredDateTime;
    offer.originalDateTime = originalDateTime;
    offer.status = status;
    offer.message = message;
    offer.createdAt = createdAt;
    offer.respondedAt = respondedAt;
    return offer;
  }

  private static String buildMessage(Appointment openedAppointment, Appointment candidate) {
    return "Uma vaga abriu para "
        + openedAppointment.getAppointmentType().label()
        + " de "
        + openedAppointment.getServiceName()
        + " em "
        + openedAppointment.getFacilityName()
        + " no horario "
        + openedAppointment.getDateTime().format(DATE_TIME_FORMATTER)
        + ". Seu horario atual e "
        + candidate.getDateTime().format(DATE_TIME_FORMATTER)
        + ". Aceite a oferta para antecipar o atendimento.";
  }
}
