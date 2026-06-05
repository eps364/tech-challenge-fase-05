/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.usecase;

import br.com.fiap.susconnect.triage.core.domain.entity.Triage;
import br.com.fiap.susconnect.triage.core.gateway.TriageGateway;
import java.util.UUID;

/** Create Triage Use Case - Pure business logic orchestration */
public class CreateTriageUseCase {

  private final TriageGateway triageGateway;

  public CreateTriageUseCase(TriageGateway triageGateway) {
    this.triageGateway = triageGateway;
  }

  public Triage execute(UUID patientId) {
    var triage = Triage.create(patientId);
    triageGateway.save(triage);
    return triage;
  }
}
