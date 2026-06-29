/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentValidationDomainException;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutputMapper;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyUpcomingAppointmentsUseCase {

  private final AppointmentGateway appointmentGateway;

  public NotifyUpcomingAppointmentsUseCase(AppointmentGateway appointmentGateway) {
    this.appointmentGateway = appointmentGateway;
  }

  public List<AppointmentOutput> execute(int hoursAhead) {
    return execute(LocalDateTime.now(), hoursAhead);
  }

  public List<AppointmentOutput> execute(LocalDateTime now, int hoursAhead) {
    if (hoursAhead <= 0) {
      throw new AppointmentValidationDomainException("The notification window must be positive");
    }

    LocalDateTime end = now.plusHours(hoursAhead);
    log.info("Notifying appointments scheduled between {} and {}", now, end);
    return appointmentGateway.findConfirmedBetween(now, end).stream()
        .map(
            appointment -> {
              appointment.remindPatient(now);
              appointmentGateway.update(appointment);
              return AppointmentOutputMapper.toOutput(appointment);
            })
        .toList();
  }
}
