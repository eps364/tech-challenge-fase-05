/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.common.exception;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Converts exceptions to RFC 9457 {@link ProblemDetail} objects.
 *
 * <p>Maps {@link ProblemType}-annotated exceptions to corresponding Problem Details, automatically
 * extracting metadata from annotations and exception data.
 *
 * @see ProblemType
 * @see DomainException
 * @see GlobalExceptionHandler
 */
@Component
public class ExceptionToProblemMapper {

  public ProblemDetail toProblemDetail(
      DomainException exception, String traceId, String correlationId) {

    ProblemType annotation = exception.getClass().getAnnotation(ProblemType.class);
    if (annotation != null) {
      return buildFromAnnotation(exception, annotation, traceId, correlationId);
    }
    return buildGenericProblem(exception, traceId, correlationId);
  }

  public ProblemDetail toGenericProblemDetail(
      Exception exception, String traceId, String correlationId) {
    return new ProblemDetailBuilder()
        .type("about:blank")
        .status(500)
        .title("Internal Server Error")
        .detail("An unexpected error occurred")
        .timestamp(Instant.now())
        .traceId(traceId)
        .correlationId(correlationId)
        .build();
  }

  private ProblemDetail buildFromAnnotation(
      DomainException exception, ProblemType annotation, String traceId, String correlationId) {

    ProblemDetailBuilder builder =
        new ProblemDetailBuilder()
            .type(annotation.type())
            .status(annotation.status().value())
            .title(annotation.title())
            .detail(exception.getMessage())
            .instance(exception.getInstance())
            .timestamp(Instant.now())
            .traceId(traceId)
            .correlationId(correlationId);

    if (exception.getExtensionData() != null) {
      addExtensionData(builder, exception.getExtensionData());
    }
    return builder.build();
  }

  private ProblemDetail buildGenericProblem(
      DomainException exception, String traceId, String correlationId) {
    return new ProblemDetailBuilder()
        .type("about:blank")
        .status(500)
        .title("Internal Server Error")
        .detail(exception.getMessage())
        .instance(exception.getInstance())
        .timestamp(Instant.now())
        .traceId(traceId)
        .correlationId(correlationId)
        .build();
  }

  @SuppressWarnings("unchecked")
  private void addExtensionData(ProblemDetailBuilder builder, Object extensionData) {
    if (extensionData instanceof Map) {
      builder.extensions((Map<String, Object>) extensionData);
    } else {
      builder.extension("extensionData", extensionData);
    }
  }

  public static String generateTraceId() {
    return UUID.randomUUID().toString();
  }
}
