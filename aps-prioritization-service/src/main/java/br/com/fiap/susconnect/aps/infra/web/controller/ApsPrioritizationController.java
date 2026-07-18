/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.web.controller;

import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import br.com.fiap.susconnect.aps.core.domain.PriorityLevel;
import br.com.fiap.susconnect.aps.core.dto.ApsOutputMapper;
import br.com.fiap.susconnect.aps.core.dto.CreateSearchActionCommand;
import br.com.fiap.susconnect.aps.core.dto.CreateTerritoryCommand;
import br.com.fiap.susconnect.aps.core.dto.DashboardOutput;
import br.com.fiap.susconnect.aps.core.dto.ReplaceTerritoryIndicatorsCommand;
import br.com.fiap.susconnect.aps.core.dto.SearchActionOutput;
import br.com.fiap.susconnect.aps.core.dto.TerritoryDetailsOutput;
import br.com.fiap.susconnect.aps.core.dto.TerritorySummaryOutput;
import br.com.fiap.susconnect.aps.core.dto.UpdateSearchActionProgressCommand;
import br.com.fiap.susconnect.aps.core.usecase.CreateSearchActionUseCase;
import br.com.fiap.susconnect.aps.core.usecase.CreateTerritoryUseCase;
import br.com.fiap.susconnect.aps.core.usecase.GetDashboardUseCase;
import br.com.fiap.susconnect.aps.core.usecase.GetTerritoryDetailsUseCase;
import br.com.fiap.susconnect.aps.core.usecase.ListTerritoriesUseCase;
import br.com.fiap.susconnect.aps.core.usecase.ReplaceTerritoryIndicatorsUseCase;
import br.com.fiap.susconnect.aps.core.usecase.UpdateSearchActionProgressUseCase;
import br.com.fiap.susconnect.aps.infra.web.request.CreateSearchActionRequest;
import br.com.fiap.susconnect.aps.infra.web.request.CreateTerritoryRequest;
import br.com.fiap.susconnect.aps.infra.web.request.IndicatorRequest;
import br.com.fiap.susconnect.aps.infra.web.request.ReplaceTerritoryIndicatorsRequest;
import br.com.fiap.susconnect.aps.infra.web.request.UpdateSearchActionProgressRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "APS Prioritization", description = "Territorial active outreach prioritization")
public class ApsPrioritizationController {

  private final GetDashboardUseCase getDashboardUseCase;
  private final ListTerritoriesUseCase listTerritoriesUseCase;
  private final GetTerritoryDetailsUseCase getTerritoryDetailsUseCase;
  private final CreateTerritoryUseCase createTerritoryUseCase;
  private final ReplaceTerritoryIndicatorsUseCase replaceTerritoryIndicatorsUseCase;
  private final CreateSearchActionUseCase createSearchActionUseCase;
  private final UpdateSearchActionProgressUseCase updateSearchActionProgressUseCase;

  public ApsPrioritizationController(
      GetDashboardUseCase getDashboardUseCase,
      ListTerritoriesUseCase listTerritoriesUseCase,
      GetTerritoryDetailsUseCase getTerritoryDetailsUseCase,
      CreateTerritoryUseCase createTerritoryUseCase,
      ReplaceTerritoryIndicatorsUseCase replaceTerritoryIndicatorsUseCase,
      CreateSearchActionUseCase createSearchActionUseCase,
      UpdateSearchActionProgressUseCase updateSearchActionProgressUseCase) {
    this.getDashboardUseCase = getDashboardUseCase;
    this.listTerritoriesUseCase = listTerritoriesUseCase;
    this.getTerritoryDetailsUseCase = getTerritoryDetailsUseCase;
    this.createTerritoryUseCase = createTerritoryUseCase;
    this.replaceTerritoryIndicatorsUseCase = replaceTerritoryIndicatorsUseCase;
    this.createSearchActionUseCase = createSearchActionUseCase;
    this.updateSearchActionProgressUseCase = updateSearchActionProgressUseCase;
  }

  @GetMapping("/dashboard")
  @Operation(summary = "Get territorial prioritization dashboard")
  public ResponseEntity<DashboardOutput> getDashboard(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
    LocalDate today = LocalDate.now();
    LocalDate periodStart = from == null ? today.withDayOfMonth(1) : from;
    LocalDate periodEnd = to == null ? today : to;
    return ResponseEntity.ok(getDashboardUseCase.execute(periodStart, periodEnd));
  }

