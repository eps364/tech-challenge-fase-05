package br.com.fiap.susconnect.triage.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an invalid risk level is provided.
 */
@ProblemType(
    type = "https://api.example.com/problems/triage/invalid-risk-level",
    title = "Invalid Risk Level",
    status = HttpStatus.UNPROCESSABLE_ENTITY,
    description = "Risk level value is not recognized (low, medium, high)"
)
public class InvalidRiskLevelException extends DomainException {

    public InvalidRiskLevelException(String message) {
        super(message);
    }

    public InvalidRiskLevelException(String message, String instance) {
        super(message, instance);
    }

    public InvalidRiskLevelException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
