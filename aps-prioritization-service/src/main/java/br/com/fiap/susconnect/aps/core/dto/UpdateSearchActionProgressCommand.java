/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.core.dto;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;

public record UpdateSearchActionProgressCommand(
    ActionStatus status, int performedCount, String resultNotes) {}
