package br.com.fiap.susconnect.medicalrecord.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when access to a medical record is denied.
 */
@ProblemType(
    type = "https://api.example.com/problems/medical-records/access-denied",
    title = "Access Denied",
    status = HttpStatus.FORBIDDEN,
    description = "User lacks permission to access this medical record"
)
public class AccessDeniedException extends DomainException {

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, String instance) {
        super(message, instance);
    }

    public AccessDeniedException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
