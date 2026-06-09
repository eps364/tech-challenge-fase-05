/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.dto;

import java.time.LocalDateTime;

/** Request body for updating an existing medical record. */
public record UpdateMedicalRecordRequest(
    String diagnosis, String prescription, LocalDateTime consultationDate) {}
