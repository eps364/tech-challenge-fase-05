/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.aps.core.CoreTestData;
import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import br.com.fiap.susconnect.aps.core.domain.PriorityCalculator;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.dto.DashboardOutput;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetDashboardUseCaseTest {

  @Mock private TerritoryGateway territoryGateway;
  @Mock private SearchActionGateway searchActionGateway;

  @Test
  void shouldSummarizePrioritiesCompletedActionsAndAlerts() {
    Territory territory =
        CoreTestData.territory(
            "T-30", List.of(CoreTestData.indicator(PreventiveFocus.CHRONIC_CONDITIONS, "40", "60")));
    LocalDate today = LocalDate.of(2026, 7, 18);
    LocalDateTime now = LocalDateTime.of(2026, 7, 18, 10, 0);
    when(territoryGateway.findAll()).thenReturn(List.of(territory));
    when(searchActionGateway.countOpenByTerritoryId(territory.getId())).thenReturn(1L);
    when(searchActionGateway.findAll())
        .thenReturn(
            List.of(
                CoreTestData.action(
                    territory.getId(), ActionStatus.PLANNED, 0, today.minusDays(4), today.minusDays(1), now),
                CoreTestData.action(
                    territory.getId(), ActionStatus.COMPLETED, 50, today.minusDays(10), today.minusDays(2), now)));
    GetDashboardUseCase useCase =
        new GetDashboardUseCase(
            territoryGateway,
            searchActionGateway,
            new PriorityCalculator(new BigDecimal("50")),
            Clock.fixed(Instant.parse("2026-07-18T10:00:00Z"), ZoneOffset.UTC));

    DashboardOutput output = useCase.execute(today.minusDays(1), today);

    assertThat(output.highPriorityTerritoryCount()).isEqualTo(1);
    assertThat(output.openActionCount()).isEqualTo(1);
    assertThat(output.completedActionCount()).isEqualTo(1);
    assertThat(output.attentionActions()).hasSize(1);
    assertThat(output.attentionActions().getFirst().reason()).isEqualTo("OVERDUE");
    assertThatThrownBy(() -> useCase.execute(today, today.minusDays(1)))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
