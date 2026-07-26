/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.domain.TerritoryNotFoundException;
import br.com.fiap.susconnect.aps.core.dto.ReplaceTerritoryIndicatorsCommand;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.util.UUID;

public class ReplaceTerritoryIndicatorsUseCase {

  private final TerritoryGateway territoryGateway;

  public ReplaceTerritoryIndicatorsUseCase(TerritoryGateway territoryGateway) {
    this.territoryGateway = territoryGateway;
  }

  public Territory execute(UUID territoryId, ReplaceTerritoryIndicatorsCommand command) {
    Territory territory =
        territoryGateway
            .findById(territoryId)
            .orElseThrow(
                () -> new TerritoryNotFoundException("No territory found with id " + territoryId));
    Territory updated =
        territory.withIndicators(
            command.linkedPopulationPercent(), command.dataCompetence(), command.indicators());
    return territoryGateway.save(updated);
  }
}
