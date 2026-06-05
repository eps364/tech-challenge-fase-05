/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/** Appointment domain entity — represents a clinical appointment slot reservation. */
public class Appointment {

  private UUID id;
  private UUID triageId;
  private UUID patientId;
  private UUID professionalId;
  private LocalDateTime dateTime;
  private AppointmentStatus status;
  private LocalDateTime createdAt;

  private Appointment() {}

  public static Appointment create(UUID triageId, UUID patientId, LocalDateTime dateTime) {
    var appointment = new Appointment();
    appointment.id = UUID.randomUUID();
    appointment.triageId = triageId;
    appointment.patientId = patientId;
    appointment.dateTime = dateTime;
    appointment.status = AppointmentStatus.CONFIRMED;
    appointment.createdAt = LocalDateTime.now();
    return appointment;
  }

  public UUID getId() {
    return id;
  }

  public UUID getTriageId() {
    return triageId;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public void setProfessionalId(UUID professionalId) {
    this.professionalId = professionalId;
  }
}
