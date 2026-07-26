/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PriorityCalculator {

  private final BigDecimal linkageTarget;

  public PriorityCalculator(BigDecimal linkageTarget) {
    if (linkageTarget == null
        || linkageTarget.compareTo(BigDecimal.ZERO) < 0
        || linkageTarget.compareTo(BigDecimal.valueOf(100)) > 0) {
      throw new ApsValidationException("The linkage target must be between 0 and 100");
    }
    this.linkageTarget = linkageTarget;
  }

  public PriorityAssessment assess(Territory territory) {
    boolean lowLinkage = territory.getLinkedPopulationPercent().compareTo(linkageTarget) < 0;
    List<PreventiveIndicator> belowTargetIndicators =
        territory.getIndicators().stream().filter(PreventiveIndicator::isBelowTarget).toList();
    List<String> reasons = new ArrayList<>();

    if (lowLinkage) {
      reasons.add(
          "Linked population "
              + territory.getLinkedPopulationPercent().toPlainString()
              + "% is below the configured target of "
              + linkageTarget.toPlainString()
              + "%");
    }
    belowTargetIndicators.forEach(
        indicator ->
            reasons.add(
                indicator.focus().label()
                    + " is "
                    + indicator.score().toPlainString()
                    + "% against target "
                    + indicator.target().toPlainString()
                    + "%"));

    PriorityLevel level;
    if (lowLinkage && !belowTargetIndicators.isEmpty()) {
      level = PriorityLevel.HIGH;
    } else if (lowLinkage || !belowTargetIndicators.isEmpty()) {
      level = PriorityLevel.MEDIUM;
    } else {
      level = PriorityLevel.LOW;
      reasons.add("Linked population and preventive indicators meet the configured targets");
    }

    return new PriorityAssessment(level, linkageTarget, reasons, belowTargetIndicators);
  }
}
