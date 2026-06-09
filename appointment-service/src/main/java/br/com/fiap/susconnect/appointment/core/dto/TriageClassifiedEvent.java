/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TriageClassifiedEvent(
    UUID triageId, UUID patientId, String riskLevel, LocalDateTime classifiedAt) {}
