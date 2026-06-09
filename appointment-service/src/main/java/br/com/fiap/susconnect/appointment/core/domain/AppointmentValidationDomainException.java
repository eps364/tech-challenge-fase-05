/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.domain;

import br.com.fiap.common.exception.DomainException;

public class AppointmentValidationDomainException extends DomainException {
  public AppointmentValidationDomainException(String message) {
    super(message);
  }
}
