package br.com.fiap.susconnect.appointment.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an appointment record is not found.
 */
@ProblemType(
    type = "https://api.example.com/problems/appointments/not-found",
    title = "Appointment Not Found",
    status = HttpStatus.NOT_FOUND,
    description = "Requested appointment does not exist"
)
public class AppointmentNotFoundException extends DomainException {

    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(String message, String instance) {
        super(message, instance);
    }

    public AppointmentNotFoundException(String message, String instance, Object extensionData) {
        super(message, instance, extensionData);
    }
}
