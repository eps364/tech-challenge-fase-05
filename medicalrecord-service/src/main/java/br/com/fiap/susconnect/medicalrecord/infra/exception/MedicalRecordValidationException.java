package br.com.fiap.susconnect.medicalrecord.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when medical record validation fails.
 */
@ProblemType(
    type = "https://api.example.com/problems/medical-records/validation-error",
    title = "Validation Error",
    status = HttpStatus.BAD_REQUEST,
    description = "Medical record data validation failed"
)
public class MedicalRecordValidationException extends DomainException {

    public MedicalRecordValidationException(String message) {
        super(message);
    }

    public MedicalRecordValidationException(String message, String instance) {
        super(message, instance);
    }

    public MedicalRecordValidationException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
