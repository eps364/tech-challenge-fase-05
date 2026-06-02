package br.com.fiap.susconnect.appointment.infra.exception;

import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Manages trace IDs and correlation IDs for request tracking.
 */
@Component
public class TraceIdProvider {

    public static final String TRACE_ID_HEADER = "X-Trace-ID";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String TRACE_ID_ATTR = "traceId";
    public static final String CORRELATION_ID_ATTR = "correlationId";

    private static final AtomicInteger CORRELATION_COUNTER = new AtomicInteger(0);

    @SuppressWarnings("all")
    public String getOrCreateTraceId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
            if (attributes != null) {
                String headerTraceId = attributes.getRequest().getHeader(TRACE_ID_HEADER);
                if (headerTraceId != null && !headerTraceId.isEmpty()) {
                    return headerTraceId;
                }
                Object existingTraceId = attributes.getAttribute(TRACE_ID_ATTR, ServletRequestAttributes.SCOPE_REQUEST);
                if (existingTraceId != null) {
                    return existingTraceId.toString();
                }
                String newTraceId = generateTraceId();
                attributes.setAttribute(TRACE_ID_ATTR, newTraceId, ServletRequestAttributes.SCOPE_REQUEST);
                return newTraceId;
            }
        } catch (Exception e) {
            // Fall back to generating new ID
        }
        return generateTraceId();
    }

    @SuppressWarnings("all")
    public String getOrCreateCorrelationId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
            if (attributes != null) {
                String headerCorrelationId = attributes.getRequest().getHeader(CORRELATION_ID_HEADER);
                if (headerCorrelationId != null && !headerCorrelationId.isEmpty()) {
                    return headerCorrelationId;
                }
                Object existingCorrelationId = attributes.getAttribute(CORRELATION_ID_ATTR, ServletRequestAttributes.SCOPE_REQUEST);
                if (existingCorrelationId != null) {
                    return existingCorrelationId.toString();
                }
                String newCorrelationId = generateCorrelationId();
                attributes.setAttribute(CORRELATION_ID_ATTR, newCorrelationId, ServletRequestAttributes.SCOPE_REQUEST);
                return newCorrelationId;
            }
        } catch (Exception e) {
            // Fall back to generating new ID
        }
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

    public String getTraceIdFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest().getHeader(TRACE_ID_HEADER);
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    public String getCorrelationIdFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest().getHeader(CORRELATION_ID_HEADER);
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
}
