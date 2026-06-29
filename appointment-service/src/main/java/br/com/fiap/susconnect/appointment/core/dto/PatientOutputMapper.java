/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import br.com.fiap.susconnect.appointment.core.domain.entity.Patient;

public final class PatientOutputMapper {

  private PatientOutputMapper() {}

  public static PatientOutput toOutput(Patient patient) {
    return new PatientOutput(
        patient.getId(), patient.getFullName(), patient.getEmail(), patient.getPhone());
  }
}
