/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.usecase;

import br.com.fiap.authservice.core.domain.LoginResult;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;

public class RefreshTokenUseCase {
  private final IdentityProviderGateway identityProviderGateway;

  public RefreshTokenUseCase(IdentityProviderGateway identityProviderGateway) {
    this.identityProviderGateway = identityProviderGateway;
  }

  public LoginResult execute(String refreshToken) {
    return identityProviderGateway.refreshToken(refreshToken);
  }
}
