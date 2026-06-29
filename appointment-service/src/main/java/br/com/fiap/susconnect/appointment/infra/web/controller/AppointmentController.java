/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.web.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutput;
import br.com.fiap.susconnect.appointment.core.dto.CancellationResultOutput;
import br.com.fiap.susconnect.appointment.core.dto.CannotAttendRequest;
import br.com.fiap.susconnect.appointment.core.dto.ScheduleAppointmentRequest;
import br.com.fiap.susconnect.appointment.core.usecase.AcceptAppointmentOfferUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.CancelAppointmentUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.DeclineAppointmentOfferUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.GetAppointmentUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ListAppointmentOffersByPatientUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ListAppointmentsByPatientUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.NotifyUpcomingAppointmentsUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ScheduleAppointmentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
  private final NotifyUpcomingAppointmentsUseCase notifyUpcomingAppointmentsUseCase;
  private final ListAppointmentOffersByPatientUseCase listAppointmentOffersByPatientUseCase;
  private final AcceptAppointmentOfferUseCase acceptAppointmentOfferUseCase;
  private final DeclineAppointmentOfferUseCase declineAppointmentOfferUseCase;

  public AppointmentController(
      ScheduleAppointmentUseCase scheduleAppointmentUseCase,
      GetAppointmentUseCase getAppointmentUseCase,
      CancelAppointmentUseCase cancelAppointmentUseCase,
      ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase,
      NotifyUpcomingAppointmentsUseCase notifyUpcomingAppointmentsUseCase,
      ListAppointmentOffersByPatientUseCase listAppointmentOffersByPatientUseCase,
      AcceptAppointmentOfferUseCase acceptAppointmentOfferUseCase,
      DeclineAppointmentOfferUseCase declineAppointmentOfferUseCase) {
    this.scheduleAppointmentUseCase = scheduleAppointmentUseCase;
    this.getAppointmentUseCase = getAppointmentUseCase;
    this.cancelAppointmentUseCase = cancelAppointmentUseCase;
    this.listAppointmentsByPatientUseCase = listAppointmentsByPatientUseCase;
    this.notifyUpcomingAppointmentsUseCase = notifyUpcomingAppointmentsUseCase;
    this.listAppointmentOffersByPatientUseCase = listAppointmentOffersByPatientUseCase;
    this.acceptAppointmentOfferUseCase = acceptAppointmentOfferUseCase;
    this.declineAppointmentOfferUseCase = declineAppointmentOfferUseCase;
  }

  @PostMapping
  @Operation(summary = "Schedule a new appointment")
  @ApiResponse(responseCode = "201", description = "Appointment scheduled successfully")
  public ResponseEntity<AppointmentOutput> schedule(
      @Valid @RequestBody ScheduleAppointmentRequest request) {
    log.info("POST /api/v1/appointments - scheduling for patientId={}", request.patientId());
    AppointmentOutput output =
        scheduleAppointmentUseCase.execute(
            request.patientId(),
            request.professionalId(),
            request.dateTime(),
            request.appointmentType(),
            request.serviceName(),
            request.facilityName(),
            request.preparationNotes());
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

  @PatchMapping("/{id}/cannot-attend")
  @Operation(summary = "Mark that the patient cannot attend and offer the slot to another patient")
  @ApiResponse(responseCode = "200", description = "Appointment cancelled and offer evaluated")
  public ResponseEntity<CancellationResultOutput> cannotAttend(
      @PathVariable UUID id, @RequestBody(required = false) CannotAttendRequest request) {
    log.info("PATCH /api/v1/appointments/{}/cannot-attend", id);
    String reason = request == null ? null : request.reason();
    return ResponseEntity.ok(cancelAppointmentUseCase.executeWithReallocation(id, reason));
  }

  @GetMapping
  @Operation(summary = "List appointments by patient")
  @ApiResponse(responseCode = "200", description = "Appointments listed")
  public ResponseEntity<List<AppointmentOutput>> listByPatient(@RequestParam UUID patientId) {
    log.info("GET /api/v1/appointments?patientId={}", patientId);
    return ResponseEntity.ok(listAppointmentsByPatientUseCase.execute(patientId));
  }

  @PostMapping("/notifications/reminders")
  @Operation(summary = "Generate reminder notifications for upcoming appointments")
  @ApiResponse(responseCode = "200", description = "Upcoming appointments notified")
  public ResponseEntity<List<AppointmentOutput>> notifyUpcoming(
      @RequestParam(defaultValue = "48") int hoursAhead) {
    log.info("POST /api/v1/appointments/notifications/reminders?hoursAhead={}", hoursAhead);
    return ResponseEntity.ok(notifyUpcomingAppointmentsUseCase.execute(hoursAhead));
  }

  @GetMapping("/offers")
  @Operation(summary = "List pending slot offers by patient")
  @ApiResponse(responseCode = "200", description = "Pending offers listed")
  public ResponseEntity<List<AppointmentOfferOutput>> listOffers(@RequestParam UUID patientId) {
    log.info("GET /api/v1/appointments/offers?patientId={}", patientId);
    return ResponseEntity.ok(listAppointmentOffersByPatientUseCase.execute(patientId));
  }

  @PatchMapping("/offers/{offerId}/accept")
  @Operation(summary = "Accept an appointment slot offer")
  @ApiResponse(responseCode = "200", description = "Offer accepted and appointment rescheduled")
  public ResponseEntity<AppointmentOfferOutput> acceptOffer(@PathVariable UUID offerId) {
    log.info("PATCH /api/v1/appointments/offers/{}/accept", offerId);
    return ResponseEntity.ok(acceptAppointmentOfferUseCase.execute(offerId));
  }

  @PatchMapping("/offers/{offerId}/decline")
  @Operation(summary = "Decline an appointment slot offer")
  @ApiResponse(responseCode = "200", description = "Offer declined")
  public ResponseEntity<AppointmentOfferOutput> declineOffer(@PathVariable UUID offerId) {
    log.info("PATCH /api/v1/appointments/offers/{}/decline", offerId);
    return ResponseEntity.ok(declineAppointmentOfferUseCase.execute(offerId));
  }
}
