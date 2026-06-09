/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.gateway.infra.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/** Fluent builder for creating RFC 9457 ProblemDetail objects. */
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
    ProblemDetailBuilder builder = new ProblemDetailBuilder();
    builder.type(problem.getType());
    builder.status(problem.getStatus());
    builder.title(problem.getTitle());
    builder.detail(problem.getDetail());
    builder.instance(problem.getInstance());
    builder.timestamp(problem.getTimestamp());
    builder.traceId(problem.getTraceId());
    builder.correlationId(problem.getCorrelationId());
    builder.extensions(new HashMap<>(problem.getExtensions()));
    return builder;
  }
}
