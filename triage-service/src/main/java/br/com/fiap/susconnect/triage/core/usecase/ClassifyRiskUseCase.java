/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.usecase;

import br.com.fiap.susconnect.triage.core.domain.TriageNotFoundDomainException;
import br.com.fiap.susconnect.triage.core.domain.entity.RiskLevel;
import br.com.fiap.susconnect.triage.core.dto.TriageClassifiedEvent;
import br.com.fiap.susconnect.triage.core.dto.TriageOutput;
import br.com.fiap.susconnect.triage.core.gateway.TriageEventPublisher;
import br.com.fiap.susconnect.triage.core.gateway.TriageGateway;
import java.time.LocalDateTime;
import java.util.UUID;

/** Classifies a triage risk level and publishes the corresponding domain event. */
public class ClassifyRiskUseCase {

  private final TriageGateway triageGateway;
  private final TriageEventPublisher eventPublisher;

  public ClassifyRiskUseCase(TriageGateway triageGateway, TriageEventPublisher eventPublisher) {
    this.triageGateway = triageGateway;
    this.eventPublisher = eventPublisher;
  }

  public TriageOutput execute(UUID triageId, RiskLevel newRiskLevel) {
    var triage =
        triageGateway
            .findById(triageId)
            .orElseThrow(
                () -> new TriageNotFoundDomainException("Triage not found with id: " + triageId));

    triage.updateRiskLevel(newRiskLevel);
    triageGateway.update(triage);

    eventPublisher.publishClassified(
        new TriageClassifiedEvent(
            triage.getId(),
            triage.getPatientId(),
            triage.getRiskLevel().name(),
            LocalDateTime.now()));

    return new TriageOutput(
        triage.getId(),
        triage.getPatientId(),
        triage.getRiskLevel(),
        triage.getCreatedAt(),
        triage.getUpdatedAt());
  }
}
