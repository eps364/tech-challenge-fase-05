/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.core.domain.PriorityCalculator;
import br.com.fiap.susconnect.aps.core.domain.PriorityLevel;
import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.dto.ActionAlertOutput;
import br.com.fiap.susconnect.aps.core.dto.ApsOutputMapper;
import br.com.fiap.susconnect.aps.core.dto.DashboardOutput;
import br.com.fiap.susconnect.aps.core.dto.TerritorySummaryOutput;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GetDashboardUseCase {

  private final TerritoryGateway territoryGateway;
  private final SearchActionGateway searchActionGateway;
  private final PriorityCalculator priorityCalculator;
  private final Clock clock;

  public GetDashboardUseCase(
      TerritoryGateway territoryGateway,
      SearchActionGateway searchActionGateway,
      PriorityCalculator priorityCalculator,
      Clock clock) {
    this.territoryGateway = territoryGateway;
    this.searchActionGateway = searchActionGateway;
    this.priorityCalculator = priorityCalculator;
    this.clock = clock;
  }

  public DashboardOutput execute(LocalDate periodStart, LocalDate periodEnd) {
    if (periodStart.isAfter(periodEnd)) {
      throw new IllegalArgumentException("The dashboard period start cannot be after its end");
    }
    List<Territory> territories = territoryGateway.findAll();
    List<SearchAction> actions = searchActionGateway.findAll();
    Map<java.util.UUID, Territory> territoryById =
        territories.stream().collect(java.util.stream.Collectors.toMap(Territory::getId, Function.identity()));

    List<TerritorySummaryOutput> summaries =
        territories.stream()
            .map(
                territory ->
                    ApsOutputMapper.toSummary(
                        territory,
                        priorityCalculator.assess(territory),
                        searchActionGateway.countOpenByTerritoryId(territory.getId())))
            .sorted(
                Comparator.comparingInt((TerritorySummaryOutput item) -> item.priority().ordinal())
                    .thenComparing(TerritorySummaryOutput::name))
            .toList();

    long completedActionCount =
        actions.stream()
            .filter(action -> action.getStatus() == ActionStatus.COMPLETED)
            .filter(
                action ->
                    !action.getUpdatedAt().toLocalDate().isBefore(periodStart)
                        && !action.getUpdatedAt().toLocalDate().isAfter(periodEnd))
            .count();
    LocalDate today = LocalDate.now(clock);
    List<ActionAlertOutput> alerts =
        actions.stream()
            .filter(action -> action.isOverdue(today) || action.isDueSoon(today))
            .sorted(Comparator.comparing(SearchAction::getPlannedEnd))
            .map(action -> toAlert(action, territoryById.get(action.getTerritoryId()), today))
            .toList();

    return new DashboardOutput(
        periodStart,
        periodEnd,
        summaries.stream().filter(item -> item.priority() == PriorityLevel.HIGH).count(),
        actions.stream().filter(action -> !action.getStatus().isTerminal()).count(),
        completedActionCount,
        summaries.stream().limit(5).toList(),
        alerts);
  }

  private ActionAlertOutput toAlert(SearchAction action, Territory territory, LocalDate today) {
    String reason = action.isOverdue(today) ? "OVERDUE" : "DUE_SOON";
    String territoryName = territory == null ? "Unknown territory" : territory.getName();
    return new ActionAlertOutput(
        action.getId(), action.getTerritoryId(), territoryName, action.getPlannedEnd(), reason);
  }
}
