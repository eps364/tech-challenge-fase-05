/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.usecase;

import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import br.com.fiap.susconnect.aps.core.domain.SearchActionNotFoundException;
import br.com.fiap.susconnect.aps.core.dto.UpdateSearchActionProgressCommand;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import java.time.Clock;
import java.util.UUID;

public class UpdateSearchActionProgressUseCase {

  private final SearchActionGateway searchActionGateway;
  private final Clock clock;

  public UpdateSearchActionProgressUseCase(SearchActionGateway searchActionGateway, Clock clock) {
    this.searchActionGateway = searchActionGateway;
    this.clock = clock;
  }

  public SearchAction execute(UUID actionId, UpdateSearchActionProgressCommand command) {
    SearchAction action =
        searchActionGateway
            .findById(actionId)
            .orElseThrow(
                () -> new SearchActionNotFoundException("No search action found with id " + actionId));
    action.updateProgress(command.status(), command.performedCount(), command.resultNotes(), clock);
    return searchActionGateway.save(action);
  }
}
