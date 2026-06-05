/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.exception;

import br.com.fiap.common.exception.DomainException;
import br.com.fiap.common.exception.ProblemType;
import org.springframework.http.HttpStatus;

/** Exception thrown when a duplicate triage entry is detected. */
@ProblemType(
    type = "https://api.example.com/problems/triage/duplicate-entry",
    title = "Duplicate Triage Entry",
    status = HttpStatus.CONFLICT,
    description = "Patient already has an active triage record")
public class TriageDuplicateEntryException extends DomainException {

  public TriageDuplicateEntryException(String message) {
    super(message);
  }

  public TriageDuplicateEntryException(String message, String instance) {
    super(message, instance);
  }

  public TriageDuplicateEntryException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
