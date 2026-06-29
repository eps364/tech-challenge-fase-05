/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import java.util.UUID;

public record PatientOutput(UUID id, String fullName, String email, String phone) {}
