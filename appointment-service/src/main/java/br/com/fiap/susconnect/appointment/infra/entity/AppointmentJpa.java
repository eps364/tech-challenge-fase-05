package br.com.fiap.susconnect.appointment.infra.entity;

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
  private UUID triageId;

  @Column(nullable = false)
  private UUID patientId;

  @Column private UUID professionalId;

  @Column(nullable = false, name = "date_time")
  private LocalDateTime dateTime;

  @Column(nullable = false, length = 20)
  private String status;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
