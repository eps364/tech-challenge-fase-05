/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.exception;

import br.com.fiap.common.exception.DomainException;
import br.com.fiap.common.exception.ProblemType;
import org.springframework.http.HttpStatus;

/** Exception thrown when a triage record is not found. */
@ProblemType(
    type = "https://api.example.com/problems/triage/not-found",
    title = "Triage Record Not Found",
    status = HttpStatus.NOT_FOUND,
    description = "Requested triage record does not exist")
public class TriageNotFoundException extends DomainException {

  public TriageNotFoundException(String message) {
    super(message);
  }

  public TriageNotFoundException(String message, String instance) {
    super(message, instance);
  }

  public TriageNotFoundException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
