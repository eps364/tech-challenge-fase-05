/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentOutput(
    UUID id,
    UUID patientId,
    UUID professionalId,
    LocalDateTime dateTime,
    String status,
    String appointmentType,
    String serviceName,
    String facilityName,
    String preparationNotes,
    String patientNotification,
    LocalDateTime lastNotifiedAt,
    LocalDateTime rescheduledFrom,
    String cancellationReason,
    LocalDateTime createdAt) {}
