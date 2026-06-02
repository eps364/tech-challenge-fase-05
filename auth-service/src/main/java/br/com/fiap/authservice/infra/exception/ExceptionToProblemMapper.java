package br.com.fiap.authservice.infra.exception;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * Converts exceptions to RFC 9457 ProblemDetail objects.
 *
 * Maps @ProblemType annotated exceptions to corresponding Problem Details,
 * automatically extracting metadata from annotations and exception data.
 *
 * Used by GlobalExceptionHandler to generate consistent error responses.
 *
 * @see ProblemType
 * @see DomainException
 * @see GlobalExceptionHandler
 */
@Component
public class ExceptionToProblemMapper {

    /**
     * Converts a DomainException to a ProblemDetail.
     *
     * If the exception class has @ProblemType annotation, uses metadata from it.
     * Otherwise, creates a generic 500 problem detail.
     *
     * @param exception    The exception to convert
     * @param traceId      Unique request identifier
     * @param correlationId Business transaction identifier
     * @return Converted ProblemDetail
     */
    public ProblemDetail toProblemDetail(
            DomainException exception,
            String traceId,
            String correlationId) {

        ProblemType problemTypeAnnotation = exception.getClass().getAnnotation(ProblemType.class);

        if (problemTypeAnnotation != null) {
            return buildFromAnnotation(exception, problemTypeAnnotation, traceId, correlationId);
        }

        return buildGenericProblem(exception, traceId, correlationId);
    }

    /**
     * Converts any exception to a generic ProblemDetail.
     *
     * @param exception    The exception to convert
     * @param traceId      Unique request identifier
     * @param correlationId Business transaction identifier
     * @return Generic 500 ProblemDetail
     */
    public ProblemDetail toGenericProblemDetail(
            Exception exception,
            String traceId,
            String correlationId) {

        return new ProblemDetailBuilder()
                .type("about:blank")
                .status(500)
                .title("Internal Server Error")
                .detail("An unexpected error occurred")
                .timestamp(Instant.now())
                .traceId(traceId)
                .correlationId(correlationId)
                .build();
    }

    /**
     * Builds ProblemDetail from @ProblemType annotation metadata.
     */
    private ProblemDetail buildFromAnnotation(
            DomainException exception,
            ProblemType annotation,
            String traceId,
            String correlationId) {

        ProblemDetailBuilder builder = new ProblemDetailBuilder()
                .type(annotation.type())
                .status(annotation.status().value())
                .title(annotation.title())
                .detail(exception.getMessage())
                .instance(exception.getInstance())
                .timestamp(Instant.now())
                .traceId(traceId)
                .correlationId(correlationId);

        // Add extension data if present
        if (exception.getExtensionData() != null) {
            addExtensionData(builder, exception.getExtensionData());
        }

        return builder.build();
    }

    /**
     * Builds a generic ProblemDetail for DomainException without @ProblemType.
     */
    private ProblemDetail buildGenericProblem(
            DomainException exception,
            String traceId,
            String correlationId) {

        return new ProblemDetailBuilder()
                .type("about:blank")
                .status(500)
                .title("Internal Server Error")
                .detail(exception.getMessage())
                .instance(exception.getInstance())
                .timestamp(Instant.now())
                .traceId(traceId)
                .correlationId(correlationId)
                .build();
    }

    /**
     * Adds extension data to the problem detail builder.
     *
     * Supports common extension data types:
     * - Map<String, Object>: Added directly as extensions
     * - Other objects: Added as "extensionData" field
     */
    private void addExtensionData(ProblemDetailBuilder builder, Object extensionData) {
        if (extensionData instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) extensionData;
            builder.extensions(map);
        } else {
            builder.extension("extensionData", extensionData);
        }
    }

    /**
     * Generates a new trace ID.
     *
     * @return UUID v4 string
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
