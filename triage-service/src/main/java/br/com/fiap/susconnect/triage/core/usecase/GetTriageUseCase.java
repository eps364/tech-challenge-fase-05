/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.usecase;

import br.com.fiap.susconnect.triage.core.domain.TriageNotFoundDomainException;
import br.com.fiap.susconnect.triage.core.dto.TriageOutput;
import br.com.fiap.susconnect.triage.core.gateway.TriageGateway;
import java.util.UUID;

/** Retrieves a triage record by its identifier. */
public class GetTriageUseCase {

  private final TriageGateway triageGateway;

  public GetTriageUseCase(TriageGateway triageGateway) {
    this.triageGateway = triageGateway;
  }

  public TriageOutput execute(UUID triageId) {
    var triage =
        triageGateway
            .findById(triageId)
            .orElseThrow(
                () -> new TriageNotFoundDomainException("Triage not found with id: " + triageId));

    return new TriageOutput(
        triage.getId(),
        triage.getPatientId(),
        triage.getRiskLevel(),
        triage.getCreatedAt(),
        triage.getUpdatedAt());
  }
}
