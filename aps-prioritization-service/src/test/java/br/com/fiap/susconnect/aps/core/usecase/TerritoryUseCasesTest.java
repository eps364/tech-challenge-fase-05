/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.aps.core.CoreTestData;
import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import br.com.fiap.susconnect.aps.core.domain.PriorityCalculator;
import br.com.fiap.susconnect.aps.core.domain.PriorityLevel;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.domain.TerritoryAlreadyExistsException;
import br.com.fiap.susconnect.aps.core.domain.TerritoryNotFoundException;
import br.com.fiap.susconnect.aps.core.dto.CreateTerritoryCommand;
import br.com.fiap.susconnect.aps.core.dto.ReplaceTerritoryIndicatorsCommand;
import br.com.fiap.susconnect.aps.core.dto.TerritoryDetailsOutput;
import br.com.fiap.susconnect.aps.core.dto.TerritorySummaryOutput;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TerritoryUseCasesTest {

  @Mock private TerritoryGateway territoryGateway;
  @Mock private SearchActionGateway searchActionGateway;

  @Test
  void shouldCreateTerritoryAndRejectDuplicateCode() {
    CreateTerritoryUseCase useCase = new CreateTerritoryUseCase(territoryGateway);
    CreateTerritoryCommand command =
        new CreateTerritoryCommand(
            "T-10",
            "North",
            "UBS North",
            new BigDecimal("40"),
            YearMonth.of(2026, 6),
            List.of(CoreTestData.indicator(PreventiveFocus.CHRONIC_CONDITIONS, "40", "60")));
    when(territoryGateway.existsByCode("T-10")).thenReturn(false);
    when(territoryGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Territory created = useCase.execute(command);

    assertThat(created.getCode()).isEqualTo("T-10");
    when(territoryGateway.existsByCode("T-10")).thenReturn(true);
    assertThatThrownBy(() -> useCase.execute(command)).isInstanceOf(TerritoryAlreadyExistsException.class);
  }

  @Test
  void shouldReplaceIndicatorsAndReturnTerritoryDetails() {
    Territory territory =
        CoreTestData.territory(
            "T-11", List.of(CoreTestData.indicator(PreventiveFocus.PRENATAL_CARE, "40", "85")));
    ReplaceTerritoryIndicatorsUseCase replaceUseCase =
        new ReplaceTerritoryIndicatorsUseCase(territoryGateway);
    when(territoryGateway.findById(territory.getId())).thenReturn(Optional.of(territory));
    when(territoryGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Territory updated =
        replaceUseCase.execute(
            territory.getId(),
            new ReplaceTerritoryIndicatorsCommand(
                new BigDecimal("80"),
                YearMonth.of(2026, 7),
                List.of(CoreTestData.indicator(PreventiveFocus.PRENATAL_CARE, "90", "85"))));

    assertThat(updated.getLinkedPopulationPercent()).isEqualByComparingTo("80");

    GetTerritoryDetailsUseCase detailsUseCase =
        new GetTerritoryDetailsUseCase(
            territoryGateway, searchActionGateway, new PriorityCalculator(new BigDecimal("50")));
    when(territoryGateway.findById(territory.getId())).thenReturn(Optional.of(updated));
    when(searchActionGateway.findByTerritoryId(territory.getId())).thenReturn(List.of());

    TerritoryDetailsOutput details = detailsUseCase.execute(territory.getId());

    assertThat(details.priority().level()).isEqualTo(PriorityLevel.LOW);
    assertThatThrownBy(() -> detailsUseCase.execute(java.util.UUID.randomUUID()))
        .isInstanceOf(TerritoryNotFoundException.class);
  }

  @Test
  void shouldListTerritoriesUsingPriorityAndFocusFilters() {
    Territory high =
        CoreTestData.territory(
            "T-12", List.of(CoreTestData.indicator(PreventiveFocus.CHRONIC_CONDITIONS, "40", "60")));
    Territory medium =
        CoreTestData.territory(
            "T-13", "80", List.of(CoreTestData.indicator(PreventiveFocus.PRENATAL_CARE, "70", "85")));
    when(territoryGateway.findAll()).thenReturn(List.of(medium, high));
    when(searchActionGateway.countOpenByTerritoryId(any())).thenReturn(2L);

    ListTerritoriesUseCase useCase =
        new ListTerritoriesUseCase(
            territoryGateway, searchActionGateway, new PriorityCalculator(new BigDecimal("50")));

    List<TerritorySummaryOutput> highPriorities = useCase.execute(PriorityLevel.HIGH, null);
    List<TerritorySummaryOutput> prenatal = useCase.execute(null, PreventiveFocus.PRENATAL_CARE);

    assertThat(highPriorities).extracting(TerritorySummaryOutput::id).containsExactly(high.getId());
    assertThat(prenatal).extracting(TerritorySummaryOutput::id).containsExactly(medium.getId());
  }

  @Test
  void shouldValidateTerritoryRequiredFieldsAndIndicators() {
    List<br.com.fiap.susconnect.aps.core.domain.PreventiveIndicator> indicators =
        List.of(CoreTestData.indicator(PreventiveFocus.PRENATAL_CARE, "80", "85"));
    assertThatThrownBy(
            () ->
                Territory.reconstruct(
                    null,
                    "T-40",
                    "Territory",
                    "UBS",
                    new BigDecimal("50"),
                    YearMonth.of(2026, 6),
                    indicators))
        .isInstanceOf(br.com.fiap.susconnect.aps.core.domain.ApsValidationException.class);
    assertThatThrownBy(
            () ->
                Territory.create(
                    " ",
                    "Territory",
                    "UBS",
                    new BigDecimal("50"),
                    YearMonth.of(2026, 6),
                    indicators))
        .isInstanceOf(br.com.fiap.susconnect.aps.core.domain.ApsValidationException.class);
    assertThatThrownBy(
            () ->
                Territory.create(
                    "T-40",
                    "Territory",
                    "UBS",
                    new BigDecimal("101"),
                    YearMonth.of(2026, 6),
                    indicators))
        .isInstanceOf(br.com.fiap.susconnect.aps.core.domain.ApsValidationException.class);
    assertThatThrownBy(
            () ->
                Territory.create("T-40", "Territory", "UBS", new BigDecimal("50"), null, indicators))
        .isInstanceOf(br.com.fiap.susconnect.aps.core.domain.ApsValidationException.class);
    assertThatThrownBy(
            () ->
                Territory.create(
                    "T-40",
                    "Territory",
                    "UBS",
                    new BigDecimal("50"),
                    YearMonth.of(2026, 6),
                    List.of()))
        .isInstanceOf(br.com.fiap.susconnect.aps.core.domain.ApsValidationException.class);
    assertThatThrownBy(
            () ->
                Territory.create(
                    "T-40",
                    "Territory",
                    "UBS",
                    new BigDecimal("50"),
                    YearMonth.of(2026, 6),
                    List.of(indicators.getFirst(), indicators.getFirst())))
        .isInstanceOf(br.com.fiap.susconnect.aps.core.domain.ApsValidationException.class);
  }
}
