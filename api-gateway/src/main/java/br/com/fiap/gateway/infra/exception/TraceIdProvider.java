package br.com.fiap.gateway.infra.exception;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

/**
 * Manages trace IDs and correlation IDs for request tracking.
 * Uses generation-only approach compatible with reactive WebFlux context.
 */
@Component
public class TraceIdProvider {

    public static final String TRACE_ID_HEADER = "X-Trace-ID";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String TRACE_ID_ATTR = "traceId";
    public static final String CORRELATION_ID_ATTR = "correlationId";

    private static final AtomicInteger CORRELATION_COUNTER = new AtomicInteger(0);

    public String getOrCreateTraceId() {
        return generateTraceId();
    }

    public String getOrCreateCorrelationId() {
        return generateCorrelationId();
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    public static String generateCorrelationId() {
        LocalDate today = LocalDate.now();
        int sequence = CORRELATION_COUNTER.incrementAndGet();
        return String.format("req-%s-%03d", today, sequence);
    }
}
