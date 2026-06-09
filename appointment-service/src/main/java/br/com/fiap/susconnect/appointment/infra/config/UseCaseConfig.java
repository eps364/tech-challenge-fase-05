/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.config;

import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import br.com.fiap.susconnect.appointment.core.usecase.CancelAppointmentUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.GetAppointmentUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ListAppointmentsByPatientUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ScheduleAppointmentUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  public ScheduleAppointmentUseCase scheduleAppointmentUseCase(AppointmentGateway gateway) {
    return new ScheduleAppointmentUseCase(gateway);
  }

  @Bean
  public GetAppointmentUseCase getAppointmentUseCase(AppointmentGateway gateway) {
    return new GetAppointmentUseCase(gateway);
  }

  @Bean
  public CancelAppointmentUseCase cancelAppointmentUseCase(AppointmentGateway gateway) {
    return new CancelAppointmentUseCase(gateway);
  }

  @Bean
  public ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase(
      AppointmentGateway gateway) {
    return new ListAppointmentsByPatientUseCase(gateway);
  }
}
