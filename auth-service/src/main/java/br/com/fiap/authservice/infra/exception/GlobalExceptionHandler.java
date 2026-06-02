package br.com.fiap.authservice.infra.exception;

import java.time.Instant;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for RFC 9457 Problem Details.
 *
 * Catches all exceptions in the application and converts them to
 * standardized RFC 9457 Problem Detail responses.
 *
 * Features:
 * - Automatically maps @ProblemType annotated exceptions
 * - Generates and propagates trace/correlation IDs
 * - Adds timestamp and operational extensions
 * - Returns application/problem+json media type
 *
 * @see ProblemDetail
 * @see DomainException
 * @see ProblemType
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionToProblemMapper problemMapper;
    private final TraceIdProvider traceIdProvider;

    public GlobalExceptionHandler(ExceptionToProblemMapper problemMapper, TraceIdProvider traceIdProvider) {
        this.problemMapper = problemMapper;
        this.traceIdProvider = traceIdProvider;
    }

    /**
     * Handles DomainException with @ProblemType annotation.
     *
     * @param ex Domain exception with problem type metadata
     * @param request Web request context
     * @return RFC 9457 Problem Detail response
     */
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

    /**
     * Handles generic RuntimeException.
     *
     * Falls back to 400 Bad Request or 500 Internal Server Error
     * depending on exception type.
     *
     * @param ex Exception
     * @param request Web request context
     * @return RFC 9457 Problem Detail response
     */
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

    /**
     * Handles any other exception as 500 Internal Server Error.
     *
     * @param ex Exception
     * @param request Web request context
     * @return RFC 9457 Problem Detail response
     */
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

