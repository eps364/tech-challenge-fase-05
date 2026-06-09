/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.exception;

import br.com.fiap.common.exception.ProblemDetail;
import br.com.fiap.common.exception.ProblemDetailBuilder;
import br.com.fiap.susconnect.medicalrecord.core.domain.MedicalRecordConflictDomainException;
import br.com.fiap.susconnect.medicalrecord.core.domain.MedicalRecordNotFoundDomainException;
import br.com.fiap.susconnect.medicalrecord.core.domain.MedicalRecordValidationDomainException;
import java.time.Instant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Exception handler for medical record domain exceptions. */
@RestControllerAdvice
public class MedicalRecordExceptionHandler {

  private static final String PROBLEM_JSON = "application/problem+json";

  @ExceptionHandler(MedicalRecordNotFoundDomainException.class)
  public ResponseEntity<ProblemDetail> handleNotFound(MedicalRecordNotFoundDomainException ex) {
    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type("https://api.example.com/problems/medical-records/not-found")
            .status(404)
            .title("Medical Record Not Found")
            .detail(ex.getMessage())
            .timestamp(Instant.now())
            .build();
    return ResponseEntity.status(404)
        .contentType(MediaType.parseMediaType(PROBLEM_JSON))
        .body(problem);
  }

  @ExceptionHandler(MedicalRecordConflictDomainException.class)
  public ResponseEntity<ProblemDetail> handleConflict(MedicalRecordConflictDomainException ex) {
    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type("https://api.example.com/problems/medical-records/conflict")
            .status(409)
            .title("Conflicting Medical Record")
            .detail(ex.getMessage())
            .timestamp(Instant.now())
            .build();
    return ResponseEntity.status(409)
        .contentType(MediaType.parseMediaType(PROBLEM_JSON))
        .body(problem);
  }

  @ExceptionHandler(MedicalRecordValidationDomainException.class)
  public ResponseEntity<ProblemDetail> handleValidation(MedicalRecordValidationDomainException ex) {
    ProblemDetail problem =
        new ProblemDetailBuilder()
            .type("https://api.example.com/problems/medical-records/validation-error")
            .status(422)
            .title("Medical Record Validation Error")
            .detail(ex.getMessage())
            .timestamp(Instant.now())
            .build();
    return ResponseEntity.status(422)
        .contentType(MediaType.parseMediaType(PROBLEM_JSON))
        .body(problem);
  }
}