  @GetMapping("/territories")
  @Operation(summary = "List territories ordered by active outreach priority")
  public ResponseEntity<List<TerritorySummaryOutput>> listTerritories(
      @RequestParam(required = false) PriorityLevel priority,
      @RequestParam(required = false) PreventiveFocus focus) {
    return ResponseEntity.ok(listTerritoriesUseCase.execute(priority, focus));
  }

  @GetMapping("/territories/{territoryId}")
  @Operation(summary = "Get the priority explanation and action history for a territory")
  public ResponseEntity<TerritoryDetailsOutput> getTerritory(@PathVariable UUID territoryId) {
    return ResponseEntity.ok(getTerritoryDetailsUseCase.execute(territoryId));
  }

  @PostMapping("/territories")
  @Operation(summary = "Create a territory with aggregated preventive indicators")
  public ResponseEntity<TerritoryDetailsOutput> createTerritory(
      @Valid @RequestBody CreateTerritoryRequest request) {
    var territory = createTerritoryUseCase.execute(toCreateTerritoryCommand(request));
    return ResponseEntity.created(URI.create("/api/v1/territories/" + territory.getId()))
        .body(getTerritoryDetailsUseCase.execute(territory.getId()));
  }

  @PutMapping("/territories/{territoryId}/indicators")
  @Operation(summary = "Replace the aggregated indicators used to prioritize a territory")
  public ResponseEntity<TerritoryDetailsOutput> replaceTerritoryIndicators(
      @PathVariable UUID territoryId,
      @Valid @RequestBody ReplaceTerritoryIndicatorsRequest request) {
    replaceTerritoryIndicatorsUseCase.execute(territoryId, toReplaceIndicatorsCommand(request));
    return ResponseEntity.ok(getTerritoryDetailsUseCase.execute(territoryId));
  }

  @PostMapping("/territories/{territoryId}/actions")
  @Operation(summary = "Create a territorial active outreach action")
  public ResponseEntity<SearchActionOutput> createSearchAction(
      @PathVariable UUID territoryId, @Valid @RequestBody CreateSearchActionRequest request) {
    var action = createSearchActionUseCase.execute(territoryId, toCreateActionCommand(request));
    return ResponseEntity.created(URI.create("/api/v1/actions/" + action.getId()))
        .body(ApsOutputMapper.toAction(action));
  }

  @PatchMapping("/actions/{actionId}/progress")
  @Operation(summary = "Update the aggregate progress of an active outreach action")
  public ResponseEntity<SearchActionOutput> updateSearchActionProgress(
      @PathVariable UUID actionId, @Valid @RequestBody UpdateSearchActionProgressRequest request) {
    var action =
        updateSearchActionProgressUseCase.execute(
            actionId,
            new UpdateSearchActionProgressCommand(
                request.status(), request.performedCount(), request.resultNotes()));
    return ResponseEntity.ok(ApsOutputMapper.toAction(action));
  }

  private CreateTerritoryCommand toCreateTerritoryCommand(CreateTerritoryRequest request) {
    return new CreateTerritoryCommand(
        request.code(),
        request.name(),
        request.unitName(),
        request.linkedPopulationPercent(),
        YearMonth.parse(request.dataCompetence(), DateTimeFormatter.ofPattern("yyyy-MM")),
        toIndicators(request.indicators()));
  }

  private ReplaceTerritoryIndicatorsCommand toReplaceIndicatorsCommand(
      ReplaceTerritoryIndicatorsRequest request) {
    return new ReplaceTerritoryIndicatorsCommand(
        request.linkedPopulationPercent(),
        YearMonth.parse(request.dataCompetence(), DateTimeFormatter.ofPattern("yyyy-MM")),
        toIndicators(request.indicators()));
  }

  private CreateSearchActionCommand toCreateActionCommand(CreateSearchActionRequest request) {
    return new CreateSearchActionCommand(
        request.focus(),
        request.objective(),
        request.responsibleTeam(),
        request.plannedStart(),
        request.plannedEnd(),
        request.targetCount(),
        request.notes());
  }

  private List<br.com.fiap.susconnect.aps.core.domain.PreventiveIndicator> toIndicators(
      List<IndicatorRequest> requests) {
    return requests.stream()
        .map(
            request ->
                new br.com.fiap.susconnect.aps.core.domain.PreventiveIndicator(
                    request.focus(), request.score(), request.target()))
        .toList();
  }
}
