/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.config;

import br.com.fiap.susconnect.triage.core.gateway.TriageEventPublisher;
import br.com.fiap.susconnect.triage.core.gateway.TriageGateway;
import br.com.fiap.susconnect.triage.core.usecase.ClassifyRiskUseCase;
import br.com.fiap.susconnect.triage.core.usecase.CreateTriageUseCase;
import br.com.fiap.susconnect.triage.core.usecase.GetTriageUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Use Case Configuration - Registers core use cases as Spring beans
 *
 * <p>This class bridges Clean Architecture (framework-independent core) with Spring Framework by
 * manually configuring use cases as beans.
 */
@Configuration
public class UseCaseConfig {

  @Bean
  public CreateTriageUseCase createTriageUseCase(TriageGateway triageGateway) {
    return new CreateTriageUseCase(triageGateway);
  }

  @Bean
  public ClassifyRiskUseCase classifyRiskUseCase(
      TriageGateway triageGateway, TriageEventPublisher triageEventPublisher) {
    return new ClassifyRiskUseCase(triageGateway, triageEventPublisher);
  }

  @Bean
  public GetTriageUseCase getTriageUseCase(TriageGateway triageGateway) {
    return new GetTriageUseCase(triageGateway);
  }
}
