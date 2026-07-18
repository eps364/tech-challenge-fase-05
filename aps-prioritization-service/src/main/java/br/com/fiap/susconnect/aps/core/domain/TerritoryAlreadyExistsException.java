/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import br.com.fiap.common.exception.DomainException;
import br.com.fiap.common.exception.ProblemType;
import org.springframework.http.HttpStatus;

@ProblemType(
    type = "https://api.example.com/problems/aps-prioritization/territory-conflict",
    title = "Territory already exists",
    status = HttpStatus.CONFLICT)
public class TerritoryAlreadyExistsException extends DomainException {

  public TerritoryAlreadyExistsException(String message) {
    super(message);
  }
}
