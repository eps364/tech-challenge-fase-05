package br.com.fiap.authservice.infra.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * RFC 9457 Problem Details for HTTP APIs.
 *
 * Provides a standardized format for HTTP error responses with optional extensions.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457.html">RFC 9457 Specification</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {

    /**
     * A URI reference that identifies the problem type.
     * Defaults to "about:blank" if not specified.
     */
    private String type;

    /**
     * The HTTP status code generated for this problem occurrence.
     */
    private Integer status;

    /**
     * A short, human-readable summary of the problem type.
     * Should not change between occurrences of the same problem (except for localization).
     */
    private String title;

    /**
     * A human-readable explanation specific to this problem occurrence.
     * Focuses on helping the client correct the problem.
     */
    private String detail;

    /**
     * A URI reference that identifies the specific occurrence of the problem.
     * Can be used for support reference or forensic purposes.
     */
    private String instance;

    /**
     * Timestamp when the problem occurred (RFC 9457 extension).
     * ISO 8601 format with millisecond precision.
     */
    private Instant timestamp;

    /**
     * Unique identifier for this request (RFC 9457 extension).
     * UUID v4 format for distributed tracing.
     */
    private String traceId;

    /**
     * Correlation ID linking related requests across services (RFC 9457 extension).
     * Enables tracking business transactions across service boundaries.
     */
    private String correlationId;

    /**
     * Additional problem-specific fields (RFC 9457 extension members).
     * Clients must ignore unknown extensions.
     */
    private Map<String, Object> extensions = new HashMap<>();

    // ====== Constructors ======

    public ProblemDetail() {
    }

    public ProblemDetail(String type, Integer status, String title) {
        this.type = type;
        this.status = status;
        this.title = title;
    }

    public ProblemDetail(String type, Integer status, String title, String detail) {
        this.type = type;
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

    // ====== Getters & Setters ======

    public String getType() {
        return type != null ? type : "about:blank";
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions != null ? extensions : new HashMap<>();
    }

    // ====== Extension Helper Methods ======

    /**
     * Adds a custom extension to the problem detail.
     *
     * @param key   Extension field name
     * @param value Extension field value
     * @return this (for method chaining)
     */
    public ProblemDetail addExtension(String key, Object value) {
        this.extensions.put(key, value);
        return this;
    }

    /**
     * Gets a custom extension value.
     *
     * @param key Extension field name
     * @return Extension value or null if not present
     */
    public Object getExtension(String key) {
        return this.extensions.get(key);
    }

    /**
     * Checks if an extension exists.
     *
     * @param key Extension field name
     * @return true if extension is present
     */
    public boolean hasExtension(String key) {
        return this.extensions.containsKey(key);
    }

    // ====== JSON Serialization Support ======

    /**
     * Gets all problem detail fields as a flat map (including extensions).
     * Useful for JSON serialization.
     *
     * @return Flat map of all problem detail fields
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        if (status != null) {
            map.put("status", status);
        }
        if (title != null) {
            map.put("title", title);
        }
        if (detail != null) {
            map.put("detail", detail);
        }
        if (instance != null) {
            map.put("instance", instance);
        }
        if (timestamp != null) {
            map.put("timestamp", timestamp.toString());
        }
        if (traceId != null) {
            map.put("traceId", traceId);
        }
        if (correlationId != null) {
            map.put("correlationId", correlationId);
        }
        // Add all extensions to the root map
        map.putAll(extensions);
        return map;
    }

    @Override
    public String toString() {
        return "ProblemDetail{" +
                "type='" + type + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", instance='" + instance + '\'' +
                ", timestamp=" + timestamp +
                ", traceId='" + traceId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", extensions=" + extensions +
                '}';
    }
}
