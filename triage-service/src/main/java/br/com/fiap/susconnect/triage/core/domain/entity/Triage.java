/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Triage domain entity — rich model following Manchester Protocol v3.0.
 *
 * <p>Default risk level on creation is {@link RiskLevel#BLUE} (non-urgent) until a clinical
 * assessment upgrades it.
 */
public class Triage {

  private UUID id;
  private UUID patientId;
  private RiskLevel riskLevel;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Triage() {}

  public static Triage create(UUID patientId) {
    var triage = new Triage();
    triage.id = UUID.randomUUID();
    triage.patientId = patientId;
    triage.riskLevel = RiskLevel.BLUE;
    triage.createdAt = LocalDateTime.now();
    return triage;
  }

  public static Triage reconstruct(
      UUID id,
      UUID patientId,
      RiskLevel riskLevel,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    var triage = new Triage();
    triage.id = id;
    triage.patientId = patientId;
    triage.riskLevel = riskLevel;
    triage.createdAt = createdAt;
    triage.updatedAt = updatedAt;
    return triage;
  }

  public void updateRiskLevel(RiskLevel newRiskLevel) {
    this.riskLevel = newRiskLevel;
    this.updatedAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public UUID getPatientId() {
    return patientId;
  }

  public RiskLevel getRiskLevel() {
    return riskLevel;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}
