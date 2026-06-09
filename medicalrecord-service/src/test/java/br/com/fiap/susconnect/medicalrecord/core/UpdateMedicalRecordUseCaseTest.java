/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.medicalrecord.core.domain.MedicalRecordNotFoundDomainException;
import br.com.fiap.susconnect.medicalrecord.core.domain.entity.MedicalRecord;
import br.com.fiap.susconnect.medicalrecord.core.dto.MedicalRecordOutput;
import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import br.com.fiap.susconnect.medicalrecord.core.usecase.UpdateMedicalRecordUseCase;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateMedicalRecordUseCaseTest {

  private MedicalRecordGateway gateway;
  private UpdateMedicalRecordUseCase useCase;

  @BeforeEach
  void setUp() {
    gateway = mock(MedicalRecordGateway.class);
    useCase = new UpdateMedicalRecordUseCase(gateway);
  }

  @Test
  void shouldUpdateAllFieldsWhenProvided() {
    UUID id = UUID.randomUUID();
    MedicalRecord existing =
        MedicalRecord.reconstruct(
            id,
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Old diagnosis",
            "Old prescription",
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().minusDays(1));
    when(gateway.findById(id)).thenReturn(Optional.of(existing));

    LocalDateTime newDate = LocalDateTime.now();
    MedicalRecordOutput output = useCase.execute(id, "New diagnosis", "New prescription", newDate);

    assertThat(output.diagnosis()).isEqualTo("New diagnosis");
    assertThat(output.prescription()).isEqualTo("New prescription");
    assertThat(output.consultationDate()).isEqualTo(newDate);
    verify(gateway).update(any(MedicalRecord.class));
  }

  @Test
  void shouldNotOverwriteFieldsWhenNullPassed() {
    UUID id = UUID.randomUUID();
    MedicalRecord existing =
        MedicalRecord.reconstruct(
            id,
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Original diagnosis",
            "Original prescription",
            LocalDateTime.now(),
            LocalDateTime.now());
    when(gateway.findById(id)).thenReturn(Optional.of(existing));

    MedicalRecordOutput output = useCase.execute(id, null, null, null);

    assertThat(output.diagnosis()).isEqualTo("Original diagnosis");
    assertThat(output.prescription()).isEqualTo("Original prescription");
    verify(gateway).update(any(MedicalRecord.class));
  }

  @Test
  void shouldThrowExceptionWhenRecordNotFound() {
    UUID id = UUID.randomUUID();
    when(gateway.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.execute(id, "diagnosis", null, null))
        .isInstanceOf(MedicalRecordNotFoundDomainException.class)
        .hasMessageContaining(id.toString());
  }
}
