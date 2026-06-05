/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Medical Record Domain Entity - Medical records Represents complete medical record for a patient
 * consultation
 */
public class MedicalRecord {

  private UUID id;
  private UUID appointmentId;
  private UUID patientId;
  private String diagnosis;
  private String prescription;
  private LocalDateTime consultationDate;
  private LocalDateTime createdAt;

  private MedicalRecord() {}

  public static MedicalRecord create(UUID appointmentId, UUID patientId) {
    var record = new MedicalRecord();
    record.id = UUID.randomUUID();
    record.appointmentId = appointmentId;
    record.patientId = patientId;
    record.createdAt = LocalDateTime.now();
    return record;
  }

  // Getters
  public UUID getId() {
    return id;
  }

  public UUID getAppointmentId() {
    return appointmentId;
  }

  public UUID getPatientId() {
    return patientId;
  }

  public String getDiagnosis() {
    return diagnosis;
  }

  public String getPrescription() {
    return prescription;
  }

  public LocalDateTime getConsultationDate() {
    return consultationDate;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
  }

  public void setPrescription(String prescription) {
    this.prescription = prescription;
  }

  public void setConsultationDate(LocalDateTime consultationDate) {
    this.consultationDate = consultationDate;
  }
}
