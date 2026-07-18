/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

public enum ActionStatus {
  PLANNED,
  IN_PROGRESS,
  COMPLETED,
  CANCELLED;

  public boolean isTerminal() {
    return this == COMPLETED || this == CANCELLED;
  }
}
