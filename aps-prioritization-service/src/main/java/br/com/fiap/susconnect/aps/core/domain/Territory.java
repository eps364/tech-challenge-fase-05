/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class Territory {

  private final UUID id;
  private final String code;
  private final String name;
  private final String unitName;
  private final BigDecimal linkedPopulationPercent;
  private final YearMonth dataCompetence;
  private final List<PreventiveIndicator> indicators;

  private Territory(
      UUID id,
      String code,
      String name,
      String unitName,
      BigDecimal linkedPopulationPercent,
      YearMonth dataCompetence,
      List<PreventiveIndicator> indicators) {
    this.id = requireId(id);
    this.code = requireText(code, "code");
    this.name = requireText(name, "name");
    this.unitName = requireText(unitName, "unit name");
    this.linkedPopulationPercent = validatePercentage(linkedPopulationPercent, "linked population percent");
    if (dataCompetence == null) {
      throw new ApsValidationException("The data competence is required");
    }
    this.dataCompetence = dataCompetence;
    this.indicators = validateIndicators(indicators);
  }

  public static Territory create(
      String code,
      String name,
      String unitName,
      BigDecimal linkedPopulationPercent,
      YearMonth dataCompetence,
      List<PreventiveIndicator> indicators) {
    return new Territory(
        UUID.randomUUID(), code, name, unitName, linkedPopulationPercent, dataCompetence, indicators);
  }

  public static Territory reconstruct(
      UUID id,
      String code,
      String name,
      String unitName,
      BigDecimal linkedPopulationPercent,
      YearMonth dataCompetence,
      List<PreventiveIndicator> indicators) {
    return new Territory(id, code, name, unitName, linkedPopulationPercent, dataCompetence, indicators);
  }

  public Territory withIndicators(
      BigDecimal newLinkedPopulationPercent,
      YearMonth newDataCompetence,
      List<PreventiveIndicator> newIndicators) {
    return new Territory(
        id, code, name, unitName, newLinkedPopulationPercent, newDataCompetence, newIndicators);
  }

  public UUID getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getUnitName() {
    return unitName;
  }

  public BigDecimal getLinkedPopulationPercent() {
    return linkedPopulationPercent;
  }

  public YearMonth getDataCompetence() {
    return dataCompetence;
  }

  public List<PreventiveIndicator> getIndicators() {
    return indicators;
  }

  private static UUID requireId(UUID value) {
    if (value == null) {
      throw new ApsValidationException("The territory id is required");
    }
    return value;
  }

  private static String requireText(String value, String field) {
    if (value == null || value.isBlank()) {
      throw new ApsValidationException("The territory " + field + " is required");
    }
    return value.trim();
  }

  private static BigDecimal validatePercentage(BigDecimal value, String field) {
    if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.valueOf(100)) > 0) {
      throw new ApsValidationException(field + " must be between 0 and 100");
    }
    return value;
  }

  private static List<PreventiveIndicator> validateIndicators(List<PreventiveIndicator> values) {
    if (values == null || values.isEmpty()) {
      throw new ApsValidationException("At least one preventive indicator is required");
    }
    Set<PreventiveFocus> focuses = new HashSet<>();
    for (PreventiveIndicator indicator : values) {
      if (!focuses.add(indicator.focus())) {
        throw new ApsValidationException("Preventive indicators cannot repeat the same focus");
      }
    }
    return List.copyOf(values);
  }
}
