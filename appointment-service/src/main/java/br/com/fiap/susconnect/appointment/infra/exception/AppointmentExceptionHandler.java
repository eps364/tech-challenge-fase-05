/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.exception;

import br.com.fiap.common.exception.ProblemDetail;
import br.com.fiap.common.exception.ProblemDetailBuilder;
import br.com.fiap.common.exception.TraceIdProvider;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentConflictDomainException;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentNotFoundDomainException;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentValidationDomainException;
import java.time.Instant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppointmentExceptionHandler {

  private static final String PROBLEM_JSON = "application/problem+json";
  private static final String BASE_TYPE = "/problems/appointment";

  private final TraceIdProvider traceIdProvider;

  public AppointmentExceptionHandler(TraceIdProvider traceIdProvider) {
    this.traceIdProvider = traceIdProvider;
  }

  @ExceptionHandler(AppointmentNotFoundDomainException.class)
  public ResponseEntity<ProblemDetail> handleNotFound(AppointmentNotFoundDomainException ex) {
    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type(BASE_TYPE + "/not-found")
            .status(404)
            .title("Appointment Not Found")
            .detail(ex.getMessage())
            .timestamp(Instant.now())
            .traceId(traceIdProvider.getOrCreateTraceId())
            .correlationId(traceIdProvider.getOrCreateCorrelationId())
            .build();
    return ResponseEntity.status(404)
        .contentType(MediaType.parseMediaType(PROBLEM_JSON))
        .body(problem);
  }

  @ExceptionHandler(AppointmentConflictDomainException.class)
  public ResponseEntity<ProblemDetail> handleConflict(AppointmentConflictDomainException ex) {
    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type(BASE_TYPE + "/conflict")
            .status(409)
            .title("Appointment Conflict")
            .detail(ex.getMessage())
            .timestamp(Instant.now())
            .traceId(traceIdProvider.getOrCreateTraceId())
            .correlationId(traceIdProvider.getOrCreateCorrelationId())
            .build();
    return ResponseEntity.status(409)
        .contentType(MediaType.parseMediaType(PROBLEM_JSON))
        .body(problem);
  }

  @ExceptionHandler(AppointmentValidationDomainException.class)
  public ResponseEntity<ProblemDetail> handleValidation(AppointmentValidationDomainException ex) {
    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type(BASE_TYPE + "/validation-error")
            .status(422)
            .title("Appointment Validation Error")
            .detail(ex.getMessage())
            .timestamp(Instant.now())
            .traceId(traceIdProvider.getOrCreateTraceId())
            .correlationId(traceIdProvider.getOrCreateCorrelationId())
            .build();
    return ResponseEntity.status(422)
        .contentType(MediaType.parseMediaType(PROBLEM_JSON))
        .body(problem);
  }
}
