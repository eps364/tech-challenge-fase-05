package br.com.fiap.susconnect.appointment.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when appointment times conflict.
 */
@ProblemType(
    type = "https://api.example.com/problems/appointments/conflict",
    title = "Appointment Conflict",
    status = HttpStatus.CONFLICT,
    description = "Requested time slot is already booked or unavailable"
)
public class AppointmentConflictException extends DomainException {

    public AppointmentConflictException(String message) {
        super(message);
    }

    public AppointmentConflictException(String message, String instance) {
        super(message, instance);
    }

    public AppointmentConflictException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
