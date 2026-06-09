/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core.gateway;

import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Output port for medical record persistence operations. */
public interface MedicalRecordGateway {

  void save(MedicalRecord record);

  Optional<MedicalRecord> findById(UUID id);

  List<MedicalRecord> findByPatientId(UUID patientId);

  void update(MedicalRecord record);
}
