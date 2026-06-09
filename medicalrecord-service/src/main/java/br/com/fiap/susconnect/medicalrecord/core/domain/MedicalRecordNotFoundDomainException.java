/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.domain;

import br.com.fiap.common.exception.DomainException;

/** Domain-level exception thrown when a medical record cannot be found. */
public class MedicalRecordNotFoundDomainException extends DomainException {

  public MedicalRecordNotFoundDomainException(String message) {
    super(message);
  }
}
