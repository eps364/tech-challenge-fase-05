/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.gateway.infra.exception;

import org.springframework.http.HttpStatus;

/** Exception thrown when a downstream service is unavailable. */
@ProblemType(
    type = "https://api.example.com/problems/gateway/service-unavailable",
    title = "Service Unavailable",
    status = HttpStatus.SERVICE_UNAVAILABLE,
    description = "Downstream microservice is unreachable or not responding")
public class ServiceUnavailableException extends DomainException {

  public ServiceUnavailableException(String message) {
    super(message);
  }

  public ServiceUnavailableException(String message, String instance) {
    super(message, instance);
  }

  public ServiceUnavailableException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
