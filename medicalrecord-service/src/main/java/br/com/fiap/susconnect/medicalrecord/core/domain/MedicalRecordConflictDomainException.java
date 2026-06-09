/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.domain;

import br.com.fiap.common.exception.DomainException;

/** Domain-level exception thrown when a medical record conflicts with existing data. */
public class MedicalRecordConflictDomainException extends DomainException {

  public MedicalRecordConflictDomainException(String message) {
    super(message);
  }
}
