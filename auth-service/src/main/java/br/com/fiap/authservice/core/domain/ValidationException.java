package br.com.fiap.authservice.core.domain;

import org.springframework.http.HttpStatus;

import br.com.fiap.authservice.infra.exception.DomainException;
import br.com.fiap.authservice.infra.exception.ProblemType;

/**
 * Exception thrown when domain validation fails.
 *
 * Mapped to RFC 9457 problem type:
 * https://api.example.com/problems/auth/validation-error
 *
 * HTTP Status: 400 Bad Request
 */
@ProblemType(
    type = "https://api.example.com/problems/auth/validation-error",
    title = "Validation Error",
    status = HttpStatus.BAD_REQUEST,
    description = "Invalid login credentials, registration data, or password format"
)
public class ValidationException extends DomainException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String instance) {
        super(message, instance);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message, String instance, Throwable cause) {
        super(message, instance, cause);
    }

    public ValidationException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
