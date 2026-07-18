/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

public enum PreventiveFocus {
  PRENATAL_CARE("Acompanhamento prenatal"),
  CHILDHOOD_VACCINATION("Vacinacao infantil"),
  CHRONIC_CONDITIONS("Condicoes cronicas"),
  CERVICAL_SCREENING("Rastreamento citopatologico");

  private final String label;

  PreventiveFocus(String label) {
    this.label = label;
  }

  public String label() {
    return label;
  }
}
