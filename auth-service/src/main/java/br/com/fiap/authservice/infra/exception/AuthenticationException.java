/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.infra.exception;

import br.com.fiap.common.exception.DomainException;
import br.com.fiap.common.exception.ProblemType;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails.
 *
 * <p>Mapped to RFC 9457 problem type: https://api.example.com/problems/auth/unauthorized
 *
 * <p>HTTP Status: 401 Unauthorized
 */
@ProblemType(
    type = "https://api.example.com/problems/auth/unauthorized",
    title = "Unauthorized",
    status = HttpStatus.UNAUTHORIZED,
    description = "Missing or invalid authentication credentials")
public class AuthenticationException extends DomainException {

  public AuthenticationException(String message) {
    super(message);
  }

  public AuthenticationException(String message, String instance) {
    super(message, instance);
  }

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AuthenticationException(String message, String instance, Throwable cause) {
    super(message, instance, cause);
  }

  public AuthenticationException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
