/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.fiap.susconnect.aps.core.CoreTestData;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class PriorityCalculatorTest {

  private final PriorityCalculator calculator = new PriorityCalculator(new BigDecimal("50.00"));

  @Test
  void shouldClassifyHighPriorityWhenBothSignalsAreBelowTarget() {
    Territory territory =
        CoreTestData.territory(
            "HIGH", List.of(CoreTestData.indicator(PreventiveFocus.CHRONIC_CONDITIONS, "35", "60")));

    PriorityAssessment assessment = calculator.assess(territory);

    assertThat(assessment.level()).isEqualTo(PriorityLevel.HIGH);
    assertThat(assessment.reasons()).hasSize(2);
    assertThat(assessment.mostCriticalIndicator()).contains(territory.getIndicators().getFirst());
  }

  @Test
  void shouldClassifyMediumPriorityWhenOnlyOneSignalIsBelowTarget() {
    Territory lowLinkage =
        CoreTestData.territory(
            "LINKAGE", List.of(CoreTestData.indicator(PreventiveFocus.PRENATAL_CARE, "90", "85")));
    Territory lowIndicator =
        CoreTestData.territory(
            "INDICATOR", "80", List.of(CoreTestData.indicator(PreventiveFocus.PRENATAL_CARE, "70", "85")));

    assertThat(calculator.assess(lowLinkage).level()).isEqualTo(PriorityLevel.MEDIUM);
    assertThat(calculator.assess(lowIndicator).level()).isEqualTo(PriorityLevel.MEDIUM);
  }

  @Test
  void shouldClassifyLowPriorityAndExplainWhenTargetsAreMet() {
    Territory territory =
        CoreTestData.territory(
            "LOW", "80", List.of(CoreTestData.indicator(PreventiveFocus.CERVICAL_SCREENING, "80", "70")));

    PriorityAssessment assessment = calculator.assess(territory);

    assertThat(assessment.level()).isEqualTo(PriorityLevel.LOW);
    assertThat(assessment.reasons()).containsExactly("Linked population and preventive indicators meet the configured targets");
    assertThat(assessment.mostCriticalIndicator()).isEmpty();
  }

  @Test
  void shouldRejectInvalidLinkageTarget() {
    assertThatThrownBy(() -> new PriorityCalculator(new BigDecimal("101")))
        .isInstanceOf(ApsValidationException.class)
        .hasMessageContaining("linkage target");
  }

  @Test
  void shouldRejectIncompleteOrOutOfRangePreventiveIndicator() {
    assertThatThrownBy(() -> new PreventiveIndicator(null, new BigDecimal("40"), new BigDecimal("60")))
        .isInstanceOf(ApsValidationException.class)
        .hasMessageContaining("focus");
    assertThatThrownBy(
            () ->
                new PreventiveIndicator(
                    PreventiveFocus.CHRONIC_CONDITIONS, new BigDecimal("-1"), new BigDecimal("60")))
        .isInstanceOf(ApsValidationException.class)
        .hasMessageContaining("score");
  }
}
