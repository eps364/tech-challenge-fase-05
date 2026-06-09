/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import br.com.fiap.susconnect.medicalrecord.core.usecase.CreateMedicalRecordUseCase;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateMedicalRecordUseCaseTest {

  private MedicalRecordGateway gateway;
  private CreateMedicalRecordUseCase useCase;

  @BeforeEach
  void setUp() {
    gateway = mock(MedicalRecordGateway.class);
    useCase = new CreateMedicalRecordUseCase(gateway);
  }

  @Test
  void shouldCreateMedicalRecordSuccessfully() {
    UUID appointmentId = UUID.randomUUID();
    UUID patientId = UUID.randomUUID();
    String diagnosis = "Hypertension";
    String prescription = "Amlodipine 5mg";
    LocalDateTime consultationDate = LocalDateTime.now();

    MedicalRecordOutput output =
        useCase.execute(appointmentId, patientId, diagnosis, prescription, consultationDate);

    assertThat(output).isNotNull();
    assertThat(output.appointmentId()).isEqualTo(appointmentId);
    assertThat(output.patientId()).isEqualTo(patientId);
    assertThat(output.diagnosis()).isEqualTo(diagnosis);
    assertThat(output.prescription()).isEqualTo(prescription);
    assertThat(output.consultationDate()).isEqualTo(consultationDate);
    assertThat(output.id()).isNotNull();
    assertThat(output.createdAt()).isNotNull();
    verify(gateway).save(any(MedicalRecord.class));
  }

  @Test
  void shouldCreateMedicalRecordWithNullOptionalFields() {
    UUID appointmentId = UUID.randomUUID();
    UUID patientId = UUID.randomUUID();

    MedicalRecordOutput output = useCase.execute(appointmentId, patientId, null, null, null);

    assertThat(output).isNotNull();
    assertThat(output.diagnosis()).isNull();
    assertThat(output.prescription()).isNull();
    assertThat(output.consultationDate()).isNull();
    verify(gateway).save(any(MedicalRecord.class));
  }

  @Test
  void shouldPersistCorrectFieldsToGateway() {
    UUID appointmentId = UUID.randomUUID();
    UUID patientId = UUID.randomUUID();
    String diagnosis = "Diabetes Type 2";
    String prescription = "Metformin 500mg";

    doAnswer(
            invocation -> {
              MedicalRecord record = invocation.getArgument(0);
              assertThat(record.getAppointmentId()).isEqualTo(appointmentId);
              assertThat(record.getPatientId()).isEqualTo(patientId);
              assertThat(record.getDiagnosis()).isEqualTo(diagnosis);
              assertThat(record.getPrescription()).isEqualTo(prescription);
              return null;
            })
        .when(gateway)
        .save(any(MedicalRecord.class));

    useCase.execute(appointmentId, patientId, diagnosis, prescription, null);

    verify(gateway).save(any(MedicalRecord.class));
  }
}
