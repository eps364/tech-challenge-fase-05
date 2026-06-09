/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.web.controller;

import br.com.fiap.susconnect.medicalrecord.core.dto.CreateMedicalRecordRequest;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.dto.UpdateMedicalRecordRequest;
import br.com.fiap.susconnect.medicalrecord.core.usecase.CreateMedicalRecordUseCase;
import br.com.fiap.susconnect.medicalrecord.core.usecase.GetMedicalRecordUseCase;
import br.com.fiap.susconnect.medicalrecord.core.usecase.ListMedicalRecordsByPatientUseCase;
import br.com.fiap.susconnect.medicalrecord.core.usecase.UpdateMedicalRecordUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Medical Records REST Controller */
@Slf4j
@RestController
@RequestMapping("/api/v1/medical-records")
@Tag(name = "Medical Records", description = "Medical Records Management Endpoints")
public class MedicalRecordController {

  private final CreateMedicalRecordUseCase createMedicalRecordUseCase;
  private final GetMedicalRecordUseCase getMedicalRecordUseCase;
  private final UpdateMedicalRecordUseCase updateMedicalRecordUseCase;
  private final ListMedicalRecordsByPatientUseCase listMedicalRecordsByPatientUseCase;

  public MedicalRecordController(
      CreateMedicalRecordUseCase createMedicalRecordUseCase,
      GetMedicalRecordUseCase getMedicalRecordUseCase,
      UpdateMedicalRecordUseCase updateMedicalRecordUseCase,
      ListMedicalRecordsByPatientUseCase listMedicalRecordsByPatientUseCase) {
    this.createMedicalRecordUseCase = createMedicalRecordUseCase;
    this.getMedicalRecordUseCase = getMedicalRecordUseCase;
    this.updateMedicalRecordUseCase = updateMedicalRecordUseCase;
    this.listMedicalRecordsByPatientUseCase = listMedicalRecordsByPatientUseCase;
  }

  @PostMapping
  @Operation(summary = "Create a new medical record")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Medical record created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
    @ApiResponse(responseCode = "422", description = "Validation failed")
  })
  public ResponseEntity<MedicalRecordOutput> create(
      @Valid @RequestBody CreateMedicalRecordRequest request) {
    log.info(
        "Creating medical record for patientId={}, appointmentId={}",
        request.patientId(),
        request.appointmentId());
    var output =
        createMedicalRecordUseCase.execute(
            request.appointmentId(),
            request.patientId(),
            request.diagnosis(),
            request.prescription(),
            request.consultationDate());
    return ResponseEntity.status(HttpStatus.CREATED).body(output);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a medical record by ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Medical record found"),
    @ApiResponse(responseCode = "404", description = "Medical record not found")
  })
  public ResponseEntity<MedicalRecordOutput> getById(@PathVariable UUID id) {
    log.info("Fetching medical record: {}", id);
    return ResponseEntity.ok(getMedicalRecordUseCase.execute(id));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update a medical record")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Medical record updated successfully"),
    @ApiResponse(responseCode = "404", description = "Medical record not found")
  })
  public ResponseEntity<MedicalRecordOutput> update(
      @PathVariable UUID id, @RequestBody UpdateMedicalRecordRequest request) {
    log.info("Updating medical record: {}", id);
    var output =
        updateMedicalRecordUseCase.execute(
            id, request.diagnosis(), request.prescription(), request.consultationDate());
    return ResponseEntity.ok(output);
  }

  @GetMapping
  @Operation(summary = "List all medical records for a patient")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of medical records returned")
  })
  public ResponseEntity<List<MedicalRecordOutput>> listByPatient(@RequestParam UUID patientId) {
    log.info("Listing medical records for patientId={}", patientId);
    return ResponseEntity.ok(listMedicalRecordsByPatientUseCase.execute(patientId));
  }
}
