/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.dto.PatientOutput;
import br.com.fiap.susconnect.appointment.core.dto.PatientOutputMapper;
import br.com.fiap.susconnect.appointment.core.gateway.PatientGateway;
import java.util.List;

public class ListPatientsUseCase {

  private final PatientGateway patientGateway;

  public ListPatientsUseCase(PatientGateway patientGateway) {
    this.patientGateway = patientGateway;
  }

  public List<PatientOutput> execute() {
    return patientGateway.findAll().stream().map(PatientOutputMapper::toOutput).toList();
  }
}
