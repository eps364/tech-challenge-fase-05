/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.exception;

import br.com.fiap.common.exception.ProblemType;
import br.com.fiap.susconnect.triage.core.domain.TriageNotFoundDomainException;
import org.springframework.http.HttpStatus;

/** Exception thrown when a triage record is not found. */
@ProblemType(
    type = "https://api.example.com/problems/triage/not-found",
    title = "Triage Record Not Found",
    status = HttpStatus.NOT_FOUND,
    description = "Requested triage record does not exist")
public class TriageNotFoundException extends TriageNotFoundDomainException {

  public TriageNotFoundException(String message) {
    super(message);
  }
}
