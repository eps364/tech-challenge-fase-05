package br.com.fiap.susconnect.triage.infra.entity;

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

/** Triage JPA Entity - Maps to database table */
@Entity
@Table(name = "triage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriageJpa {

  @Id private UUID id;

  @Column(nullable = false)
  private UUID patientId;

  @Column(nullable = false, length = 10)
  private String riskLevel;

  @Column(nullable = false, name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
