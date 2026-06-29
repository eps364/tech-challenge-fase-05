/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.dto;

import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;

public final class AppointmentOutputMapper {

  private AppointmentOutputMapper() {}

  public static AppointmentOutput toOutput(Appointment appointment) {
    return new AppointmentOutput(
        appointment.getId(),
        appointment.getPatientId(),
        appointment.getProfessionalId(),
        appointment.getDateTime(),
        appointment.getStatus().name(),
        appointment.getAppointmentType().name(),
        appointment.getServiceName(),
        appointment.getFacilityName(),
        appointment.getPreparationNotes(),
        appointment.getPatientNotification(),
        appointment.getLastNotifiedAt(),
        appointment.getRescheduledFrom(),
        appointment.getCancellationReason(),
        appointment.getCreatedAt());
  }
}
