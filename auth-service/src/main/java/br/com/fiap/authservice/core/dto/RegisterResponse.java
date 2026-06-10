/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.core.dto;

import java.util.Set;

public record RegisterResponse(
    String username, String email, String firstName, String lastName, Set<String> roles) {}
