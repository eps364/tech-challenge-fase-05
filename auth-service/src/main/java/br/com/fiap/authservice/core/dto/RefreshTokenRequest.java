/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "Refresh token is required") String refreshToken) {}
