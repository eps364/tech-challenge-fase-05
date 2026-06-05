/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.domain;

public record LoginResult(
    String accessToken, String refreshToken, long expiresIn, String tokenType) {}
