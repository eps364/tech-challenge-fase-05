package br.com.fiap.susconnect.triage.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when triage data validation fails.
 */
@ProblemType(
    type = "https://api.example.com/problems/triage/validation-error",
    title = "Validation Error",
    status = HttpStatus.BAD_REQUEST,
    description = "Triage data validation failed"
)
public class TriageValidationException extends DomainException {

    public TriageValidationException(String message) {
        super(message);
    }

    public TriageValidationException(String message, String instance) {
        super(message, instance);
    }

    public TriageValidationException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
