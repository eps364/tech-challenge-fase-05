/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.domain.entity;

/**
 * Manchester Protocol risk levels (v3.0). Ordered by descending clinical urgency.
 *
 * <ul>
 *   <li>{@link #RED} – Immediate (0 min)
 *   <li>{@link #ORANGE} – Very urgent (10 min)
 *   <li>{@link #YELLOW} – Urgent (60 min)
 *   <li>{@link #GREEN} – Standard (120 min)
 *   <li>{@link #BLUE} – Non-urgent (240 min)
 * </ul>
 */
public enum RiskLevel {
  RED,
  ORANGE,
  YELLOW,
  GREEN,
  BLUE;

  public boolean isMoreUrgentThan(RiskLevel other) {
    return this.ordinal() < other.ordinal();
  }
}
