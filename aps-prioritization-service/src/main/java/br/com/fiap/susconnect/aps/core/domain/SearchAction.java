/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class SearchAction {

  private final UUID id;
  private final UUID territoryId;
  private final PreventiveFocus focus;
  private final String objective;
  private final String responsibleTeam;
  private final LocalDate plannedStart;
  private final LocalDate plannedEnd;
  private final int targetCount;
  private int performedCount;
  private ActionStatus status;
  private final String notes;
  private String resultNotes;
  private final LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private SearchAction(
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
    this.id = requireId(id, "action id");
    this.territoryId = requireId(territoryId, "territory id");
    if (focus == null) {
      throw new ApsValidationException("The preventive focus is required");
    }
    this.focus = focus;
    this.objective = requireText(objective, "objective");
    this.responsibleTeam = requireText(responsibleTeam, "responsible team");
    if (plannedStart == null || plannedEnd == null || plannedEnd.isBefore(plannedStart)) {
      throw new ApsValidationException("The planned period is invalid");
    }
    this.plannedStart = plannedStart;
    this.plannedEnd = plannedEnd;
    if (targetCount <= 0) {
      throw new ApsValidationException("The target count must be greater than zero");
    }
    if (performedCount < 0) {
      throw new ApsValidationException("The performed count cannot be negative");
    }
    if (status == null) {
      throw new ApsValidationException("The action status is required");
    }
    if (status == ActionStatus.COMPLETED && performedCount == 0) {
      throw new ApsValidationException("A completed action must have a performed count");
    }
    this.targetCount = targetCount;
    this.performedCount = performedCount;
    this.status = status;
    this.notes = normalizeNullable(notes);
    this.resultNotes = normalizeNullable(resultNotes);
    this.createdAt = requireDateTime(createdAt, "created at");
    this.updatedAt = requireDateTime(updatedAt, "updated at");
  }

  public static SearchAction create(
      UUID territoryId,
      PreventiveFocus focus,
      String objective,
      String responsibleTeam,
      LocalDate plannedStart,
      LocalDate plannedEnd,
      int targetCount,
      String notes,
      Clock clock) {
    LocalDateTime now = LocalDateTime.now(clock);
    return new SearchAction(
        UUID.randomUUID(),
        territoryId,
        focus,
        objective,
        responsibleTeam,
        plannedStart,
        plannedEnd,
        targetCount,
        0,
        ActionStatus.PLANNED,
        notes,
        null,
        now,
        now);
  }

  public static SearchAction reconstruct(
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
    return new SearchAction(
        id,
        territoryId,
        focus,
        objective,
        responsibleTeam,
        plannedStart,
        plannedEnd,
        targetCount,
        performedCount,
        status,
        notes,
        resultNotes,
        createdAt,
        updatedAt);
  }

  public void updateProgress(
      ActionStatus newStatus, int newPerformedCount, String newResultNotes, Clock clock) {
    if (status.isTerminal()) {
      throw new ApsValidationException("A completed or cancelled action cannot be updated");
    }
    if (newStatus == null) {
      throw new ApsValidationException("The action status is required");
    }
    if (newPerformedCount < 0) {
      throw new ApsValidationException("The performed count cannot be negative");
    }
    if (newStatus == ActionStatus.COMPLETED && newPerformedCount == 0) {
      throw new ApsValidationException("A completed action must have a performed count");
    }
    performedCount = newPerformedCount;
    status = newStatus;
    resultNotes = normalizeNullable(newResultNotes);
    updatedAt = LocalDateTime.now(clock);
  }

  public boolean isOverdue(LocalDate referenceDate) {
    return !status.isTerminal() && plannedEnd.isBefore(referenceDate);
  }

  public boolean isDueSoon(LocalDate referenceDate) {
    return !status.isTerminal()
        && !plannedEnd.isBefore(referenceDate)
        && !plannedEnd.isAfter(referenceDate.plusDays(7));
  }

  public BigDecimal progressPercent() {
    return BigDecimal.valueOf(performedCount)
        .multiply(BigDecimal.valueOf(100))
        .divide(BigDecimal.valueOf(targetCount), 2, RoundingMode.HALF_UP);
  }

  public UUID getId() {
    return id;
  }

  public UUID getTerritoryId() {
    return territoryId;
  }

  public PreventiveFocus getFocus() {
    return focus;
  }

  public String getObjective() {
    return objective;
  }

  public String getResponsibleTeam() {
    return responsibleTeam;
  }

  public LocalDate getPlannedStart() {
    return plannedStart;
  }

  public LocalDate getPlannedEnd() {
    return plannedEnd;
  }

  public int getTargetCount() {
    return targetCount;
  }

  public int getPerformedCount() {
    return performedCount;
  }

  public ActionStatus getStatus() {
    return status;
  }

  public String getNotes() {
    return notes;
  }

  public String getResultNotes() {
    return resultNotes;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  private static UUID requireId(UUID value, String field) {
    if (value == null) {
      throw new ApsValidationException("The " + field + " is required");
    }
    return value;
  }

  private static String requireText(String value, String field) {
    if (value == null || value.isBlank()) {
      throw new ApsValidationException("The " + field + " is required");
    }
    return value.trim();
  }

  private static String normalizeNullable(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }

  private static LocalDateTime requireDateTime(LocalDateTime value, String field) {
    if (value == null) {
      throw new ApsValidationException("The " + field + " is required");
    }
    return value;
  }
}
