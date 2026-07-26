/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.gateway;

import br.com.fiap.susconnect.aps.core.domain.PreventiveIndicator;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import br.com.fiap.susconnect.aps.infra.entity.TerritoryIndicatorJpa;
import br.com.fiap.susconnect.aps.infra.entity.TerritoryJpa;
import br.com.fiap.susconnect.aps.infra.repository.TerritoryJpaRepository;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TerritoryRepositoryAdapter implements TerritoryGateway {

  private final TerritoryJpaRepository repository;

  public TerritoryRepositoryAdapter(TerritoryJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Territory save(Territory territory) {
    Optional<TerritoryJpa> existing = repository.findById(territory.getId());
    if (existing.isPresent()) {
      TerritoryJpa entity = existing.get();
      entity.clearIndicators();
      repository.flush();
      entity.updatePrioritizationData(
          territory.getLinkedPopulationPercent(), territory.getDataCompetence().toString());
      toIndicators(territory).forEach(entity::addIndicator);
    } else {
      repository.save(toJpa(territory));
    }
    return territory;
  }

  @Override
  public Optional<Territory> findById(UUID id) {
    return repository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Territory> findAll() {
    return repository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public boolean existsByCode(String code) {
    return repository.existsByCode(code);
  }

  private TerritoryJpa toJpa(Territory territory) {
    TerritoryJpa entity =
        new TerritoryJpa(
            territory.getId(),
            territory.getCode(),
            territory.getName(),
            territory.getUnitName(),
            territory.getLinkedPopulationPercent(),
            territory.getDataCompetence().toString(),
            LocalDateTime.now());
    toIndicators(territory).forEach(entity::addIndicator);
    return entity;
  }

  private List<TerritoryIndicatorJpa> toIndicators(Territory territory) {
    return territory.getIndicators().stream()
        .map(
            indicator ->
                new TerritoryIndicatorJpa(
                    UUID.randomUUID(), indicator.focus(), indicator.score(), indicator.target()))
        .toList();
  }

  private Territory toDomain(TerritoryJpa entity) {
    List<PreventiveIndicator> indicators =
        entity.getIndicators().stream()
            .map(
                indicator ->
                    new PreventiveIndicator(
                        indicator.getFocus(), indicator.getScore(), indicator.getTarget()))
            .toList();
    return Territory.reconstruct(
        entity.getId(),
        entity.getCode(),
        entity.getName(),
        entity.getUnitName(),
        entity.getLinkedPopulationPercent(),
        YearMonth.parse(entity.getDataCompetence()),
        indicators);
  }
}
