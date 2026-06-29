/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleAppointmentRequest(
    @NotNull(message = "The patient ID cannot be null") UUID patientId,
    UUID professionalId,
    @NotNull(message = "The date and time cannot be null") LocalDateTime dateTime,
    @NotNull(message = "The appointment type cannot be null") AppointmentType appointmentType,
    @NotBlank(message = "The service name cannot be blank") String serviceName,
    @NotBlank(message = "The facility name cannot be blank") String facilityName,
    String preparationNotes) {}
