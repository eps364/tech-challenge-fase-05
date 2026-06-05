/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** Triage creation request payload. */
public record TriageRequest(@NotNull(message = "Patient ID is required") UUID patientId) {}
