/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.web.controller;

import br.com.fiap.susconnect.triage.core.dto.ClassifyRequest;
import br.com.fiap.susconnect.triage.core.dto.TriageOutput;
import br.com.fiap.susconnect.triage.core.dto.TriageRequest;
import br.com.fiap.susconnect.triage.core.usecase.ClassifyRiskUseCase;
import br.com.fiap.susconnect.triage.core.usecase.CreateTriageUseCase;
import br.com.fiap.susconnect.triage.core.usecase.GetTriageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Triage REST Controller */
@Slf4j
@RestController
@RequestMapping("/api/v1/triage")
@Tag(name = "Triage", description = "Clinical Triage Endpoints")
public class TriageController {

  private final CreateTriageUseCase createTriageUseCase;
  private final ClassifyRiskUseCase classifyRiskUseCase;
  private final GetTriageUseCase getTriageUseCase;

  public TriageController(
      CreateTriageUseCase createTriageUseCase,
      ClassifyRiskUseCase classifyRiskUseCase,
      GetTriageUseCase getTriageUseCase) {
    this.createTriageUseCase = createTriageUseCase;
    this.classifyRiskUseCase = classifyRiskUseCase;
    this.getTriageUseCase = getTriageUseCase;
  }

  @PostMapping
  @Operation(summary = "Create new triage for a patient")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Triage created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
    @ApiResponse(responseCode = "422", description = "Validation failed")
  })
  public ResponseEntity<TriageOutput> create(@Valid @RequestBody TriageRequest request) {
    log.info("Creating triage for patient: {}", request.patientId());
    var triage = createTriageUseCase.execute(request.patientId());
    log.info("Triage created: {}", triage.getId());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new TriageOutput(
                triage.getId(),
                triage.getPatientId(),
                triage.getRiskLevel(),
                triage.getCreatedAt(),
                triage.getUpdatedAt()));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get triage by ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Triage found"),
    @ApiResponse(responseCode = "404", description = "Triage not found")
  })
  public ResponseEntity<TriageOutput> find(@PathVariable UUID id) {
    log.info("Fetching triage: {}", id);
    var output = getTriageUseCase.execute(id);
    return ResponseEntity.ok(output);
  }

  @PatchMapping("/{id}/classify")
  @Operation(summary = "Classify triage risk level")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Risk level classified successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
    @ApiResponse(responseCode = "404", description = "Triage not found")
  })
  public ResponseEntity<TriageOutput> classify(
      @PathVariable UUID id, @Valid @RequestBody ClassifyRequest request) {
    log.info("Classifying triage {} with risk level: {}", id, request.riskLevel());
    var output = classifyRiskUseCase.execute(id, request.riskLevel());
    log.info("Triage {} classified as: {}", id, request.riskLevel());
    return ResponseEntity.ok(output);
  }
}
