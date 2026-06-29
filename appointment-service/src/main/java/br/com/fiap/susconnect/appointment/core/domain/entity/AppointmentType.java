/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.domain.entity;

/** Type of appointment resource reserved in the health unit. */
public enum AppointmentType {
  CONSULTATION("consulta"),
  EXAM("exame");

  private final String label;

  AppointmentType(String label) {
    this.label = label;
  }

  public String label() {
    return label;
  }
}
