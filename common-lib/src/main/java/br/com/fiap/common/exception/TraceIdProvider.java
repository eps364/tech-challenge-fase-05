/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.common.exception;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Manages trace IDs and correlation IDs for distributed request tracking.
 *
 * <p>Trace ID: unique UUID per request. Correlation ID: links related requests across services
 * using pattern {@code req-YYYY-MM-DD-NNN}.
 *
 * <p>Both IDs are extracted from incoming request headers ({@code X-Trace-ID} / {@code
 * X-Correlation-ID}) or generated automatically.
 */
@Component
public class TraceIdProvider {

  public static final String TRACE_ID_HEADER = "X-Trace-ID";
  public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
  public static final String TRACE_ID_ATTR = "traceId";
  public static final String CORRELATION_ID_ATTR = "correlationId";

  private static final AtomicInteger CORRELATION_COUNTER = new AtomicInteger(0);

  public String getOrCreateTraceId() {
    try {
      ServletRequestAttributes attrs =
          (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attrs != null) {
        String header = attrs.getRequest().getHeader(TRACE_ID_HEADER);
        if (header != null && !header.isEmpty()) return header;

        Object existing = attrs.getAttribute(TRACE_ID_ATTR, ServletRequestAttributes.SCOPE_REQUEST);
        if (existing != null) return existing.toString();

        String newId = generateTraceId();
        attrs.setAttribute(TRACE_ID_ATTR, newId, ServletRequestAttributes.SCOPE_REQUEST);
        return newId;
      }
    } catch (Exception ignored) {
      // fall through to generate
    }
    return generateTraceId();
  }

  public String getOrCreateCorrelationId() {
    try {
      ServletRequestAttributes attrs =
          (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attrs != null) {
        String header = attrs.getRequest().getHeader(CORRELATION_ID_HEADER);
        if (header != null && !header.isEmpty()) return header;

        Object existing =
            attrs.getAttribute(CORRELATION_ID_ATTR, ServletRequestAttributes.SCOPE_REQUEST);
        if (existing != null) return existing.toString();

        String newId = generateCorrelationId();
        attrs.setAttribute(CORRELATION_ID_ATTR, newId, ServletRequestAttributes.SCOPE_REQUEST);
        return newId;
      }
    } catch (Exception ignored) {
      // fall through to generate
    }
    return generateCorrelationId();
  }

  public static String generateTraceId() {
    return UUID.randomUUID().toString();
  }

  public static String generateCorrelationId() {
    return String.format("req-%s-%03d", LocalDate.now(), CORRELATION_COUNTER.incrementAndGet());
  }
}
