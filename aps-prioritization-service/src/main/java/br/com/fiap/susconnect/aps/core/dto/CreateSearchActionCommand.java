/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import java.time.LocalDate;

public record CreateSearchActionCommand(
    PreventiveFocus focus,
    String objective,
    String responsibleTeam,
    LocalDate plannedStart,
    LocalDate plannedEnd,
    int targetCount,
    String notes) {}
