/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.web;

import br.com.fiap.susconnect.triage.core.dto.TriageOutput;
import br.com.fiap.susconnect.triage.core.dto.TriageRequest;
import br.com.fiap.susconnect.triage.core.usecase.CreateTriageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  public TriageController(CreateTriageUseCase createTriageUseCase) {
    this.createTriageUseCase = createTriageUseCase;
  }

  @PostMapping
  @Operation(summary = "Create new triage for a patient")
  public ResponseEntity<TriageOutput> create(@Valid @RequestBody TriageRequest request) {
    log.info("Creating triage for patient: {}", request.patientId());
    var triage = createTriageUseCase.execute(request.patientId());
    log.info("Triage created: {}", triage.getId());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new TriageOutput(triage.getId(), triage.getRiskLevel(), triage.getCreatedAt()));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get triage by ID")
  public ResponseEntity<String> find(@PathVariable UUID id) {
    log.info("Fetching triage: {}", id);
    return ResponseEntity.ok("OK");
  }
}
