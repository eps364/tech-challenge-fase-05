/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.web.request;

import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record IndicatorRequest(
    @NotNull PreventiveFocus focus,
    @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal score,
    @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal target) {}
