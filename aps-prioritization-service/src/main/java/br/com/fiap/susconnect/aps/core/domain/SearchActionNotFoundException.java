/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

import br.com.fiap.common.exception.DomainException;
import br.com.fiap.common.exception.ProblemType;
import org.springframework.http.HttpStatus;

@ProblemType(
    type = "https://api.example.com/problems/aps-prioritization/search-action-not-found",
    title = "Search action not found",
    status = HttpStatus.NOT_FOUND)
public class SearchActionNotFoundException extends DomainException {

  public SearchActionNotFoundException(String message) {
    super(message);
  }
}
