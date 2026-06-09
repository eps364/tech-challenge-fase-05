/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.domain;

import br.com.fiap.common.exception.DomainException;

/** Domain-level exception thrown when medical record data fails validation. */
public class MedicalRecordValidationDomainException extends DomainException {

  public MedicalRecordValidationDomainException(String message) {
    super(message);
  }
}
