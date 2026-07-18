/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public record PriorityAssessment(
    PriorityLevel level,
    BigDecimal linkageTarget,
    List<String> reasons,
    List<PreventiveIndicator> belowTargetIndicators) {

  public PriorityAssessment {
    reasons = List.copyOf(reasons);
    belowTargetIndicators = List.copyOf(belowTargetIndicators);
  }

  public Optional<PreventiveIndicator> mostCriticalIndicator() {
    return belowTargetIndicators.stream().min((left, right) -> left.gapToTarget().compareTo(right.gapToTarget()));
  }
}
