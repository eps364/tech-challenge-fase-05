/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SearchActionOutput(
    UUID id,
    UUID territoryId,
    PreventiveFocus focus,
    String focusLabel,
    String objective,
    String responsibleTeam,
    LocalDate plannedStart,
    LocalDate plannedEnd,
    int targetCount,
    int performedCount,
    BigDecimal progressPercent,
    ActionStatus status,
    String notes,
    String resultNotes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
