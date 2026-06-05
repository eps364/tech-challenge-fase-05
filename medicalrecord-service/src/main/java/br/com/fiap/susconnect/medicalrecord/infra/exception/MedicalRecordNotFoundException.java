/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.exception;

import br.com.fiap.common.exception.DomainException;
import br.com.fiap.common.exception.ProblemType;
import org.springframework.http.HttpStatus;

/** Exception thrown when a medical record is not found. */
@ProblemType(
    type = "https://api.example.com/problems/medical-records/not-found",
    title = "Medical Record Not Found",
    status = HttpStatus.NOT_FOUND,
    description = "Requested medical record does not exist")
public class MedicalRecordNotFoundException extends DomainException {

  public MedicalRecordNotFoundException(String message) {
    super(message);
  }

  public MedicalRecordNotFoundException(String message, String instance) {
    super(message, instance);
  }

  public MedicalRecordNotFoundException(String message, String instance, Object extensionData) {
    super(message, instance, extensionData);
  }
}
