/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.registry.infra.exception;

import org.springframework.http.HttpStatus;

/** Exception thrown when registry entries conflict. */
@ProblemType(
    type = "https://api.example.com/problems/registry/conflict",
    title = "Conflicting Entry",
    status = HttpStatus.CONFLICT,
    description = "Entry conflicts with existing registry data")
public class RegistryConflictException extends DomainException {

  public RegistryConflictException(String message) {
    super(message);
  }

  public RegistryConflictException(String message, String instance) {
    super(message, instance);
  }

  public RegistryConflictException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
