/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.gateway;

import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SearchActionGateway {

  SearchAction save(SearchAction action);

  Optional<SearchAction> findById(UUID id);

  List<SearchAction> findAll();

  List<SearchAction> findByTerritoryId(UUID territoryId);

  long countOpenByTerritoryId(UUID territoryId);
}
