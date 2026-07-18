/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.domain.TerritoryAlreadyExistsException;
import br.com.fiap.susconnect.aps.core.dto.CreateTerritoryCommand;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;

public class CreateTerritoryUseCase {

  private final TerritoryGateway territoryGateway;

  public CreateTerritoryUseCase(TerritoryGateway territoryGateway) {
    this.territoryGateway = territoryGateway;
  }

  public Territory execute(CreateTerritoryCommand command) {
    if (territoryGateway.existsByCode(command.code())) {
      throw new TerritoryAlreadyExistsException("A territory already exists with code " + command.code());
    }
    Territory territory =
        Territory.create(
            command.code(),
            command.name(),
            command.unitName(),
            command.linkedPopulationPercent(),
            command.dataCompetence(),
            command.indicators());
    return territoryGateway.save(territory);
  }
}
