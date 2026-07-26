/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.entity;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "search_actions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchActionJpa {

  @Id private UUID id;

  @Column(name = "territory_id", nullable = false)
  private UUID territoryId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 40)
  private PreventiveFocus focus;

  @Column(nullable = false, length = 500)
  private String objective;

  @Column(name = "responsible_team", nullable = false, length = 160)
  private String responsibleTeam;

  @Column(name = "planned_start", nullable = false)
  private LocalDate plannedStart;

  @Column(name = "planned_end", nullable = false)
  private LocalDate plannedEnd;

  @Column(name = "target_count", nullable = false)
  private int targetCount;

  @Column(name = "performed_count", nullable = false)
  private int performedCount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private ActionStatus status;

  @Column(length = 1000)
  private String notes;

  @Column(name = "result_notes", length = 1000)
  private String resultNotes;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public SearchActionJpa(
      UUID id,
      UUID territoryId,
      PreventiveFocus focus,
      String objective,
      String responsibleTeam,
      LocalDate plannedStart,
      LocalDate plannedEnd,
      int targetCount,
      int performedCount,
      ActionStatus status,
      String notes,
      String resultNotes,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = id;
    this.territoryId = territoryId;
    this.focus = focus;
    this.objective = objective;
    this.responsibleTeam = responsibleTeam;
    this.plannedStart = plannedStart;
    this.plannedEnd = plannedEnd;
    this.targetCount = targetCount;
    this.performedCount = performedCount;
    this.status = status;
    this.notes = notes;
    this.resultNotes = resultNotes;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
