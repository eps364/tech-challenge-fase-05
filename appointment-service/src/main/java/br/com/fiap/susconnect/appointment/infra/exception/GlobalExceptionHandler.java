package br.com.fiap.susconnect.appointment.infra.exception;

import java.time.Instant;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for RFC 9457 Problem Details.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionToProblemMapper problemMapper;
    private final TraceIdProvider traceIdProvider;

    public GlobalExceptionHandler(ExceptionToProblemMapper problemMapper, TraceIdProvider traceIdProvider) {
        this.problemMapper = problemMapper;
        this.traceIdProvider = traceIdProvider;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomainException(
            DomainException ex,
            WebRequest request) {

        String traceId = traceIdProvider.getOrCreateTraceId();
        String correlationId = traceIdProvider.getOrCreateCorrelationId();

        ProblemDetail problem = problemMapper.toProblemDetail(ex, traceId, correlationId);

        return ResponseEntity
                .status(problem.getStatus())
                .contentType(MediaType.parseMediaType("application/problem+json"))
                .header(TraceIdProvider.TRACE_ID_HEADER, traceId)
                .header(TraceIdProvider.CORRELATION_ID_HEADER, correlationId)
                .body(problem);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(
            RuntimeException ex,
            WebRequest request) {

        String traceId = traceIdProvider.getOrCreateTraceId();
        String correlationId = traceIdProvider.getOrCreateCorrelationId();

        ProblemDetail problem = new ProblemDetailBuilder()
                .type("about:blank")
                .status(400)
                .title("Bad Request")
                .detail(ex.getMessage() != null ? ex.getMessage() : "An error occurred")
                .timestamp(Instant.now())
                .traceId(traceId)
                .correlationId(correlationId)
                .build();

        return ResponseEntity
                .status(problem.getStatus())
                .contentType(MediaType.parseMediaType("application/problem+json"))
                .header(TraceIdProvider.TRACE_ID_HEADER, traceId)
                .header(TraceIdProvider.CORRELATION_ID_HEADER, correlationId)
                .body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex,
            WebRequest request) {

        String traceId = traceIdProvider.getOrCreateTraceId();
        String correlationId = traceIdProvider.getOrCreateCorrelationId();

        ProblemDetail problem = problemMapper.toGenericProblemDetail(ex, traceId, correlationId);

        return ResponseEntity
                .status(500)
                .contentType(MediaType.parseMediaType("application/problem+json"))
                .header(TraceIdProvider.TRACE_ID_HEADER, traceId)
                .header(TraceIdProvider.CORRELATION_ID_HEADER, correlationId)
                .body(problem);
    }
}
