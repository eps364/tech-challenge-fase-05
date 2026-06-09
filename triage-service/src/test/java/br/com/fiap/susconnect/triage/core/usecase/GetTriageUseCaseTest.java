/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.triage.core.domain.TriageNotFoundDomainException;
import br.com.fiap.susconnect.triage.core.domain.entity.RiskLevel;
import br.com.fiap.susconnect.triage.core.domain.entity.Triage;
import br.com.fiap.susconnect.triage.core.gateway.TriageGateway;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTriageUseCaseTest {

  @Mock private TriageGateway triageGateway;

  private GetTriageUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new GetTriageUseCase(triageGateway);
  }

  @Test
  void execute_shouldReturnTriageOutput() {
    var triageId = UUID.randomUUID();
    var patientId = UUID.randomUUID();
    var createdAt = LocalDateTime.now().minusMinutes(30);
    var updatedAt = LocalDateTime.now();
    var triage = Triage.reconstruct(triageId, patientId, RiskLevel.ORANGE, createdAt, updatedAt);
    when(triageGateway.findById(triageId)).thenReturn(Optional.of(triage));

    var output = useCase.execute(triageId);

    assertThat(output.id()).isEqualTo(triageId);
    assertThat(output.patientId()).isEqualTo(patientId);
    assertThat(output.riskLevel()).isEqualTo(RiskLevel.ORANGE);
    assertThat(output.createdAt()).isEqualTo(createdAt);
    assertThat(output.updatedAt()).isEqualTo(updatedAt);
  }

  @Test
  void execute_shouldThrowWhenTriageNotFound() {
    var triageId = UUID.randomUUID();
    when(triageGateway.findById(triageId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.execute(triageId))
        .isInstanceOf(TriageNotFoundDomainException.class)
        .hasMessageContaining(triageId.toString());
  }
}
