/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Triage Domain Entity - Rich domain model with business logic Represents a clinical triage
 * assessment following Manchester Protocol v3.0
 */
public class Triage {

  private UUID id;
  private UUID patientId;
  private String riskLevel; // RED, ORANGE, YELLOW, GREEN, BLUE
  private LocalDateTime createdAt;

  private Triage() {}

  public static Triage create(UUID patientId) {
    var triage = new Triage();
    triage.id = UUID.randomUUID();
    triage.patientId = patientId;
    triage.riskLevel = "BLUE"; // default
    triage.createdAt = LocalDateTime.now();
    return triage;
  }

  // Getters
  public UUID getId() {
    return id;
  }

  public UUID getPatientId() {
    return patientId;
  }

  public String getRiskLevel() {
    return riskLevel;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setRiskLevel(String riskLevel) {
    this.riskLevel = riskLevel;
  }
}
