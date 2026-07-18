/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import br.com.fiap.susconnect.aps.core.domain.PriorityLevel;
import java.math.BigDecimal;
import java.util.List;

public record PriorityOutput(
    PriorityLevel level, BigDecimal linkageTarget, List<String> reasons) {}
