/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.triage.core.domain.TriageNotFoundDomainException;
import br.com.fiap.susconnect.triage.core.domain.entity.RiskLevel;
import br.com.fiap.susconnect.triage.core.domain.entity.Triage;
import br.com.fiap.susconnect.triage.core.gateway.TriageEventPublisher;
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
class ClassifyRiskUseCaseTest {

  @Mock private TriageGateway triageGateway;
  @Mock private TriageEventPublisher eventPublisher;

  private ClassifyRiskUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ClassifyRiskUseCase(triageGateway, eventPublisher);
  }

  @Test
  void execute_shouldUpdateRiskLevel() {
    var triageId = UUID.randomUUID();
    var patientId = UUID.randomUUID();
    var triage = Triage.reconstruct(triageId, patientId, RiskLevel.BLUE, LocalDateTime.now(), null);
    when(triageGateway.findById(triageId)).thenReturn(Optional.of(triage));

    var output = useCase.execute(triageId, RiskLevel.RED);

    assertThat(output.riskLevel()).isEqualTo(RiskLevel.RED);
  }

  @Test
  void execute_shouldCallUpdateOnGateway() {
    var triageId = UUID.randomUUID();
    var patientId = UUID.randomUUID();
    var triage = Triage.reconstruct(triageId, patientId, RiskLevel.BLUE, LocalDateTime.now(), null);
    when(triageGateway.findById(triageId)).thenReturn(Optional.of(triage));

    useCase.execute(triageId, RiskLevel.ORANGE);

    verify(triageGateway).update(triage);
  }

  @Test
  void execute_shouldPublishEvent() {
    var triageId = UUID.randomUUID();
    var patientId = UUID.randomUUID();
    var triage = Triage.reconstruct(triageId, patientId, RiskLevel.BLUE, LocalDateTime.now(), null);
    when(triageGateway.findById(triageId)).thenReturn(Optional.of(triage));

    useCase.execute(triageId, RiskLevel.YELLOW);

    verify(eventPublisher).publishClassified(any());
  }

  @Test
  void execute_shouldThrowWhenTriageNotFound() {
    var triageId = UUID.randomUUID();
    when(triageGateway.findById(triageId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.execute(triageId, RiskLevel.RED))
        .isInstanceOf(TriageNotFoundDomainException.class)
        .hasMessageContaining(triageId.toString());
  }

  @Test
  void execute_shouldNotPublishEvent_whenTriageNotFound() {
    var triageId = UUID.randomUUID();
    when(triageGateway.findById(triageId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.execute(triageId, RiskLevel.RED))
        .isInstanceOf(TriageNotFoundDomainException.class);

    verify(eventPublisher, never()).publishClassified(any());
  }

  @Test
  void execute_shouldReturnOutputWithAllFields() {
    var triageId = UUID.randomUUID();
    var patientId = UUID.randomUUID();
    var createdAt = LocalDateTime.now().minusHours(1);
    var triage = Triage.reconstruct(triageId, patientId, RiskLevel.BLUE, createdAt, null);
    when(triageGateway.findById(triageId)).thenReturn(Optional.of(triage));

    var output = useCase.execute(triageId, RiskLevel.GREEN);

    assertThat(output.id()).isEqualTo(triageId);
    assertThat(output.patientId()).isEqualTo(patientId);
    assertThat(output.riskLevel()).isEqualTo(RiskLevel.GREEN);
    assertThat(output.createdAt()).isEqualTo(createdAt);
    assertThat(output.updatedAt()).isNotNull();
  }
}
