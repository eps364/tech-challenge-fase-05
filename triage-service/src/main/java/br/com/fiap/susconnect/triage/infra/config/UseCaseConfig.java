/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.triage.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.susconnect.triage.core.gateway.TriageGateway;
import br.com.fiap.susconnect.triage.core.usecase.CreateTriageUseCase;

/**
 * Use Case Configuration - Registers core use cases as Spring beans
 *
 * <p>This class bridges Clean Architecture (framework-independent core) with Spring Framework by
 * manually configuring use cases as beans.
 */
@Configuration
public class UseCaseConfig {

  /**
   * Creates CreateTriageUseCase bean
   *
   * @param triageGateway The gateway implementation (injected by Spring)
   * @return Configured use case instance
   */
  @Bean
  public CreateTriageUseCase createTriageUseCase(TriageGateway triageGateway) {
    return new CreateTriageUseCase(triageGateway);
  }
}
