/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.gateway;

import br.com.fiap.authservice.core.domain.LoginResult;
import br.com.fiap.authservice.core.domain.User;

public interface IdentityProviderGateway {
  User createUser(User user);

  LoginResult login(String username, String password);

  LoginResult refreshToken(String refreshToken);

  void logout(String token);
}
