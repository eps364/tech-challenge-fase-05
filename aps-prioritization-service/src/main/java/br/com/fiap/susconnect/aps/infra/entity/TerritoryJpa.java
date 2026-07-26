/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "territories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TerritoryJpa {

  @Id private UUID id;

  @Column(nullable = false, unique = true, length = 50)
  private String code;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(name = "unit_name", nullable = false, length = 120)
  private String unitName;

  @Column(name = "linked_population_percent", nullable = false, precision = 5, scale = 2)
  private BigDecimal linkedPopulationPercent;

  @Column(name = "data_competence", nullable = false, length = 7)
  private String dataCompetence;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "territory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @OrderBy("focus ASC")
  private List<TerritoryIndicatorJpa> indicators = new ArrayList<>();

  public TerritoryJpa(
      UUID id,
      String code,
      String name,
      String unitName,
      BigDecimal linkedPopulationPercent,
      String dataCompetence,
      LocalDateTime createdAt) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.unitName = unitName;
    this.linkedPopulationPercent = linkedPopulationPercent;
    this.dataCompetence = dataCompetence;
    this.createdAt = createdAt;
  }

  public void addIndicator(TerritoryIndicatorJpa indicator) {
    indicator.assignTerritory(this);
    indicators.add(indicator);
  }

  public void updatePrioritizationData(
      BigDecimal newLinkedPopulationPercent, String newDataCompetence) {
    linkedPopulationPercent = newLinkedPopulationPercent;
    dataCompetence = newDataCompetence;
  }

  public void clearIndicators() {
    indicators.clear();
  }
}
