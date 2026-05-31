package br.com.fiap.authservice.core.domain;

public record LoginResult(
    String accessToken,
    String refreshToken,
    long expiresIn,
    String tokenType
) {}
