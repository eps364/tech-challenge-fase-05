/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentOfferOutput(
    UUID id,
    UUID openedAppointmentId,
    UUID candidateAppointmentId,
    UUID candidatePatientId,
    LocalDateTime offeredDateTime,
    LocalDateTime originalDateTime,
    String status,
    String message,
    LocalDateTime createdAt,
    LocalDateTime respondedAt) {}
