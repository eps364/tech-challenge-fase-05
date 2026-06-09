/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.gateway;

import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import br.com.fiap.susconnect.medicalrecord.infra.entity.MedicalRecordJpa;
import br.com.fiap.susconnect.medicalrecord.infra.repository.MedicalRecordRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Adapter implementing MedicalRecordGateway using Spring Data JPA. */
@Component
public class MedicalRecordRepositoryAdapter implements MedicalRecordGateway {

  private final MedicalRecordRepository repository;

  public MedicalRecordRepositoryAdapter(MedicalRecordRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public void save(MedicalRecord record) {
    repository.save(toJpa(record));
  }

  @Override
  public Optional<MedicalRecord> findById(UUID id) {
    return repository.findById(id).map(this::toDomain);
  }

  @Override
  public List<MedicalRecord> findByPatientId(UUID patientId) {
    return repository.findByPatientId(patientId).stream().map(this::toDomain).toList();
  }

  @Override
  @Transactional
  public void update(MedicalRecord record) {
    var jpa = toJpa(record);
    jpa.setUpdatedAt(LocalDateTime.now());
    repository.save(jpa);
  }

  private MedicalRecordJpa toJpa(MedicalRecord record) {
    return MedicalRecordJpa.builder()
        .id(record.getId())
        .appointmentId(record.getAppointmentId())
        .patientId(record.getPatientId())
        .diagnosis(record.getDiagnosis())
        .prescription(record.getPrescription())
        .consultationDate(record.getConsultationDate())
        .createdAt(record.getCreatedAt())
        .updatedAt(record.getUpdatedAt())
        .build();
  }

  private MedicalRecord toDomain(MedicalRecordJpa jpa) {
    return MedicalRecord.reconstruct(
        jpa.getId(),
        jpa.getAppointmentId(),
        jpa.getPatientId(),
        jpa.getDiagnosis(),
        jpa.getPrescription(),
        jpa.getConsultationDate(),
        jpa.getCreatedAt());
  }
}
