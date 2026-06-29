/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.domain.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/** Appointment domain entity representing a health unit slot reservation. */
public class Appointment {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
  private static final String DEFAULT_SERVICE_NAME = "Consulta clinica";
  private static final String DEFAULT_FACILITY_NAME = "UBS Central";

  private UUID id;
  private UUID patientId;
  private UUID professionalId;
  private LocalDateTime dateTime;
  private AppointmentStatus status;
  private AppointmentType appointmentType;
  private String serviceName;
  private String facilityName;
  private String preparationNotes;
  private String patientNotification;
  private LocalDateTime lastNotifiedAt;
  private LocalDateTime rescheduledFrom;
  private String cancellationReason;
  private LocalDateTime createdAt;

  private Appointment() {}

  public static Appointment create(UUID patientId, LocalDateTime dateTime) {
    return create(
        patientId,
        null,
        dateTime,
        AppointmentType.CONSULTATION,
        DEFAULT_SERVICE_NAME,
        DEFAULT_FACILITY_NAME,
        null);
  }

  public static Appointment create(
      UUID patientId,
      UUID professionalId,
      LocalDateTime dateTime,
      AppointmentType appointmentType,
      String serviceName,
      String facilityName,
      String preparationNotes) {
    var appointment = new Appointment();
    appointment.id = UUID.randomUUID();
    appointment.patientId = patientId;
    appointment.professionalId = professionalId;
    appointment.dateTime = dateTime;
    appointment.status = AppointmentStatus.CONFIRMED;
    appointment.appointmentType =
        appointmentType == null ? AppointmentType.CONSULTATION : appointmentType;
    appointment.serviceName = normalize(serviceName, DEFAULT_SERVICE_NAME);
    appointment.facilityName = normalize(facilityName, DEFAULT_FACILITY_NAME);
    appointment.preparationNotes = normalizeNullable(preparationNotes);
    appointment.patientNotification = appointment.buildScheduledNotification();
    appointment.lastNotifiedAt = LocalDateTime.now();
    appointment.createdAt = LocalDateTime.now();
    return appointment;
  }

  public UUID getId() {
    return id;
  }

  public UUID getPatientId() {
    return patientId;
  }

  public UUID getProfessionalId() {
    return professionalId;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public AppointmentStatus getStatus() {
    return status;
  }

  public AppointmentType getAppointmentType() {
    return appointmentType;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getFacilityName() {
    return facilityName;
  }

  public String getPreparationNotes() {
    return preparationNotes;
  }

  public String getPatientNotification() {
    return patientNotification;
  }

  public LocalDateTime getLastNotifiedAt() {
    return lastNotifiedAt;
  }

  public LocalDateTime getRescheduledFrom() {
    return rescheduledFrom;
  }

  public String getCancellationReason() {
    return cancellationReason;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public void setProfessionalId(UUID professionalId) {
    this.professionalId = professionalId;
  }

  public void cancel(String reason) {
    status = AppointmentStatus.CANCELLED;
    cancellationReason = normalizeNullable(reason);
    patientNotification = buildCancellationNotification();
    lastNotifiedAt = LocalDateTime.now();
  }

  public void rescheduleTo(LocalDateTime newDateTime) {
    rescheduledFrom = dateTime;
    dateTime = newDateTime;
    status = AppointmentStatus.CONFIRMED;
    patientNotification = buildRescheduledNotification();
    lastNotifiedAt = LocalDateTime.now();
  }

  public void remindPatient(LocalDateTime notifiedAt) {
    patientNotification = buildReminderNotification();
    lastNotifiedAt = notifiedAt;
  }

  public static Appointment reconstruct(
      UUID id,
      UUID patientId,
      UUID professionalId,
      LocalDateTime dateTime,
      AppointmentStatus status,
      LocalDateTime createdAt) {
    return reconstruct(
        id,
        patientId,
        professionalId,
        dateTime,
        status,
        AppointmentType.CONSULTATION,
        DEFAULT_SERVICE_NAME,
        DEFAULT_FACILITY_NAME,
        null,
        null,
        null,
        null,
        null,
        createdAt);
  }

  public static Appointment reconstruct(
      UUID id,
      UUID patientId,
      UUID professionalId,
      LocalDateTime dateTime,
      AppointmentStatus status,
      AppointmentType appointmentType,
      String serviceName,
      String facilityName,
      String preparationNotes,
      String patientNotification,
      LocalDateTime lastNotifiedAt,
      LocalDateTime rescheduledFrom,
      String cancellationReason,
      LocalDateTime createdAt) {
    var appointment = new Appointment();
    appointment.id = id;
    appointment.patientId = patientId;
    appointment.professionalId = professionalId;
    appointment.dateTime = dateTime;
    appointment.status = status;
    appointment.appointmentType =
        appointmentType == null ? AppointmentType.CONSULTATION : appointmentType;
    appointment.serviceName = normalize(serviceName, DEFAULT_SERVICE_NAME);
    appointment.facilityName = normalize(facilityName, DEFAULT_FACILITY_NAME);
    appointment.preparationNotes = normalizeNullable(preparationNotes);
    appointment.patientNotification = patientNotification;
    appointment.lastNotifiedAt = lastNotifiedAt;
    appointment.rescheduledFrom = rescheduledFrom;
    appointment.cancellationReason = cancellationReason;
    appointment.createdAt = createdAt;
    return appointment;
  }

  private String buildScheduledNotification() {
    return baseMessage("Agendamento confirmado para");
  }

  private String buildReminderNotification() {
    return baseMessage("Lembrete de agendamento para");
  }

  private String buildRescheduledNotification() {
    return baseMessage("Agendamento antecipado para");
  }

  private String buildCancellationNotification() {
    String message =
        "Agendamento cancelado para "
            + appointmentType.label()
            + " de "
            + serviceName
            + " em "
            + facilityName
            + " no horario "
            + dateTime.format(DATE_TIME_FORMATTER)
            + ".";
    if (cancellationReason != null) {
      message += " Motivo informado: " + cancellationReason + ".";
    }
    return message;
  }

  private String baseMessage(String prefix) {
    String message =
        prefix
            + " "
            + appointmentType.label()
            + " de "
            + serviceName
            + " em "
            + facilityName
            + " no horario "
            + dateTime.format(DATE_TIME_FORMATTER)
            + ".";
    if (preparationNotes != null) {
      message += " Preparo: " + preparationNotes + ".";
    }
    return message;
  }

  private static String normalize(String value, String fallback) {
    String normalized = normalizeNullable(value);
    return normalized == null ? fallback : normalized;
  }

  private static String normalizeNullable(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }
}
