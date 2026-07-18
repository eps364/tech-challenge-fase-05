/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.aps.core.CoreTestData;
import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import br.com.fiap.susconnect.aps.core.domain.SearchActionNotFoundException;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.domain.TerritoryNotFoundException;
import br.com.fiap.susconnect.aps.core.dto.CreateSearchActionCommand;
import br.com.fiap.susconnect.aps.core.dto.UpdateSearchActionProgressCommand;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchActionUseCasesTest {

  @Mock private TerritoryGateway territoryGateway;
  @Mock private SearchActionGateway searchActionGateway;

  private final Clock clock = Clock.fixed(Instant.parse("2026-07-18T10:00:00Z"), ZoneOffset.UTC);

  @Test
  void shouldCreateAndUpdateAction() {
    Territory territory =
        CoreTestData.territory(
            "T-20", List.of(CoreTestData.indicator(PreventiveFocus.CHRONIC_CONDITIONS, "40", "60")));
    when(territoryGateway.findById(territory.getId())).thenReturn(Optional.of(territory));
    when(searchActionGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    CreateSearchActionUseCase createUseCase =
        new CreateSearchActionUseCase(territoryGateway, searchActionGateway, clock);

    SearchAction created =
        createUseCase.execute(
            territory.getId(),
            new CreateSearchActionCommand(
                PreventiveFocus.CHRONIC_CONDITIONS,
                "Reconnect chronic care",
                "ESF Test",
                LocalDate.of(2026, 7, 18),
                LocalDate.of(2026, 7, 25),
                30,
                null));
    when(searchActionGateway.findById(created.getId())).thenReturn(Optional.of(created));
    UpdateSearchActionProgressUseCase updateUseCase =
        new UpdateSearchActionProgressUseCase(searchActionGateway, clock);

    SearchAction updated =
        updateUseCase.execute(
            created.getId(),
            new UpdateSearchActionProgressCommand(ActionStatus.IN_PROGRESS, 12, "12 contacts"));

    assertThat(updated.getPerformedCount()).isEqualTo(12);
  }

  @Test
  void shouldReportMissingTerritoryAndAction() {
    CreateSearchActionUseCase createUseCase =
        new CreateSearchActionUseCase(territoryGateway, searchActionGateway, clock);
    UUID missingTerritoryId = UUID.randomUUID();
    when(territoryGateway.findById(missingTerritoryId)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                createUseCase.execute(
                    missingTerritoryId,
                    new CreateSearchActionCommand(
                        PreventiveFocus.PRENATAL_CARE,
                        "Action",
                        "Team",
                        LocalDate.of(2026, 7, 18),
                        LocalDate.of(2026, 7, 19),
                        1,
                        null)))
        .isInstanceOf(TerritoryNotFoundException.class);

    UpdateSearchActionProgressUseCase updateUseCase =
        new UpdateSearchActionProgressUseCase(searchActionGateway, clock);
    UUID missingActionId = UUID.randomUUID();
    when(searchActionGateway.findById(missingActionId)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                updateUseCase.execute(
                    missingActionId,
                    new UpdateSearchActionProgressCommand(ActionStatus.IN_PROGRESS, 1, null)))
        .isInstanceOf(SearchActionNotFoundException.class);
  }
}
