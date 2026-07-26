/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record CreateTerritoryRequest(
    @NotBlank @Size(max = 50) String code,
    @NotBlank @Size(max = 120) String name,
    @NotBlank @Size(max = 120) String unitName,
    @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal linkedPopulationPercent,
    @NotBlank @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$") String dataCompetence,
    @NotEmpty List<@Valid IndicatorRequest> indicators) {}
