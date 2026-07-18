/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import br.com.fiap.susconnect.aps.core.domain.PreventiveIndicator;
import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public final class CoreTestData {

  private CoreTestData() {}

  public static Territory territory(String code, List<PreventiveIndicator> indicators) {
    return territory(code, "40", indicators);
  }

  public static Territory territory(String code, String linkage, List<PreventiveIndicator> indicators) {
    return Territory.reconstruct(
        UUID.randomUUID(),
        code,
        "Territory " + code,
        "UBS " + code,
        new BigDecimal(linkage),
        YearMonth.of(2026, 6),
        indicators);
  }

  public static PreventiveIndicator indicator(PreventiveFocus focus, String score, String target) {
    return new PreventiveIndicator(focus, new BigDecimal(score), new BigDecimal(target));
  }

  public static SearchAction action(
      UUID territoryId,
      ActionStatus status,
      int performedCount,
      LocalDate plannedStart,
      LocalDate plannedEnd,
      LocalDateTime updatedAt) {
    return SearchAction.reconstruct(
        UUID.randomUUID(),
        territoryId,
        PreventiveFocus.CHRONIC_CONDITIONS,
        "Reconnect people to follow-up",
        "ESF Test",
        plannedStart,
        plannedEnd,
        50,
        performedCount,
        status,
        null,
        null,
        updatedAt.minusDays(1),
        updatedAt);
  }
}
