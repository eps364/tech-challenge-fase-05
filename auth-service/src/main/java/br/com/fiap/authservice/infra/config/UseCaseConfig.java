/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.infra.config;

import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import br.com.fiap.authservice.core.gateway.TokenBlacklistGateway;
import br.com.fiap.authservice.core.usecase.LoginUseCase;
import br.com.fiap.authservice.core.usecase.LogoutUseCase;
import br.com.fiap.authservice.core.usecase.RefreshTokenUseCase;
import br.com.fiap.authservice.core.usecase.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
  @Bean
  public RegisterUserUseCase registerUserUseCase(IdentityProviderGateway identityProviderGateway) {
    return new RegisterUserUseCase(identityProviderGateway);
  }

  @Bean
  public LoginUseCase loginUseCase(IdentityProviderGateway identityProviderGateway) {
    return new LoginUseCase(identityProviderGateway);
  }

  @Bean
  public LogoutUseCase logoutUseCase(
      IdentityProviderGateway identityProviderGateway,
      TokenBlacklistGateway tokenBlacklistGateway) {
    return new LogoutUseCase(identityProviderGateway, tokenBlacklistGateway);
  }

  @Bean
  public RefreshTokenUseCase refreshTokenUseCase(IdentityProviderGateway identityProviderGateway) {
    return new RefreshTokenUseCase(identityProviderGateway);
  }
}
