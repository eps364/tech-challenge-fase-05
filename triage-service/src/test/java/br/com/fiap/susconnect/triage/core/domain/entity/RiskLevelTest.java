/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RiskLevelTest {

  @Test
  void redIsMoreUrgentThanOrange() {
    assertThat(RiskLevel.RED.isMoreUrgentThan(RiskLevel.ORANGE)).isTrue();
  }

  @Test
  void redIsMoreUrgentThanBlue() {
    assertThat(RiskLevel.RED.isMoreUrgentThan(RiskLevel.BLUE)).isTrue();
  }

  @Test
  void blueIsNotMoreUrgentThanRed() {
    assertThat(RiskLevel.BLUE.isMoreUrgentThan(RiskLevel.RED)).isFalse();
  }

  @Test
  void blueIsNotMoreUrgentThanGreen() {
    assertThat(RiskLevel.BLUE.isMoreUrgentThan(RiskLevel.GREEN)).isFalse();
  }

  @Test
  void orangeIsMoreUrgentThanYellow() {
    assertThat(RiskLevel.ORANGE.isMoreUrgentThan(RiskLevel.YELLOW)).isTrue();
  }

  @Test
  void yellowIsMoreUrgentThanGreen() {
    assertThat(RiskLevel.YELLOW.isMoreUrgentThan(RiskLevel.GREEN)).isTrue();
  }

  @Test
  void greenIsMoreUrgentThanBlue() {
    assertThat(RiskLevel.GREEN.isMoreUrgentThan(RiskLevel.BLUE)).isTrue();
  }

  @Test
  void sameLevel_notMoreUrgent() {
    assertThat(RiskLevel.RED.isMoreUrgentThan(RiskLevel.RED)).isFalse();
    assertThat(RiskLevel.BLUE.isMoreUrgentThan(RiskLevel.BLUE)).isFalse();
  }
}
