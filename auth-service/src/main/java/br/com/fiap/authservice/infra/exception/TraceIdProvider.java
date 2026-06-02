package br.com.fiap.authservice.infra.exception;

import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Manages trace IDs and correlation IDs for request tracking.
 *
 * Trace ID: Unique identifier for each request (UUID)
 * Correlation ID: Links related requests across service boundaries
 *
 * Both IDs are:
 * - Generated if not present in request headers
 * - Propagated in X-Trace-ID and X-Correlation-ID response headers
 * - Included in all Problem Detail responses
 *
 * @see ProblemDetail
 */
@Component
public class TraceIdProvider {

    public static final String TRACE_ID_HEADER = "X-Trace-ID";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String TRACE_ID_ATTR = "traceId";
    public static final String CORRELATION_ID_ATTR = "correlationId";

    private static final AtomicInteger CORRELATION_COUNTER = new AtomicInteger(0);

    /**
     * Gets or creates a trace ID for the current request.
     *
     * Attempts to extract from request headers (X-Trace-ID),
     * or generates a new UUID v4 if not present.
     *
     * @return Trace ID (UUID format)
     */
    @SuppressWarnings("all")
    public String getOrCreateTraceId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
            if (attributes != null) {
                // Try to get from request header
                String headerTraceId = attributes.getRequest().getHeader(TRACE_ID_HEADER);
                if (headerTraceId != null && !headerTraceId.isEmpty()) {
                    return headerTraceId;
                }

                // Try to get from request attribute
                Object existingTraceId = attributes.getAttribute(TRACE_ID_ATTR, ServletRequestAttributes.SCOPE_REQUEST);
                if (existingTraceId != null) {
                    return existingTraceId.toString();
                }

                // Generate new and store in request attribute
                String newTraceId = generateTraceId();
                //noinspection ConstantConditions
                attributes.setAttribute(TRACE_ID_ATTR, newTraceId, ServletRequestAttributes.SCOPE_REQUEST);
                return newTraceId;
            }
        } catch (Exception e) {
            // Fall back to generating new ID if request context unavailable
        }

        return generateTraceId();
    }

    /**
     * Gets or creates a correlation ID for the current request.
     *
     * Attempts to extract from request headers (X-Correlation-ID),
     * or generates a new ID with pattern: req-YYYY-MM-DD-NNN
     *
     * @return Correlation ID
     */
    @SuppressWarnings("all")
    public String getOrCreateCorrelationId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
            if (attributes != null) {
                // Try to get from request header
                String headerCorrelationId = attributes.getRequest().getHeader(CORRELATION_ID_HEADER);
                if (headerCorrelationId != null && !headerCorrelationId.isEmpty()) {
                    return headerCorrelationId;
                }

                // Try to get from request attribute
                Object existingCorrelationId = attributes.getAttribute(CORRELATION_ID_ATTR, ServletRequestAttributes.SCOPE_REQUEST);
                if (existingCorrelationId != null) {
                    return existingCorrelationId.toString();
                }

                // Generate new and store in request attribute
                String newCorrelationId = generateCorrelationId();
                attributes.setAttribute(CORRELATION_ID_ATTR, newCorrelationId, ServletRequestAttributes.SCOPE_REQUEST);
                return newCorrelationId;
            }
        } catch (Exception e) {
            // Fall back to generating new ID if request context unavailable
        }

        return generateCorrelationId();
    }

    /**
     * Generates a new trace ID (UUID v4).
     *
     * @return UUID string
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a new correlation ID with pattern: req-YYYY-MM-DD-NNN
     *
     * @return Correlation ID string
     */
    public static String generateCorrelationId() {
        LocalDate today = LocalDate.now();
        int sequence = CORRELATION_COUNTER.incrementAndGet();
        return String.format("req-%s-%03d", today, sequence);
    }

    /**
     * Extracts trace ID from request headers, or returns null.
     *
     * @return Trace ID from header or null
     */
    public String getTraceIdFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest().getHeader(TRACE_ID_HEADER);
            }
        } catch (Exception e) {
            // Ignore - request context may not be available
        }
        return null;
    }

    /**
     * Extracts correlation ID from request headers, or returns null.
     *
     * @return Correlation ID from header or null
     */
    public String getCorrelationIdFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest().getHeader(CORRELATION_ID_HEADER);
            }
        } catch (Exception e) {
            // Ignore - request context may not be available
        }
        return null;
    }
}
