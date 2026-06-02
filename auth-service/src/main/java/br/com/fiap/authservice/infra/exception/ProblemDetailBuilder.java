package br.com.fiap.authservice.infra.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Fluent builder for creating RFC 9457 ProblemDetail objects.
 *
 * Provides a convenient API for constructing problem details with method chaining.
 *
 * Example:
 * <pre>
 * {@code
 * ProblemDetail problem = new ProblemDetailBuilder()
 *     .type("https://api.example.com/problems/triage/invalid-risk-level")
 *     .status(422)
 *     .title("Invalid Risk Level")
 *     .detail("The provided risk level is not valid")
 *     .instance("/api/triage/abc123")
 *     .timestamp(Instant.now())
 *     .traceId("uuid-123")
 *     .correlationId("req-2024-12-15-001")
 *     .extension("validValues", Arrays.asList("low", "medium", "high"))
 *     .build();
 * }
 * </pre>
 *
 * @see ProblemDetail
 */
public class ProblemDetailBuilder {

    private final ProblemDetail problemDetail = new ProblemDetail();

    /**
     * Sets the problem type URI.
     *
     * @param type URI identifying the problem type
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder type(String type) {
        problemDetail.setType(type);
        return this;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param status HTTP status code (100-599)
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder status(int status) {
        problemDetail.setStatus(status);
        return this;
    }

    /**
     * Sets the problem title.
     *
     * @param title Short, human-readable summary
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder title(String title) {
        problemDetail.setTitle(title);
        return this;
    }

    /**
     * Sets the problem detail (specific explanation).
     *
     * @param detail Human-readable explanation for this occurrence
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder detail(String detail) {
        problemDetail.setDetail(detail);
        return this;
    }

    /**
     * Sets the problem instance identifier.
     *
     * @param instance URI or identifier for this specific occurrence
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder instance(String instance) {
        problemDetail.setInstance(instance);
        return this;
    }

    /**
     * Sets the timestamp when the problem occurred.
     *
     * @param timestamp ISO 8601 instant
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder timestamp(Instant timestamp) {
        problemDetail.setTimestamp(timestamp);
        return this;
    }

    /**
     * Sets the trace ID for request tracking.
     *
     * @param traceId Unique request identifier (typically UUID)
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder traceId(String traceId) {
        problemDetail.setTraceId(traceId);
        return this;
    }

    /**
     * Sets the correlation ID for business transaction tracking.
     *
     * @param correlationId ID linking related service calls
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder correlationId(String correlationId) {
        problemDetail.setCorrelationId(correlationId);
        return this;
    }

    /**
     * Adds a custom extension field.
     *
     * @param key   Extension field name
     * @param value Extension field value
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder extension(String key, Object value) {
        problemDetail.addExtension(key, value);
        return this;
    }

    /**
     * Adds multiple custom extensions at once.
     *
     * @param extensions Map of extension fields
     * @return this builder for method chaining
     */
    public ProblemDetailBuilder extensions(Map<String, Object> extensions) {
        if (extensions != null) {
            problemDetail.getExtensions().putAll(extensions);
        }
        return this;
    }

    /**
     * Builds and returns the configured ProblemDetail.
     *
     * @return The built ProblemDetail instance
     */
    public ProblemDetail build() {
        // Validate required fields
        if (problemDetail.getStatus() == null) {
            throw new IllegalStateException("Problem status must be set");
        }

        // Auto-set timestamp if not already set
        if (problemDetail.getTimestamp() == null) {
            problemDetail.setTimestamp(Instant.now());
        }

        return problemDetail;
    }

    /**
     * Creates a new builder with the same values as the given problem detail.
     *
     * @param problem Existing ProblemDetail to clone
     * @return New builder with problem's values
     */
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
