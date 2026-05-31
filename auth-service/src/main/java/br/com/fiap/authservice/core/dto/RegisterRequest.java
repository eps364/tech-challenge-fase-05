package br.com.fiap.authservice.core.dto;

public record RegisterRequest(
    String username,
    String email,
    String firstName,
    String lastName,
    String password
) {}
