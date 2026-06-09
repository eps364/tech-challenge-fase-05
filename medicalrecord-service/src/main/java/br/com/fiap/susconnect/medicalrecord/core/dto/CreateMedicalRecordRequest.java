/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/** Request body for creating a new medical record. */
public record CreateMedicalRecordRequest(
    @NotNull(message = "The appointment ID cannot be null") UUID appointmentId,
    @NotNull(message = "The patient ID cannot be null") UUID patientId,
    String diagnosis,
    String prescription,
    LocalDateTime consultationDate) {}
