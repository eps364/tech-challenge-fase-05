/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import br.com.fiap.susconnect.aps.core.domain.PriorityCalculator;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.domain.TerritoryNotFoundException;
import br.com.fiap.susconnect.aps.core.dto.ApsOutputMapper;
import br.com.fiap.susconnect.aps.core.dto.TerritoryDetailsOutput;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.util.UUID;

public class GetTerritoryDetailsUseCase {

  private final TerritoryGateway territoryGateway;
  private final SearchActionGateway searchActionGateway;
  private final PriorityCalculator priorityCalculator;

  public GetTerritoryDetailsUseCase(
      TerritoryGateway territoryGateway,
      SearchActionGateway searchActionGateway,
      PriorityCalculator priorityCalculator) {
    this.territoryGateway = territoryGateway;
    this.searchActionGateway = searchActionGateway;
    this.priorityCalculator = priorityCalculator;
  }

  public TerritoryDetailsOutput execute(UUID territoryId) {
    Territory territory =
        territoryGateway
            .findById(territoryId)
            .orElseThrow(
                () -> new TerritoryNotFoundException("No territory found with id " + territoryId));
    return ApsOutputMapper.toDetails(
        territory,
        priorityCalculator.assess(territory),
        searchActionGateway.findByTerritoryId(territoryId));
  }
}
