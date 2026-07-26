/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import br.com.fiap.susconnect.aps.core.domain.PreventiveIndicator;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record CreateTerritoryCommand(
    String code,
    String name,
    String unitName,
    BigDecimal linkedPopulationPercent,
    YearMonth dataCompetence,
    List<PreventiveIndicator> indicators) {}
