/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleAppointmentRequest(
    @NotNull(message = "The triage ID cannot be null") UUID triageId,
    @NotNull(message = "The patient ID cannot be null") UUID patientId,
    @NotNull(message = "The date and time cannot be null") LocalDateTime dateTime) {}
