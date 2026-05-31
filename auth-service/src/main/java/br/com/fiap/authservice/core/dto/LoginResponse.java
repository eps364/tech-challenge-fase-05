package br.com.fiap.authservice.core.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken,
    long expiresIn,
    String tokenType
) {}
