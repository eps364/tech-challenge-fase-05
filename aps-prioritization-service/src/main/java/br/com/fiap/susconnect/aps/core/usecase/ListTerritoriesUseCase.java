/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import br.com.fiap.susconnect.aps.core.domain.PriorityCalculator;
import br.com.fiap.susconnect.aps.core.domain.PriorityLevel;
import br.com.fiap.susconnect.aps.core.dto.ApsOutputMapper;
import br.com.fiap.susconnect.aps.core.dto.TerritorySummaryOutput;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.util.Comparator;
import java.util.List;

public class ListTerritoriesUseCase {

  private final TerritoryGateway territoryGateway;
  private final SearchActionGateway searchActionGateway;
  private final PriorityCalculator priorityCalculator;

  public ListTerritoriesUseCase(
      TerritoryGateway territoryGateway,
      SearchActionGateway searchActionGateway,
      PriorityCalculator priorityCalculator) {
    this.territoryGateway = territoryGateway;
    this.searchActionGateway = searchActionGateway;
    this.priorityCalculator = priorityCalculator;
  }

  public List<TerritorySummaryOutput> execute(PriorityLevel priority, PreventiveFocus focus) {
    return territoryGateway.findAll().stream()
        .map(
            territory ->
                ApsOutputMapper.toSummary(
                    territory,
                    priorityCalculator.assess(territory),
                    searchActionGateway.countOpenByTerritoryId(territory.getId())))
        .filter(summary -> priority == null || summary.priority() == priority)
        .filter(summary -> focus == null || summary.attentionFocus() == focus)
        .sorted(
            Comparator.comparingInt((TerritorySummaryOutput item) -> item.priority().ordinal())
                .thenComparing(TerritorySummaryOutput::name))
        .toList();
  }
}
