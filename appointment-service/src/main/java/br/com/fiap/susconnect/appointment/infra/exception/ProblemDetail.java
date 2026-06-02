package br.com.fiap.susconnect.appointment.infra.exception;

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

    private String type;
    private Integer status;
    private String title;
    private String detail;
    private String instance;
    private Instant timestamp;
    private String traceId;
    private String correlationId;
    private Map<String, Object> extensions = new HashMap<>();

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

    public ProblemDetail addExtension(String key, Object value) {
        this.extensions.put(key, value);
        return this;
    }

    public Object getExtension(String key) {
        return this.extensions.get(key);
    }

    public boolean hasExtension(String key) {
        return this.extensions.containsKey(key);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        if (status != null) map.put("status", status);
        if (title != null) map.put("title", title);
        if (detail != null) map.put("detail", detail);
        if (instance != null) map.put("instance", instance);
        if (timestamp != null) map.put("timestamp", timestamp.toString());
        if (traceId != null) map.put("traceId", traceId);
        if (correlationId != null) map.put("correlationId", correlationId);
        map.putAll(extensions);
        return map;
    }

    @Override
    public String toString() {
        return "ProblemDetail{" + "type='" + type + '\'' + ", status=" + status + ", title='" + title + '\'' + '}';
    }
}
