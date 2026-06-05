/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.common.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for RFC 9457 Problem Details.
 *
 * <p>Handles:
 *
 * <ul>
 *   <li>{@link DomainException} - maps via {@link ProblemType} annotation
 *   <li>{@link MethodArgumentNotValidException} - Bean Validation (@Valid) failures → 422
 *   <li>{@link ConstraintViolationException} - path/query param violations → 422
 *   <li>{@link RuntimeException} - generic fallback → 400
 *   <li>{@link Exception} - unexpected errors → 500
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String PROBLEM_JSON = "application/problem+json";

  private final ExceptionToProblemMapper problemMapper;
  private final TraceIdProvider traceIdProvider;

  public GlobalExceptionHandler(
      ExceptionToProblemMapper problemMapper, TraceIdProvider traceIdProvider) {
    this.problemMapper = problemMapper;
    this.traceIdProvider = traceIdProvider;
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ProblemDetail> handleDomainException(DomainException ex, WebRequest req) {
    String traceId = traceIdProvider.getOrCreateTraceId();
    String correlationId = traceIdProvider.getOrCreateCorrelationId();
    ProblemDetail problem = problemMapper.toProblemDetail(ex, traceId, correlationId);
    return buildResponse(problem, traceId, correlationId);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidationException(
      MethodArgumentNotValidException ex, WebRequest req) {

    String traceId = traceIdProvider.getOrCreateTraceId();
    String correlationId = traceIdProvider.getOrCreateCorrelationId();

    Map<String, String> fieldErrors = new LinkedHashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      fieldErrors.put(fe.getField(), fe.getDefaultMessage());
    }

    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type("about:blank")
            .status(422)
            .title("Validation Failed")
            .detail("One or more fields failed validation. See 'errors' for details.")
            .timestamp(Instant.now())
            .traceId(traceId)
            .correlationId(correlationId)
            .extension("errors", fieldErrors)
            .build();

    return buildResponse(problem, traceId, correlationId);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest req) {

    String traceId = traceIdProvider.getOrCreateTraceId();
    String correlationId = traceIdProvider.getOrCreateCorrelationId();

    Map<String, String> violations = new LinkedHashMap<>();
    ex.getConstraintViolations()
        .forEach(cv -> violations.put(cv.getPropertyPath().toString(), cv.getMessage()));

    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type("about:blank")
            .status(422)
            .title("Validation Failed")
            .detail("One or more constraints were violated. See 'errors' for details.")
            .timestamp(Instant.now())
            .traceId(traceId)
            .correlationId(correlationId)
            .extension("errors", violations)
            .build();

    return buildResponse(problem, traceId, correlationId);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException ex, WebRequest req) {
    String traceId = traceIdProvider.getOrCreateTraceId();
    String correlationId = traceIdProvider.getOrCreateCorrelationId();

    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type("about:blank")
            .status(400)
            .title("Bad Request")
            .detail(ex.getMessage() != null ? ex.getMessage() : "An error occurred")
            .timestamp(Instant.now())
            .traceId(traceId)
            .correlationId(correlationId)
            .build();

    return buildResponse(problem, traceId, correlationId);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, WebRequest req) {
    String traceId = traceIdProvider.getOrCreateTraceId();
    String correlationId = traceIdProvider.getOrCreateCorrelationId();
    ProblemDetail problem = problemMapper.toGenericProblemDetail(ex, traceId, correlationId);
    return buildResponse(problem, 500, traceId, correlationId);
  }

  private ResponseEntity<ProblemDetail> buildResponse(
      ProblemDetail problem, String traceId, String correlationId) {
    return buildResponse(problem, problem.getStatus(), traceId, correlationId);
  }

  private ResponseEntity<ProblemDetail> buildResponse(
      ProblemDetail problem, int status, String traceId, String correlationId) {
    return ResponseEntity.status(status)
        .contentType(MediaType.parseMediaType(PROBLEM_JSON))
        .header(TraceIdProvider.TRACE_ID_HEADER, traceId)
        .header(TraceIdProvider.CORRELATION_ID_HEADER, correlationId)
        .body(problem);
  }
}
