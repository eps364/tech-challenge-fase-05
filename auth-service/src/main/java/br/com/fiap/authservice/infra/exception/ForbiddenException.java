package br.com.fiap.authservice.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an authenticated user lacks required permissions.
 *
 * Mapped to RFC 9457 problem type:
 * https://api.example.com/problems/auth/forbidden
 *
 * HTTP Status: 403 Forbidden
 */
@ProblemType(
    type = "https://api.example.com/problems/auth/forbidden",
    title = "Forbidden",
    status = HttpStatus.FORBIDDEN,
    description = "Authenticated user lacks required permissions or roles"
)
public class ForbiddenException extends DomainException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, String instance) {
        super(message, instance);
    }

    public ForbiddenException(String message, Object extensionData) {
        super(message, null, extensionData);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(String message, String instance, Throwable cause) {
        super(message, instance, cause);
    }

    public ForbiddenException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
