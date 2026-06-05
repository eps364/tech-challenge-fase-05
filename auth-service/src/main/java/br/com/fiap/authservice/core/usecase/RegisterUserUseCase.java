/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.usecase;

import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;

public class RegisterUserUseCase {
  private final IdentityProviderGateway identityProviderGateway;

  public RegisterUserUseCase(IdentityProviderGateway identityProviderGateway) {
    this.identityProviderGateway = identityProviderGateway;
  }

  public void execute(
      String username, String email, String firstName, String lastName, String password) {
    User user = User.create(username, email, firstName, lastName, password);
    identityProviderGateway.createUser(user);
  }
}
