/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TerritoryDetailsOutput(
    UUID id,
    String code,
    String name,
    String unitName,
    BigDecimal linkedPopulationPercent,
    String dataCompetence,
    PriorityOutput priority,
    List<IndicatorOutput> indicators,
    List<SearchActionOutput> actions) {}
