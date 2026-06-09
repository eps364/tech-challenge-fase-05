/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.gateway.infra.exception;

import org.springframework.http.HttpStatus;

/** Exception thrown when rate limit is exceeded. */
@ProblemType(
    type = "https://api.example.com/problems/gateway/rate-limited",
    title = "Too Many Requests",
    status = HttpStatus.TOO_MANY_REQUESTS,
    description = "Client has exceeded rate limit for this endpoint")
public class RateLimitedException extends DomainException {

  public RateLimitedException(String message) {
    super(message);
  }

  public RateLimitedException(String message, String instance) {
    super(message, instance);
  }

  public RateLimitedException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
