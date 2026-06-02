package br.com.fiap.susconnect.medicalrecord.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when medical records conflict.
 */
@ProblemType(
    type = "https://api.example.com/problems/medical-records/conflict",
    title = "Conflicting Record",
    status = HttpStatus.CONFLICT,
    description = "Record conflicts with existing medical data"
)
public class MedicalRecordConflictException extends DomainException {

    public MedicalRecordConflictException(String message) {
        super(message);
    }

    public MedicalRecordConflictException(String message, String instance) {
        super(message, instance);
    }

    public MedicalRecordConflictException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
