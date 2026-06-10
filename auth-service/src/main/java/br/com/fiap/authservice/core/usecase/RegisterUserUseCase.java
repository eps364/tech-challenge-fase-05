/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.usecase;

import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.dto.RegisterOutput;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;

public class RegisterUserUseCase {
  private final IdentityProviderGateway identityProviderGateway;

  public RegisterUserUseCase(IdentityProviderGateway identityProviderGateway) {
    this.identityProviderGateway = identityProviderGateway;
  }

  public RegisterOutput execute(
      String username, String email, String firstName, String lastName, String password) {
    User user = User.create(username, email, firstName, lastName, password);
    User created = identityProviderGateway.createUser(user);
    return new RegisterOutput(
        created.getUsername(),
        created.getEmail(),
        created.getFirstName(),
        created.getLastName(),
        created.getRoles());
  }
}
