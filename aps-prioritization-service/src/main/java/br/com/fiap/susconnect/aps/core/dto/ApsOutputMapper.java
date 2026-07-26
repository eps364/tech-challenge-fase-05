/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import br.com.fiap.susconnect.aps.core.domain.PreventiveIndicator;
import br.com.fiap.susconnect.aps.core.domain.PriorityAssessment;
import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import java.util.List;

public final class ApsOutputMapper {

  private ApsOutputMapper() {}

  public static TerritorySummaryOutput toSummary(
      Territory territory, PriorityAssessment assessment, long openActionCount) {
    PreventiveIndicator attentionIndicator = assessment.mostCriticalIndicator().orElse(null);
    return new TerritorySummaryOutput(
        territory.getId(),
        territory.getCode(),
        territory.getName(),
        territory.getUnitName(),
        territory.getLinkedPopulationPercent(),
        territory.getDataCompetence().toString(),
        assessment.level(),
        attentionIndicator == null ? null : attentionIndicator.focus(),
        attentionIndicator == null ? null : attentionIndicator.focus().label(),
        openActionCount);
  }

  public static TerritoryDetailsOutput toDetails(
      Territory territory, PriorityAssessment assessment, List<SearchAction> actions) {
    List<IndicatorOutput> indicators = territory.getIndicators().stream().map(ApsOutputMapper::toIndicator).toList();
    return new TerritoryDetailsOutput(
        territory.getId(),
        territory.getCode(),
        territory.getName(),
        territory.getUnitName(),
        territory.getLinkedPopulationPercent(),
        territory.getDataCompetence().toString(),
        new PriorityOutput(assessment.level(), assessment.linkageTarget(), assessment.reasons()),
        indicators,
        actions.stream().map(ApsOutputMapper::toAction).toList());
  }

  public static IndicatorOutput toIndicator(PreventiveIndicator indicator) {
    return new IndicatorOutput(
        indicator.focus(),
        indicator.focus().label(),
        indicator.score(),
        indicator.target(),
        indicator.isBelowTarget());
  }

  public static SearchActionOutput toAction(SearchAction action) {
    return new SearchActionOutput(
        action.getId(),
        action.getTerritoryId(),
        action.getFocus(),
        action.getFocus().label(),
        action.getObjective(),
        action.getResponsibleTeam(),
        action.getPlannedStart(),
        action.getPlannedEnd(),
        action.getTargetCount(),
        action.getPerformedCount(),
        action.progressPercent(),
        action.getStatus(),
        action.getNotes(),
        action.getResultNotes(),
        action.getCreatedAt(),
        action.getUpdatedAt());
  }
}
