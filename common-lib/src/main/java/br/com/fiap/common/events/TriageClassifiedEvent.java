/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

/** Event published when a triage risk classification is performed. */
public record TriageClassifiedEvent(
    UUID triageId, UUID patientId, String riskLevel, LocalDateTime classifiedAt) {}
