/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.medicalrecord.core.domain.MedicalRecordNotFoundDomainException;
import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import br.com.fiap.susconnect.medicalrecord.core.usecase.GetMedicalRecordUseCase;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetMedicalRecordUseCaseTest {

  private MedicalRecordGateway gateway;
  private GetMedicalRecordUseCase useCase;

  @BeforeEach
  void setUp() {
    gateway = mock(MedicalRecordGateway.class);
    useCase = new GetMedicalRecordUseCase(gateway);
  }

  @Test
  void shouldReturnMedicalRecordWhenFound() {
    UUID id = UUID.randomUUID();
    UUID appointmentId = UUID.randomUUID();
    UUID patientId = UUID.randomUUID();
    MedicalRecord record =
        MedicalRecord.reconstruct(
            id, appointmentId, patientId, "Flu", "Rest", LocalDateTime.now(), LocalDateTime.now());
    when(gateway.findById(id)).thenReturn(Optional.of(record));

    MedicalRecordOutput output = useCase.execute(id);

    assertThat(output).isNotNull();
    assertThat(output.id()).isEqualTo(id);
    assertThat(output.appointmentId()).isEqualTo(appointmentId);
    assertThat(output.patientId()).isEqualTo(patientId);
    assertThat(output.diagnosis()).isEqualTo("Flu");
    assertThat(output.prescription()).isEqualTo("Rest");
  }

  @Test
  void shouldThrowExceptionWhenNotFound() {
    UUID id = UUID.randomUUID();
    when(gateway.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.execute(id))
        .isInstanceOf(MedicalRecordNotFoundDomainException.class)
        .hasMessageContaining(id.toString());
  }
}
