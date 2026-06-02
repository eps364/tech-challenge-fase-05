package br.com.fiap.registry.infra.exception;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * Converts exceptions to RFC 9457 ProblemDetail objects.
 */
@Component
public class ExceptionToProblemMapper {

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

        if (exception.getExtensionData() != null) {
            addExtensionData(builder, exception.getExtensionData());
        }

        return builder.build();
    }

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

    private void addExtensionData(ProblemDetailBuilder builder, Object extensionData) {
        if (extensionData instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) extensionData;
            builder.extensions(map);
        } else {
            builder.extension("extensionData", extensionData);
        }
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
