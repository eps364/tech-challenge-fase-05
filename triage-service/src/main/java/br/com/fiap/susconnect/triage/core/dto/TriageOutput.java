/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/** Triage Output DTO - Response payload */
public record TriageOutput(UUID id, String riskLevel, LocalDateTime createdAt) {}
