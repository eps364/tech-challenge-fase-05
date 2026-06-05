/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.dto;

public record LoginResponse(
    String accessToken, String refreshToken, long expiresIn, String tokenType) {}
