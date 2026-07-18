/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.repository;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.infra.entity.SearchActionJpa;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchActionJpaRepository extends JpaRepository<SearchActionJpa, UUID> {

  List<SearchActionJpa> findByTerritoryIdOrderByUpdatedAtDesc(UUID territoryId);

  long countByTerritoryIdAndStatusIn(UUID territoryId, Collection<ActionStatus> statuses);
}
