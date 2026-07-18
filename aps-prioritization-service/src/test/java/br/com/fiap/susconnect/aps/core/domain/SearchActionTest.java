/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SearchActionTest {

  private final Clock clock = Clock.fixed(Instant.parse("2026-07-18T10:00:00Z"), ZoneOffset.UTC);

  @Test
  void shouldCreateAndUpdateActionProgress() {
    SearchAction action =
        SearchAction.create(
            UUID.randomUUID(),
            PreventiveFocus.CHRONIC_CONDITIONS,
            "Reconnect people to follow-up",
            "ESF Central",
            LocalDate.of(2026, 7, 18),
            LocalDate.of(2026, 7, 25),
            80,
            "Aggregate records only",
            clock);

    action.updateProgress(ActionStatus.IN_PROGRESS, 40, "40 contacts made", clock);

    assertThat(action.getStatus()).isEqualTo(ActionStatus.IN_PROGRESS);
    assertThat(action.progressPercent()).isEqualByComparingTo("50.00");
    assertThat(action.isDueSoon(LocalDate.of(2026, 7, 18))).isTrue();
    assertThat(action.isOverdue(LocalDate.of(2026, 7, 26))).isTrue();
  }

  @Test
  void shouldRequirePerformedCountBeforeCompletionAndBlockTerminalUpdates() {
    SearchAction action =
        SearchAction.create(
            UUID.randomUUID(),
            PreventiveFocus.PRENATAL_CARE,
            "Reconnect prenatal follow-up",
            "ESF Central",
            LocalDate.of(2026, 7, 18),
            LocalDate.of(2026, 7, 20),
            10,
            null,
            clock);

    assertThatThrownBy(() -> action.updateProgress(ActionStatus.COMPLETED, 0, null, clock))
        .isInstanceOf(ApsValidationException.class);

    action.updateProgress(ActionStatus.COMPLETED, 10, "All planned contacts completed", clock);

    assertThat(action.getStatus().isTerminal()).isTrue();
    assertThatThrownBy(() -> action.updateProgress(ActionStatus.CANCELLED, 10, null, clock))
        .isInstanceOf(ApsValidationException.class)
        .hasMessageContaining("cannot be updated");
  }

  @Test
  void shouldRejectInvalidActionPeriodAndNegativeProgress() {
    assertThatThrownBy(
            () ->
                SearchAction.create(
                    UUID.randomUUID(),
                    PreventiveFocus.CERVICAL_SCREENING,
                    "Action",
                    "ESF Central",
                    LocalDate.of(2026, 7, 20),
                    LocalDate.of(2026, 7, 18),
                    10,
                    null,
                    clock))
        .isInstanceOf(ApsValidationException.class);
  }

  @Test
  void shouldValidateRequiredActionDataAndProgressValues() {
    LocalDate start = LocalDate.of(2026, 7, 18);
    LocalDate end = LocalDate.of(2026, 7, 20);
    assertThatThrownBy(
            () ->
                SearchAction.create(
                    null,
                    PreventiveFocus.CHRONIC_CONDITIONS,
                    "Action",
                    "Team",
                    start,
                    end,
                    10,
                    " ",
                    clock))
        .isInstanceOf(ApsValidationException.class)
        .hasMessageContaining("territory id");
    assertThatThrownBy(
            () ->
                SearchAction.create(
                    UUID.randomUUID(),
                    null,
                    "Action",
                    "Team",
                    start,
                    end,
                    10,
                    null,
                    clock))
        .isInstanceOf(ApsValidationException.class)
        .hasMessageContaining("focus");
    assertThatThrownBy(
            () ->
                SearchAction.create(
                    UUID.randomUUID(),
                    PreventiveFocus.CHRONIC_CONDITIONS,
                    " ",
                    "Team",
                    start,
                    end,
                    0,
                    null,
                    clock))
        .isInstanceOf(ApsValidationException.class);

    SearchAction action =
        SearchAction.create(
            UUID.randomUUID(),
            PreventiveFocus.CHRONIC_CONDITIONS,
            "Action",
            "Team",
            start,
            end,
            10,
            null,
            clock);
    assertThatThrownBy(() -> action.updateProgress(null, 1, null, clock))
        .isInstanceOf(ApsValidationException.class);
    assertThatThrownBy(() -> action.updateProgress(ActionStatus.IN_PROGRESS, -1, null, clock))
        .isInstanceOf(ApsValidationException.class);
  }
}
