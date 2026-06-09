/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.gateway;

import br.com.fiap.susconnect.triage.core.dto.TriageClassifiedEvent;

/** Output port for publishing triage domain events. */
public interface TriageEventPublisher {

  void publishClassified(TriageClassifiedEvent event);
}
