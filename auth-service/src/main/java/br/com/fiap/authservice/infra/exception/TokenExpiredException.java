package br.com.fiap.authservice.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an authentication token has expired.
 *
 * Mapped to RFC 9457 problem type:
 * https://api.example.com/problems/auth/token-expired
 *
 * HTTP Status: 401 Unauthorized
 */
@ProblemType(
    type = "https://api.example.com/problems/auth/token-expired",
    title = "Token Expired",
    status = HttpStatus.UNAUTHORIZED,
    description = "JWT or session token has expired"
)
public class TokenExpiredException extends DomainException {

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, String instance) {
        super(message, instance);
    }

    public TokenExpiredException(String message, Object extensionData) {
        super(message, null, extensionData);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpiredException(String message, String instance, Throwable cause) {
        super(message, instance, cause);
    }
}
