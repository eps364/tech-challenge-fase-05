/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ActionAlertOutput(
    UUID actionId, UUID territoryId, String territoryName, LocalDate plannedEnd, String reason) {}
