/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.domain.entity;

/** Life-cycle states for a scheduled appointment. */
public enum AppointmentStatus {
  CONFIRMED,
  IN_PROGRESS,
  COMPLETED,
  CANCELLED,
  NO_SHOW
}
