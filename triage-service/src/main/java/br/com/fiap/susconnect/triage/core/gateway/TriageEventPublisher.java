/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.gateway;

import br.com.fiap.common.events.TriageClassifiedEvent;

/** Output port for publishing triage domain events. */
public interface TriageEventPublisher {

  void publishClassified(TriageClassifiedEvent event);
}
