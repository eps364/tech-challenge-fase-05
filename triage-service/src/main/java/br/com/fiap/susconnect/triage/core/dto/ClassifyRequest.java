/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.dto;

import br.com.fiap.susconnect.triage.core.domain.entity.RiskLevel;
import jakarta.validation.constraints.NotNull;

/** Request payload for classifying a triage risk level. */
public record ClassifyRequest(
    @NotNull(message = "The risk level cannot be null") RiskLevel riskLevel) {}
