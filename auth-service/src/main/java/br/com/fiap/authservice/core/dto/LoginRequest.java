package br.com.fiap.authservice.core.dto;

public record LoginRequest(
    String username,
    String password
) {}
