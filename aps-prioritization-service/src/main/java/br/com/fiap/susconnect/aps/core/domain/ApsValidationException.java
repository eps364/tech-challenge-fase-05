/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import br.com.fiap.common.exception.DomainException;
import br.com.fiap.common.exception.ProblemType;
import org.springframework.http.HttpStatus;

@ProblemType(
    type = "https://api.example.com/problems/aps-prioritization/validation-error",
    title = "Invalid APS prioritization data",
    status = HttpStatus.UNPROCESSABLE_ENTITY)
public class ApsValidationException extends DomainException {

  public ApsValidationException(String message) {
    super(message);
  }
}
