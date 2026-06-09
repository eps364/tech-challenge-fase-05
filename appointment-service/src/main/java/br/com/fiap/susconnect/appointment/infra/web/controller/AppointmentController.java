/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.web.controller;

import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.dto.ScheduleAppointmentRequest;
import br.com.fiap.susconnect.appointment.core.usecase.CancelAppointmentUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.GetAppointmentUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ListAppointmentsByPatientUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ScheduleAppointmentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointments", description = "Appointment Scheduling Endpoints")
public class AppointmentController {

  private final ScheduleAppointmentUseCase scheduleAppointmentUseCase;
  private final GetAppointmentUseCase getAppointmentUseCase;
  private final CancelAppointmentUseCase cancelAppointmentUseCase;
  private final ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase;

  public AppointmentController(
      ScheduleAppointmentUseCase scheduleAppointmentUseCase,
      GetAppointmentUseCase getAppointmentUseCase,
      CancelAppointmentUseCase cancelAppointmentUseCase,
      ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase) {
    this.scheduleAppointmentUseCase = scheduleAppointmentUseCase;
    this.getAppointmentUseCase = getAppointmentUseCase;
    this.cancelAppointmentUseCase = cancelAppointmentUseCase;
    this.listAppointmentsByPatientUseCase = listAppointmentsByPatientUseCase;
  }

  @PostMapping
  @Operation(summary = "Schedule a new appointment")
  @ApiResponse(responseCode = "201", description = "Appointment scheduled successfully")
  public ResponseEntity<AppointmentOutput> schedule(
      @Valid @RequestBody ScheduleAppointmentRequest request) {
    log.info("POST /api/v1/appointments - scheduling for patientId={}", request.patientId());
    AppointmentOutput output =
        scheduleAppointmentUseCase.execute(
            request.triageId(), request.patientId(), request.dateTime());
    return ResponseEntity.created(URI.create("/api/v1/appointments/" + output.id())).body(output);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get appointment by ID")
  @ApiResponse(responseCode = "200", description = "Appointment found")
  public ResponseEntity<AppointmentOutput> findById(@PathVariable UUID id) {
    log.info("GET /api/v1/appointments/{}", id);
    return ResponseEntity.ok(getAppointmentUseCase.execute(id));
  }

  @PatchMapping("/{id}/cancel")
  @Operation(summary = "Cancel an appointment")
  @ApiResponse(responseCode = "200", description = "Appointment cancelled")
  public ResponseEntity<AppointmentOutput> cancel(@PathVariable UUID id) {
    log.info("PATCH /api/v1/appointments/{}/cancel", id);
    return ResponseEntity.ok(cancelAppointmentUseCase.execute(id));
  }

  @GetMapping
  @Operation(summary = "List appointments by patient")
  @ApiResponse(responseCode = "200", description = "Appointments listed")
  public ResponseEntity<List<AppointmentOutput>> listByPatient(@RequestParam UUID patientId) {
    log.info("GET /api/v1/appointments?patientId={}", patientId);
    return ResponseEntity.ok(listAppointmentsByPatientUseCase.execute(patientId));
  }
}
