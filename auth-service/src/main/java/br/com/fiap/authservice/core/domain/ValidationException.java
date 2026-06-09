/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.domain;

import br.com.fiap.common.exception.DomainException;

/** Exception thrown when domain validation fails. HTTP mapping is handled by the infra layer. */
public class ValidationException extends DomainException {

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, String instance) {
    super(message, instance);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValidationException(String message, String instance, Throwable cause) {
    super(message, instance, cause);
  }

  public ValidationException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
