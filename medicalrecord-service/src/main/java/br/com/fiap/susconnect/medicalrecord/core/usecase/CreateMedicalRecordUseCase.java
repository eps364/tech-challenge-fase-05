/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.usecase;

import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/** Use case for creating a new medical record. */
@Slf4j
public class CreateMedicalRecordUseCase {

  private final MedicalRecordGateway gateway;

  public CreateMedicalRecordUseCase(MedicalRecordGateway gateway) {
    this.gateway = gateway;
  }

  public MedicalRecordOutput execute(
      UUID appointmentId,
      UUID patientId,
      String diagnosis,
      String prescription,
      LocalDateTime consultationDate) {
    log.info(
        "Creating medical record for patientId={}, appointmentId={}", patientId, appointmentId);
    var record = MedicalRecord.create(appointmentId, patientId);
    record.setDiagnosis(diagnosis);
    record.setPrescription(prescription);
    record.setConsultationDate(consultationDate);
    gateway.save(record);
    log.info("Medical record created: {}", record.getId());
    return toOutput(record);
  }

  MedicalRecordOutput toOutput(MedicalRecord r) {
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
