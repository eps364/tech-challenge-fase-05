/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.repository;

import br.com.fiap.susconnect.medicalrecord.infra.entity.MedicalRecordJpa;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data JPA repository for medical records. */
public interface MedicalRecordRepository extends JpaRepository<MedicalRecordJpa, UUID> {

  List<MedicalRecordJpa> findByPatientId(UUID patientId);
}
