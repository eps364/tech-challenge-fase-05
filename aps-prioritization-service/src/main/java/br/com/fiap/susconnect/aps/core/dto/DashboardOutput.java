/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import java.time.LocalDate;
import java.util.List;

public record DashboardOutput(
    LocalDate periodStart,
    LocalDate periodEnd,
    long highPriorityTerritoryCount,
    long openActionCount,
    long completedActionCount,
    List<TerritorySummaryOutput> topPriorities,
    List<ActionAlertOutput> attentionActions) {}
