/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.config;

import br.com.fiap.susconnect.aps.core.domain.PriorityCalculator;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import br.com.fiap.susconnect.aps.core.usecase.CreateSearchActionUseCase;
import br.com.fiap.susconnect.aps.core.usecase.CreateTerritoryUseCase;
import br.com.fiap.susconnect.aps.core.usecase.GetDashboardUseCase;
import br.com.fiap.susconnect.aps.core.usecase.GetTerritoryDetailsUseCase;
import br.com.fiap.susconnect.aps.core.usecase.ListTerritoriesUseCase;
import br.com.fiap.susconnect.aps.core.usecase.ReplaceTerritoryIndicatorsUseCase;
import br.com.fiap.susconnect.aps.core.usecase.UpdateSearchActionProgressUseCase;
import java.time.Clock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApsPriorityProperties.class)
public class UseCaseConfig {

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean
  public PriorityCalculator priorityCalculator(ApsPriorityProperties properties) {
    return new PriorityCalculator(properties.getLinkageTarget());
  }

  @Bean
  public CreateTerritoryUseCase createTerritoryUseCase(TerritoryGateway territoryGateway) {
    return new CreateTerritoryUseCase(territoryGateway);
  }

  @Bean
  public ReplaceTerritoryIndicatorsUseCase replaceTerritoryIndicatorsUseCase(
      TerritoryGateway territoryGateway) {
    return new ReplaceTerritoryIndicatorsUseCase(territoryGateway);
  }

  @Bean
  public ListTerritoriesUseCase listTerritoriesUseCase(
      TerritoryGateway territoryGateway,
      SearchActionGateway searchActionGateway,
      PriorityCalculator priorityCalculator) {
    return new ListTerritoriesUseCase(territoryGateway, searchActionGateway, priorityCalculator);
  }

  @Bean
  public GetTerritoryDetailsUseCase getTerritoryDetailsUseCase(
      TerritoryGateway territoryGateway,
      SearchActionGateway searchActionGateway,
      PriorityCalculator priorityCalculator) {
    return new GetTerritoryDetailsUseCase(territoryGateway, searchActionGateway, priorityCalculator);
  }

  @Bean
  public CreateSearchActionUseCase createSearchActionUseCase(
      TerritoryGateway territoryGateway, SearchActionGateway searchActionGateway, Clock clock) {
    return new CreateSearchActionUseCase(territoryGateway, searchActionGateway, clock);
  }

  @Bean
  public UpdateSearchActionProgressUseCase updateSearchActionProgressUseCase(
      SearchActionGateway searchActionGateway, Clock clock) {
    return new UpdateSearchActionProgressUseCase(searchActionGateway, clock);
  }

  @Bean
  public GetDashboardUseCase getDashboardUseCase(
      TerritoryGateway territoryGateway,
      SearchActionGateway searchActionGateway,
      PriorityCalculator priorityCalculator,
      Clock clock) {
    return new GetDashboardUseCase(territoryGateway, searchActionGateway, priorityCalculator, clock);
  }
}
