/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.entity;

import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "territory_indicators")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TerritoryIndicatorJpa {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "territory_id", nullable = false)
  private TerritoryJpa territory;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 40)
  private PreventiveFocus focus;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal score;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal target;

  public TerritoryIndicatorJpa(UUID id, PreventiveFocus focus, BigDecimal score, BigDecimal target) {
    this.id = id;
    this.focus = focus;
    this.score = score;
    this.target = target;
  }

  public void assignTerritory(TerritoryJpa territory) {
    this.territory = territory;
  }
}
