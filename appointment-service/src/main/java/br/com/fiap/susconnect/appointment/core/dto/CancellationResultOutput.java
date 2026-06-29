/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

public record CancellationResultOutput(
    AppointmentOutput appointment, AppointmentOfferOutput generatedOffer) {}
