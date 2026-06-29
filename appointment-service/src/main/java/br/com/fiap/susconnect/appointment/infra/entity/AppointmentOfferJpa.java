/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.entity;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOfferStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointment_offer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentOfferJpa {

  @Id private UUID id;

  @Column(name = "opened_appointment_id", nullable = false)
  private UUID openedAppointmentId;

  @Column(name = "candidate_appointment_id", nullable = false)
  private UUID candidateAppointmentId;

  @Column(name = "candidate_patient_id", nullable = false)
  private UUID candidatePatientId;

  @Column(name = "offered_date_time", nullable = false)
  private LocalDateTime offeredDateTime;

  @Column(name = "original_date_time", nullable = false)
  private LocalDateTime originalDateTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private AppointmentOfferStatus status;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String message;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "responded_at")
  private LocalDateTime respondedAt;
}
