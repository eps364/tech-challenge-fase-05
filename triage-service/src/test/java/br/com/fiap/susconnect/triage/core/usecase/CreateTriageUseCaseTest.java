/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import br.com.fiap.susconnect.triage.core.domain.entity.RiskLevel;
import br.com.fiap.susconnect.triage.core.domain.entity.Triage;
import br.com.fiap.susconnect.triage.core.gateway.TriageGateway;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateTriageUseCaseTest {

  @Mock private TriageGateway triageGateway;

  private CreateTriageUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new CreateTriageUseCase(triageGateway);
  }

  @Test
  void execute_shouldCallSaveOnGateway() {
    var patientId = UUID.randomUUID();

    useCase.execute(patientId);

    verify(triageGateway).save(any(Triage.class));
  }

  @Test
  void execute_shouldSetInitialRiskLevelToBlue() {
    var patientId = UUID.randomUUID();

    var triage = useCase.execute(patientId);

    assertThat(triage.getRiskLevel()).isEqualTo(RiskLevel.BLUE);
  }

  @Test
  void execute_shouldSetPatientId() {
    var patientId = UUID.randomUUID();

    var triage = useCase.execute(patientId);

    assertThat(triage.getPatientId()).isEqualTo(patientId);
  }

  @Test
  void execute_shouldGenerateNewId() {
    var patientId = UUID.randomUUID();

    var triage = useCase.execute(patientId);

    assertThat(triage.getId()).isNotNull();
  }
}
