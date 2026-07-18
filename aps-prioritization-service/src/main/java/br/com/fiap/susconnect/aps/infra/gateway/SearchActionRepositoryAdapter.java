/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.gateway;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.infra.entity.SearchActionJpa;
import br.com.fiap.susconnect.aps.infra.repository.SearchActionJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SearchActionRepositoryAdapter implements SearchActionGateway {

  private static final List<ActionStatus> OPEN_STATUSES =
      List.of(ActionStatus.PLANNED, ActionStatus.IN_PROGRESS);

  private final SearchActionJpaRepository repository;

  public SearchActionRepositoryAdapter(SearchActionJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public SearchAction save(SearchAction action) {
    repository.save(toJpa(action));
    return action;
  }

  @Override
  public Optional<SearchAction> findById(UUID id) {
    return repository.findById(id).map(this::toDomain);
  }

  @Override
  public List<SearchAction> findAll() {
    return repository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public List<SearchAction> findByTerritoryId(UUID territoryId) {
    return repository.findByTerritoryIdOrderByUpdatedAtDesc(territoryId).stream()
        .map(this::toDomain)
        .toList();
  }

  @Override
  public long countOpenByTerritoryId(UUID territoryId) {
    return repository.countByTerritoryIdAndStatusIn(territoryId, OPEN_STATUSES);
  }

  private SearchActionJpa toJpa(SearchAction action) {
    return new SearchActionJpa(
        action.getId(),
        action.getTerritoryId(),
        action.getFocus(),
        action.getObjective(),
        action.getResponsibleTeam(),
        action.getPlannedStart(),
        action.getPlannedEnd(),
        action.getTargetCount(),
        action.getPerformedCount(),
        action.getStatus(),
        action.getNotes(),
        action.getResultNotes(),
        action.getCreatedAt(),
        action.getUpdatedAt());
  }

  private SearchAction toDomain(SearchActionJpa entity) {
    return SearchAction.reconstruct(
        entity.getId(),
        entity.getTerritoryId(),
        entity.getFocus(),
        entity.getObjective(),
        entity.getResponsibleTeam(),
        entity.getPlannedStart(),
        entity.getPlannedEnd(),
        entity.getTargetCount(),
        entity.getPerformedCount(),
        entity.getStatus(),
        entity.getNotes(),
        entity.getResultNotes(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }
}
