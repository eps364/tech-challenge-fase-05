/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import br.com.fiap.susconnect.aps.core.domain.TerritoryNotFoundException;
import br.com.fiap.susconnect.aps.core.dto.CreateSearchActionCommand;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.time.Clock;
import java.util.UUID;

public class CreateSearchActionUseCase {

  private final TerritoryGateway territoryGateway;
  private final SearchActionGateway searchActionGateway;
  private final Clock clock;

  public CreateSearchActionUseCase(
      TerritoryGateway territoryGateway, SearchActionGateway searchActionGateway, Clock clock) {
    this.territoryGateway = territoryGateway;
    this.searchActionGateway = searchActionGateway;
    this.clock = clock;
  }

  public SearchAction execute(UUID territoryId, CreateSearchActionCommand command) {
    if (territoryGateway.findById(territoryId).isEmpty()) {
      throw new TerritoryNotFoundException("No territory found with id " + territoryId);
    }
    SearchAction action =
        SearchAction.create(
            territoryId,
            command.focus(),
            command.objective(),
            command.responsibleTeam(),
            command.plannedStart(),
            command.plannedEnd(),
            command.targetCount(),
            command.notes(),
            clock);
    return searchActionGateway.save(action);
  }
}
