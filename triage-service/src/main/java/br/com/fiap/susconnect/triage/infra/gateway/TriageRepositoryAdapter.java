/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.gateway;

import br.com.fiap.susconnect.triage.core.domain.entity.Triage;
import br.com.fiap.susconnect.triage.core.gateway.TriageGateway;
import br.com.fiap.susconnect.triage.infra.entity.TriageJpa;
import br.com.fiap.susconnect.triage.infra.repository.TriageRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Triage Repository Adapter - Implements core gateway with JPA */
@Component
public class TriageRepositoryAdapter implements TriageGateway {

  private final TriageRepository repository;

  public TriageRepositoryAdapter(TriageRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public void save(Triage triage) {
    var jpa =
        TriageJpa.builder()
            .id(triage.getId())
            .patientId(triage.getPatientId())
            .riskLevel(triage.getRiskLevel())
            .createdAt(triage.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();
    repository.save(jpa);
  }

  @Override
  public Optional<Triage> findById(UUID id) {
    return repository
        .findById(id)
        .map(
            jpa -> {
              var triage = Triage.create(jpa.getPatientId());
              triage.setRiskLevel(jpa.getRiskLevel());
              return triage;
            });
  }
}
