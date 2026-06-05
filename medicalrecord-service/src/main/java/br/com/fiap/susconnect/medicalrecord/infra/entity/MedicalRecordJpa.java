/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Medical Record JPA Entity - Maps to database table */
@Entity
@Table(name = "medical_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordJpa {

  @Id private UUID id;

  @Column(nullable = false)
  private UUID appointmentId;

  @Column(nullable = false)
  private UUID patientId;

  @Column(columnDefinition = "TEXT")
  private String diagnosis;

  @Column(columnDefinition = "TEXT")
  private String prescription;

  @Column(name = "consultation_date")
  private LocalDateTime consultationDate;

  @Column(nullable = false, name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
