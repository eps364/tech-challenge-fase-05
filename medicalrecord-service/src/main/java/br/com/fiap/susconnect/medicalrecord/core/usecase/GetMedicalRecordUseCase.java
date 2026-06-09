/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.usecase;

import br.com.fiap.susconnect.medicalrecord.core.domain.MedicalRecordNotFoundDomainException;
import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/** Use case for retrieving a medical record by ID. */
@Slf4j
public class GetMedicalRecordUseCase {

  private final MedicalRecordGateway gateway;

  public GetMedicalRecordUseCase(MedicalRecordGateway gateway) {
    this.gateway = gateway;
  }

  public MedicalRecordOutput execute(UUID id) {
    log.info("Fetching medical record: {}", id);
    return gateway
        .findById(id)
        .map(this::toOutput)
        .orElseThrow(
            () -> new MedicalRecordNotFoundDomainException("Medical record not found: " + id));
  }

  private MedicalRecordOutput toOutput(MedicalRecord r) {
    return new MedicalRecordOutput(
        r.getId(),
        r.getAppointmentId(),
        r.getPatientId(),
        r.getDiagnosis(),
        r.getPrescription(),
        r.getConsultationDate(),
        r.getCreatedAt());
  }
}
