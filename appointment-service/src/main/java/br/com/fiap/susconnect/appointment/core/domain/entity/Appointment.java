/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Appointment Domain Entity - Appointment scheduling Represents a clinical appointment slot
 * reservation
 */
public class Appointment {

  private UUID id;
  private UUID triageId;
  private UUID patientId;
  private UUID professionalId;
  private LocalDateTime dateTime;
  private String status; // CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
  private LocalDateTime createdAt;

  private Appointment() {}

  public static Appointment create(UUID triageId, UUID patientId, LocalDateTime dateTime) {
    var appointment = new Appointment();
    appointment.id = UUID.randomUUID();
    appointment.triageId = triageId;
    appointment.patientId = patientId;
    appointment.dateTime = dateTime;
    appointment.status = "CONFIRMED";
    appointment.createdAt = LocalDateTime.now();
    return appointment;
  }

  // Getters
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

  public String getStatus() {
    return status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setProfessionalId(UUID professionalId) {
    this.professionalId = professionalId;
  }
}
