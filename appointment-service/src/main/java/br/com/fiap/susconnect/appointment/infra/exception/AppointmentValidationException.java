package br.com.fiap.susconnect.appointment.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when appointment validation fails.
 */
@ProblemType(
    type = "https://api.example.com/problems/appointments/validation-error",
    title = "Validation Error",
    status = HttpStatus.BAD_REQUEST,
    description = "Appointment data validation failed"
)
public class AppointmentValidationException extends DomainException {

    public AppointmentValidationException(String message) {
        super(message);
    }

    public AppointmentValidationException(String message, String instance) {
        super(message, instance);
    }

    public AppointmentValidationException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
