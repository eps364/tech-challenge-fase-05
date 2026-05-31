package br.com.fiap.susconnect.triage.core.gateway;

import br.com.fiap.susconnect.triage.core.domain.entity.Triage;
import java.util.Optional;
import java.util.UUID;

/**
 * Triage Gateway Port - Interface defining persistence contract
 * Must be implemented in infra layer (adapter)
 */
public interface TriageGateway {

  void save(Triage triage);

  Optional<Triage> findById(UUID id);
}
