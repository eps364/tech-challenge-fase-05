/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.entity;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentType;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
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

/** Appointment JPA Entity - Maps to database table */
@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentJpa {

  @Id private UUID id;

  @Column(nullable = false)
  private UUID patientId;

  @Column private UUID professionalId;

  @Column(nullable = false, name = "date_time")
  private LocalDateTime dateTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private AppointmentStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "appointment_type", nullable = false, length = 20)
  private AppointmentType appointmentType;

  @Column(name = "service_name", nullable = false, length = 120)
  private String serviceName;

  @Column(name = "facility_name", nullable = false, length = 160)
  private String facilityName;

  @Column(name = "preparation_notes", columnDefinition = "TEXT")
  private String preparationNotes;

  @Column(name = "patient_notification", columnDefinition = "TEXT")
  private String patientNotification;

  @Column(name = "last_notified_at")
  private LocalDateTime lastNotifiedAt;

  @Column(name = "rescheduled_from")
  private LocalDateTime rescheduledFrom;

  @Column(name = "cancellation_reason", columnDefinition = "TEXT")
  private String cancellationReason;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
