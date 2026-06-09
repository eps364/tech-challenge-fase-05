/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.usecase;

import br.com.fiap.susconnect.medicalrecord.core.domain.MedicalRecordNotFoundDomainException;
import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/** Use case for updating an existing medical record. */
@Slf4j
public class UpdateMedicalRecordUseCase {

  private final MedicalRecordGateway gateway;

  public UpdateMedicalRecordUseCase(MedicalRecordGateway gateway) {
    this.gateway = gateway;
  }

  public MedicalRecordOutput execute(
      UUID id, String diagnosis, String prescription, LocalDateTime consultationDate) {
    log.info("Updating medical record: {}", id);
    var record =
        gateway
            .findById(id)
            .orElseThrow(
                () -> new MedicalRecordNotFoundDomainException("Medical record not found: " + id));
    if (diagnosis != null) record.setDiagnosis(diagnosis);
    if (prescription != null) record.setPrescription(prescription);
    if (consultationDate != null) record.setConsultationDate(consultationDate);
    gateway.update(record);
    log.info("Medical record updated: {}", id);
    return toOutput(record);
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
