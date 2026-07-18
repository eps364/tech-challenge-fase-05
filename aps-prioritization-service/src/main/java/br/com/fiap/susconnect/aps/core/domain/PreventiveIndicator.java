/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import java.math.BigDecimal;

public record PreventiveIndicator(PreventiveFocus focus, BigDecimal score, BigDecimal target) {

  public PreventiveIndicator {
    if (focus == null) {
      throw new ApsValidationException("The preventive focus is required");
    }
    score = validatePercentage(score, "score");
    target = validatePercentage(target, "target");
  }

  public boolean isBelowTarget() {
    return score.compareTo(target) < 0;
  }

  public BigDecimal gapToTarget() {
    return score.subtract(target);
  }

  private static BigDecimal validatePercentage(BigDecimal value, String field) {
    if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.valueOf(100)) > 0) {
      throw new ApsValidationException(field + " must be between 0 and 100");
    }
    return value;
  }
}
