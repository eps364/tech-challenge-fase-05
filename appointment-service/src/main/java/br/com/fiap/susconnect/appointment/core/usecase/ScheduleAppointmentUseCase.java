/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentConflictDomainException;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentValidationDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentType;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutputMapper;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleAppointmentUseCase {

  private final AppointmentGateway appointmentGateway;

  public ScheduleAppointmentUseCase(AppointmentGateway appointmentGateway) {
    this.appointmentGateway = appointmentGateway;
  }

  public AppointmentOutput execute(UUID patientId, LocalDateTime dateTime) {
    return execute(
        patientId,
        null,
        dateTime,
        AppointmentType.CONSULTATION,
        "Consulta clinica",
        "UBS Central",
        null);
  }

  public AppointmentOutput execute(
      UUID patientId,
      UUID professionalId,
      LocalDateTime dateTime,
      AppointmentType appointmentType,
      String serviceName,
      String facilityName,
      String preparationNotes) {
    log.info(
        "Scheduling appointment for patientId={}, type={}, service={}",
        patientId,
        appointmentType,
        serviceName);

    if (dateTime.isBefore(LocalDateTime.now().minusSeconds(30))) {
      throw new AppointmentValidationDomainException(
          "The appointment date and time cannot be in the past");
    }

    if (serviceName == null || serviceName.isBlank()) {
      throw new AppointmentValidationDomainException("The service name cannot be blank");
    }

    if (facilityName == null || facilityName.isBlank()) {
      throw new AppointmentValidationDomainException("The facility name cannot be blank");
    }

    if (appointmentGateway.existsByDateTimeAndStatus(dateTime, AppointmentStatus.CONFIRMED)) {
      throw new AppointmentConflictDomainException(
          "There is already a confirmed appointment at the requested time slot");
    }

    Appointment appointment =
        Appointment.create(
            patientId,
            professionalId,
            dateTime,
            appointmentType,
            serviceName,
            facilityName,
            preparationNotes);
    appointmentGateway.save(appointment);
    log.info("Appointment scheduled: id={}", appointment.getId());
    return AppointmentOutputMapper.toOutput(appointment);
  }
}
