/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "Username may only contain letters, digits, dots, underscores and hyphens")
        String username,
    @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid address")
        @Size(max = 100, message = "Email must be at most 100 characters")
        String email,
    @NotBlank(message = "First name is required")
        @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
        String firstName,
    @NotBlank(message = "Last name is required")
        @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
        String lastName,
    @NotBlank(message = "Password is required")
        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message =
                "Password must contain at least one uppercase letter, one lowercase letter and one digit")
        String password) {}
