/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.infra.web.controller;

import br.com.fiap.authservice.core.dto.LoginRequest;
import br.com.fiap.authservice.core.dto.LoginResponse;
import br.com.fiap.authservice.core.dto.RefreshTokenRequest;
import br.com.fiap.authservice.core.dto.RegisterRequest;
import br.com.fiap.authservice.core.usecase.LoginUseCase;
import br.com.fiap.authservice.core.usecase.LogoutUseCase;
import br.com.fiap.authservice.core.usecase.RefreshTokenUseCase;
import br.com.fiap.authservice.core.usecase.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication and Authorization Endpoints")
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
  @Operation(summary = "Register a new user")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "User registered successfully"),
    @ApiResponse(responseCode = "400", description = "Validation error"),
    @ApiResponse(responseCode = "422", description = "Validation failed")
  })
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
  @Operation(summary = "Authenticate user and return JWT tokens")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Login successful"),
    @ApiResponse(responseCode = "400", description = "Invalid credentials"),
    @ApiResponse(responseCode = "422", description = "Validation failed")
  })
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    var result = loginUseCase.execute(request.username(), request.password());
    return ResponseEntity.ok(
        new LoginResponse(
            result.accessToken(), result.refreshToken(), result.expiresIn(), result.tokenType()));
  }

  @PostMapping("/refresh")
  @Operation(summary = "Refresh access token using refresh token")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token"),
    @ApiResponse(responseCode = "422", description = "Validation failed")
  })
  public ResponseEntity<LoginResponse> refreshToken(
      @Valid @RequestBody RefreshTokenRequest request) {
    var result = refreshTokenUseCase.execute(request.refreshToken());
    return ResponseEntity.ok(
        new LoginResponse(
            result.accessToken(), result.refreshToken(), result.expiresIn(), result.tokenType()));
  }

  @PostMapping("/logout")
  @Operation(summary = "Invalidate current JWT token")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Logged out successfully"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<String> logout(@AuthenticationPrincipal Jwt jwt) {
    long expiresIn =
        jwt.getExpiresAt() != null
            ? (jwt.getExpiresAt().getEpochSecond() - System.currentTimeMillis() / 1000)
            : 3600;
    logoutUseCase.execute(jwt.getTokenValue(), expiresIn);
    return ResponseEntity.ok("Logged out successfully");
  }

  @GetMapping("/test/public")
  @Operation(summary = "Public health check endpoint")
  @ApiResponse(responseCode = "200", description = "Service is accessible")
  public ResponseEntity<String> publicTest() {
    return ResponseEntity.ok("This is a public endpoint");
  }

  @GetMapping("/test/private")
  @Operation(summary = "Private endpoint requiring valid JWT")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Authenticated user info"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<String> privateTest(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(
        "Hello " + jwt.getClaimAsString("preferred_username") + "! This is a private endpoint");
  }
}
