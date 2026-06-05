/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.common.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Fluent builder for creating RFC 9457 {@link ProblemDetail} objects.
 *
 * <pre>{@code
 * ProblemDetail problem = new ProblemDetailBuilder()
 *     .type("https://api.example.com/problems/triage/not-found")
 *     .status(404)
 *     .title("Triage Not Found")
 *     .detail("No triage record with id abc-123")
 *     .timestamp(Instant.now())
 *     .traceId("uuid-xyz")
 *     .build();
 * }</pre>
 */
public class ProblemDetailBuilder {

  private final ProblemDetail problemDetail = new ProblemDetail();

  public ProblemDetailBuilder type(String type) {
    problemDetail.setType(type);
    return this;
  }

  public ProblemDetailBuilder status(int status) {
    problemDetail.setStatus(status);
    return this;
  }

  public ProblemDetailBuilder title(String title) {
    problemDetail.setTitle(title);
    return this;
  }

  public ProblemDetailBuilder detail(String detail) {
    problemDetail.setDetail(detail);
    return this;
  }

  public ProblemDetailBuilder instance(String instance) {
    problemDetail.setInstance(instance);
    return this;
  }

  public ProblemDetailBuilder timestamp(Instant timestamp) {
    problemDetail.setTimestamp(timestamp);
    return this;
  }

  public ProblemDetailBuilder traceId(String traceId) {
    problemDetail.setTraceId(traceId);
    return this;
  }

  public ProblemDetailBuilder correlationId(String correlationId) {
    problemDetail.setCorrelationId(correlationId);
    return this;
  }

  public ProblemDetailBuilder extension(String key, Object value) {
    problemDetail.addExtension(key, value);
    return this;
  }

  public ProblemDetailBuilder extensions(Map<String, Object> extensions) {
    if (extensions != null) {
      problemDetail.getExtensions().putAll(extensions);
    }
    return this;
  }

  public ProblemDetail build() {
    if (problemDetail.getStatus() == null) {
      throw new IllegalStateException("Problem status must be set");
    }
    if (problemDetail.getTimestamp() == null) {
      problemDetail.setTimestamp(Instant.now());
    }
    return problemDetail;
  }

  public static ProblemDetailBuilder from(ProblemDetail problem) {
    return new ProblemDetailBuilder()
        .type(problem.getType())
        .status(problem.getStatus())
        .title(problem.getTitle())
        .detail(problem.getDetail())
        .instance(problem.getInstance())
        .timestamp(problem.getTimestamp())
        .traceId(problem.getTraceId())
        .correlationId(problem.getCorrelationId())
        .extensions(new HashMap<>(problem.getExtensions()));
  }
}
