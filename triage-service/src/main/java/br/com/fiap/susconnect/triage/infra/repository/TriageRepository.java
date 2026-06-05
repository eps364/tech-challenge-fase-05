/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.repository;

import br.com.fiap.susconnect.triage.infra.entity.TriageJpa;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring Data JPA Repository for Triage */
@Repository
public interface TriageRepository extends JpaRepository<TriageJpa, UUID> {

  Optional<TriageJpa> findByPatientId(UUID patientId);
}
