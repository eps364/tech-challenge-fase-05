/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.usecase;

import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/** Use case for listing all medical records for a patient. */
@Slf4j
public class ListMedicalRecordsByPatientUseCase {

  private final MedicalRecordGateway gateway;

  public ListMedicalRecordsByPatientUseCase(MedicalRecordGateway gateway) {
    this.gateway = gateway;
  }

  public List<MedicalRecordOutput> execute(UUID patientId) {
    log.info("Listing medical records for patientId={}", patientId);
    return gateway.findByPatientId(patientId).stream().map(this::toOutput).toList();
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
