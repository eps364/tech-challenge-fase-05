/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/** Output DTO for medical record use cases. */
public record MedicalRecordOutput(
    UUID id,
    UUID appointmentId,
    UUID patientId,
    String diagnosis,
    String prescription,
    LocalDateTime consultationDate,
    LocalDateTime createdAt) {}
