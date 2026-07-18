/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import java.math.BigDecimal;

public record IndicatorOutput(
    PreventiveFocus focus, String label, BigDecimal score, BigDecimal target, boolean belowTarget) {}
