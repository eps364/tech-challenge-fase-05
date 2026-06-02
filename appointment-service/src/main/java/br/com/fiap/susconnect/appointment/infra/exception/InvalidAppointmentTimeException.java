package br.com.fiap.susconnect.appointment.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when appointment time is invalid.
 */
@ProblemType(
    type = "https://api.example.com/problems/appointments/invalid-time",
    title = "Invalid Appointment Time",
    status = HttpStatus.UNPROCESSABLE_ENTITY,
    description = "Proposed appointment time is in the past or invalid"
)
public class InvalidAppointmentTimeException extends DomainException {

    public InvalidAppointmentTimeException(String message) {
        super(message);
    }

    public InvalidAppointmentTimeException(String message, String instance) {
        super(message, instance);
    }

    public InvalidAppointmentTimeException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
