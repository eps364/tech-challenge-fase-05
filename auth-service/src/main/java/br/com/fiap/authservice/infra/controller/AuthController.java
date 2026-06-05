/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.infra.controller;

import br.com.fiap.authservice.core.dto.LoginRequest;
import br.com.fiap.authservice.core.dto.LoginResponse;
import br.com.fiap.authservice.core.dto.RefreshTokenRequest;
import br.com.fiap.authservice.core.dto.RegisterRequest;
import br.com.fiap.authservice.core.usecase.LoginUseCase;
import br.com.fiap.authservice.core.usecase.LogoutUseCase;
import br.com.fiap.authservice.core.usecase.RefreshTokenUseCase;
import br.com.fiap.authservice.core.usecase.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final RegisterUserUseCase registerUserUseCase;
  private final LoginUseCase loginUseCase;
  private final LogoutUseCase logoutUseCase;
  private final RefreshTokenUseCase refreshTokenUseCase;

  public AuthController(
      RegisterUserUseCase registerUserUseCase,
      LoginUseCase loginUseCase,
      LogoutUseCase logoutUseCase,
      RefreshTokenUseCase refreshTokenUseCase) {
    this.registerUserUseCase = registerUserUseCase;
    this.loginUseCase = loginUseCase;
    this.logoutUseCase = logoutUseCase;
    this.refreshTokenUseCase = refreshTokenUseCase;
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
    registerUserUseCase.execute(
        request.username(),
        request.email(),
        request.firstName(),
        request.lastName(),
        request.password());
    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    var result = loginUseCase.execute(request.username(), request.password());
    return ResponseEntity.ok(
        new LoginResponse(
            result.accessToken(), result.refreshToken(), result.expiresIn(), result.tokenType()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(
      @Valid @RequestBody RefreshTokenRequest request) {
    var result = refreshTokenUseCase.execute(request.refreshToken());
    return ResponseEntity.ok(
        new LoginResponse(
            result.accessToken(), result.refreshToken(), result.expiresIn(), result.tokenType()));
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@AuthenticationPrincipal Jwt jwt) {
    long expiresIn =
        jwt.getExpiresAt() != null
            ? (jwt.getExpiresAt().getEpochSecond() - System.currentTimeMillis() / 1000)
            : 3600;
    logoutUseCase.execute(jwt.getTokenValue(), expiresIn);
    return ResponseEntity.ok("Logged out successfully");
  }

  @GetMapping("/test/public")
  public ResponseEntity<String> publicTest() {
    return ResponseEntity.ok("This is a public endpoint");
  }

  @GetMapping("/test/private")
  public ResponseEntity<String> privateTest(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(
        "Hello " + jwt.getClaimAsString("preferred_username") + "! This is a private endpoint");
  }
}
