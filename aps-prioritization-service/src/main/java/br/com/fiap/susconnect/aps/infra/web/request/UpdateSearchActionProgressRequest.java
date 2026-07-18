/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.web.request;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateSearchActionProgressRequest(
    @NotNull ActionStatus status,
    @PositiveOrZero int performedCount,
    @Size(max = 1000) String resultNotes) {}
