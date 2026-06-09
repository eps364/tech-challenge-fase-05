/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.domain;

import br.com.fiap.common.exception.DomainException;

/** Domain-level exception thrown when a triage record cannot be found. */
public class TriageNotFoundDomainException extends DomainException {

  public TriageNotFoundDomainException(String message) {
    super(message);
  }
}
