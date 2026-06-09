/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.infra.exception;

import br.com.fiap.authservice.core.domain.ValidationException;
import br.com.fiap.common.exception.ProblemDetail;
import br.com.fiap.common.exception.ProblemDetailBuilder;
import br.com.fiap.common.exception.TraceIdProvider;
import java.time.Instant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps {@link ValidationException} (thrown by core domain entities) to HTTP 400 Bad Request.
 *
 * <p>Takes precedence over the generic {@code DomainException} handler in {@code
 * GlobalExceptionHandler} because Spring selects the most specific exception type.
 */
@RestControllerAdvice
public class AuthExceptionHandler {

  private static final String PROBLEM_JSON = "application/problem+json";
  private static final String PROBLEM_TYPE =
      "https://api.example.com/problems/auth/validation-error";

  private final TraceIdProvider traceIdProvider;

  public AuthExceptionHandler(TraceIdProvider traceIdProvider) {
    this.traceIdProvider = traceIdProvider;
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ProblemDetail> handleValidationException(ValidationException ex) {
    String traceId = traceIdProvider.getOrCreateTraceId();
    String correlationId = traceIdProvider.getOrCreateCorrelationId();

    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type(PROBLEM_TYPE)
            .status(400)
            .title("Validation Error")
            .detail(ex.getMessage())
            .instance(ex.getInstance())
            .timestamp(Instant.now())
            .traceId(traceId)
            .correlationId(correlationId)
            .build();

    return ResponseEntity.status(400)
        .contentType(MediaType.parseMediaType(PROBLEM_JSON))
        .header(TraceIdProvider.TRACE_ID_HEADER, traceId)
        .header(TraceIdProvider.CORRELATION_ID_HEADER, correlationId)
        .body(problem);
  }
}
