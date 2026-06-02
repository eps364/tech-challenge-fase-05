package br.com.fiap.registry.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when registry validation fails.
 */
@ProblemType(
    type = "https://api.example.com/problems/registry/validation-error",
    title = "Validation Error",
    status = HttpStatus.BAD_REQUEST,
    description = "Registry data validation failed"
)
public class RegistryValidationException extends DomainException {

    public RegistryValidationException(String message) {
        super(message);
    }

    public RegistryValidationException(String message, String instance) {
        super(message, instance);
    }

    public RegistryValidationException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
