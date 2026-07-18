/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import br.com.fiap.susconnect.aps.core.domain.PriorityLevel;
import java.math.BigDecimal;
import java.util.UUID;

public record TerritorySummaryOutput(
    UUID id,
    String code,
    String name,
    String unitName,
    BigDecimal linkedPopulationPercent,
    String dataCompetence,
    PriorityLevel priority,
    PreventiveFocus attentionFocus,
    String attentionFocusLabel,
    long openActionCount) {}
