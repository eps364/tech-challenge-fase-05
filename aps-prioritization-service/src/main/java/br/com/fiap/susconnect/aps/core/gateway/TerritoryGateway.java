/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.gateway;

import br.com.fiap.susconnect.aps.core.domain.Territory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TerritoryGateway {

  Territory save(Territory territory);

  Optional<Territory> findById(UUID id);

  List<Territory> findAll();

  boolean existsByCode(String code);
}
