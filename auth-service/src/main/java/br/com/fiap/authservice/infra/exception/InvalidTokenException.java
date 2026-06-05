/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.infra.exception;

import br.com.fiap.common.exception.DomainException;
import br.com.fiap.common.exception.ProblemType;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a JWT token is malformed, invalid, or corrupted.
 *
 * <p>Mapped to RFC 9457 problem type: https://api.example.com/problems/auth/invalid-token
 *
 * <p>HTTP Status: 401 Unauthorized
 */
@ProblemType(
    type = "https://api.example.com/problems/auth/invalid-token",
    title = "Invalid Token",
    status = HttpStatus.UNAUTHORIZED,
    description = "JWT token is malformed, invalid signature, or corrupted")
public class InvalidTokenException extends DomainException {

  public InvalidTokenException(String message) {
    super(message);
  }

  public InvalidTokenException(String message, String instance) {
    super(message, instance);
  }

  public InvalidTokenException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidTokenException(String message, String instance, Throwable cause) {
    super(message, instance, cause);
  }

  public InvalidTokenException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
