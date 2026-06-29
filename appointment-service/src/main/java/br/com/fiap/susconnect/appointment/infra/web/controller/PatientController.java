/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.web.controller;

import br.com.fiap.susconnect.appointment.core.dto.PatientOutput;
import br.com.fiap.susconnect.appointment.core.usecase.ListPatientsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patients", description = "Local patient seed data")
public class PatientController {

  private final ListPatientsUseCase listPatientsUseCase;

  public PatientController(ListPatientsUseCase listPatientsUseCase) {
    this.listPatientsUseCase = listPatientsUseCase;
  }

  @GetMapping
  @Operation(summary = "List local seed patients")
  @ApiResponse(responseCode = "200", description = "Patients listed")
  public ResponseEntity<List<PatientOutput>> list() {
    return ResponseEntity.ok(listPatientsUseCase.execute());
  }
}
