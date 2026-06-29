/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.config;

import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentOfferGateway;
import br.com.fiap.susconnect.appointment.core.gateway.PatientGateway;
import br.com.fiap.susconnect.appointment.core.usecase.AcceptAppointmentOfferUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.CancelAppointmentUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.DeclineAppointmentOfferUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.GetAppointmentUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ListAppointmentOffersByPatientUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ListAppointmentsByPatientUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.ListPatientsUseCase;
import br.com.fiap.susconnect.appointment.core.usecase.NotifyUpcomingAppointmentsUseCase;
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
  public CancelAppointmentUseCase cancelAppointmentUseCase(
      AppointmentGateway gateway, AppointmentOfferGateway appointmentOfferGateway) {
    return new CancelAppointmentUseCase(gateway, appointmentOfferGateway);
  }

  @Bean
  public ListAppointmentsByPatientUseCase listAppointmentsByPatientUseCase(
      AppointmentGateway gateway) {
    return new ListAppointmentsByPatientUseCase(gateway);
  }

  @Bean
  public NotifyUpcomingAppointmentsUseCase notifyUpcomingAppointmentsUseCase(
      AppointmentGateway gateway) {
    return new NotifyUpcomingAppointmentsUseCase(gateway);
  }

  @Bean
  public ListAppointmentOffersByPatientUseCase listAppointmentOffersByPatientUseCase(
      AppointmentOfferGateway appointmentOfferGateway) {
    return new ListAppointmentOffersByPatientUseCase(appointmentOfferGateway);
  }

  @Bean
  public AcceptAppointmentOfferUseCase acceptAppointmentOfferUseCase(
      AppointmentGateway appointmentGateway, AppointmentOfferGateway appointmentOfferGateway) {
    return new AcceptAppointmentOfferUseCase(appointmentGateway, appointmentOfferGateway);
  }

  @Bean
  public DeclineAppointmentOfferUseCase declineAppointmentOfferUseCase(
      AppointmentOfferGateway appointmentOfferGateway) {
    return new DeclineAppointmentOfferUseCase(appointmentOfferGateway);
  }

  @Bean
  public ListPatientsUseCase listPatientsUseCase(PatientGateway patientGateway) {
    return new ListPatientsUseCase(patientGateway);
  }
}
