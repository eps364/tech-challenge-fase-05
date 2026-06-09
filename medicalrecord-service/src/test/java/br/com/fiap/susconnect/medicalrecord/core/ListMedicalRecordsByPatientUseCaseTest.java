/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import br.com.fiap.susconnect.medicalrecord.core.usecase.ListMedicalRecordsByPatientUseCase;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListMedicalRecordsByPatientUseCaseTest {

  @Mock private MedicalRecordGateway gateway;

  private ListMedicalRecordsByPatientUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ListMedicalRecordsByPatientUseCase(gateway);
  }

  @Test
  void shouldReturnMedicalRecordsForPatient() {
    UUID patientId = UUID.randomUUID();
    UUID appointmentId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    MedicalRecord record =
        MedicalRecord.reconstruct(
            UUID.randomUUID(), appointmentId, patientId, "Hypertension", "Losartan 50mg", now, now);

    when(gateway.findByPatientId(patientId)).thenReturn(List.of(record));

    List<MedicalRecordOutput> result = useCase.execute(patientId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).patientId()).isEqualTo(patientId);
    assertThat(result.get(0).appointmentId()).isEqualTo(appointmentId);
    assertThat(result.get(0).diagnosis()).isEqualTo("Hypertension");
  }

  @Test
  void shouldReturnEmptyListWhenNoRecords() {
    UUID patientId = UUID.randomUUID();
    when(gateway.findByPatientId(patientId)).thenReturn(List.of());

    List<MedicalRecordOutput> result = useCase.execute(patientId);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldReturnMultipleRecords() {
    UUID patientId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    MedicalRecord r1 =
        MedicalRecord.reconstruct(
            UUID.randomUUID(), UUID.randomUUID(), patientId, "Diabetes", null, now, now);
    MedicalRecord r2 =
        MedicalRecord.reconstruct(
            UUID.randomUUID(), UUID.randomUUID(), patientId, "Asthma", "Salbutamol", now, now);

    when(gateway.findByPatientId(patientId)).thenReturn(List.of(r1, r2));

    List<MedicalRecordOutput> result = useCase.execute(patientId);

    assertThat(result).hasSize(2);
    assertThat(result).extracting(MedicalRecordOutput::patientId).containsOnly(patientId);
  }
}
