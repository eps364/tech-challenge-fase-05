/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.domain;

public class TerritoryAlreadyExistsException extends RuntimeException {

  public TerritoryAlreadyExistsException(String message) {
    super(message);
  }
}
