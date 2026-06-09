/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.registry.infra.exception;

import org.springframework.http.HttpStatus;

/** Exception thrown when a registry entry is not found. */
@ProblemType(
    type = "https://api.example.com/problems/registry/not-found",
    title = "Registry Entry Not Found",
    status = HttpStatus.NOT_FOUND,
    description = "Requested registry entry does not exist")
public class RegistryEntryNotFoundException extends DomainException {

  public RegistryEntryNotFoundException(String message) {
    super(message);
  }

  public RegistryEntryNotFoundException(String message, String instance) {
    super(message, instance);
  }

  public RegistryEntryNotFoundException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
