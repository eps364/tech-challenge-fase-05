/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.gateway.infra.exception;

import org.springframework.http.HttpStatus;

/** Exception thrown when request is invalid. */
@ProblemType(
    type = "https://api.example.com/problems/gateway/invalid-request",
    title = "Invalid Request",
    status = HttpStatus.BAD_REQUEST,
    description = "Request is malformed or violates API contract")
public class InvalidRequestException extends DomainException {

  public InvalidRequestException(String message) {
    super(message);
  }

  public InvalidRequestException(String message, String instance) {
    super(message, instance);
  }

  public InvalidRequestException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
