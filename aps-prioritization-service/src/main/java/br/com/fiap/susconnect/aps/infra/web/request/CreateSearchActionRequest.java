/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.web.request;

import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateSearchActionRequest(
    @NotNull PreventiveFocus focus,
    @NotBlank @Size(max = 500) String objective,
    @NotBlank @Size(max = 160) String responsibleTeam,
    @NotNull LocalDate plannedStart,
    @NotNull LocalDate plannedEnd,
    @Positive int targetCount,
    @Size(max = 1000) String notes) {}
