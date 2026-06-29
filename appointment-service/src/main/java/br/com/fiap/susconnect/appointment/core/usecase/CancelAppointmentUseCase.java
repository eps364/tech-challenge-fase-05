/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentNotFoundDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOffer;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutput;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutputMapper;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutputMapper;
import br.com.fiap.susconnect.appointment.core.dto.CancellationResultOutput;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentOfferGateway;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CancelAppointmentUseCase {

  private final AppointmentGateway appointmentGateway;
  private final AppointmentOfferGateway appointmentOfferGateway;

  public CancelAppointmentUseCase(AppointmentGateway appointmentGateway) {
    this(appointmentGateway, null);
  }

  public CancelAppointmentUseCase(
      AppointmentGateway appointmentGateway, AppointmentOfferGateway appointmentOfferGateway) {
    this.appointmentGateway = appointmentGateway;
    this.appointmentOfferGateway = appointmentOfferGateway;
  }

  public AppointmentOutput execute(UUID id) {
    return executeWithReallocation(id, null).appointment();
  }

  public CancellationResultOutput executeWithReallocation(UUID id, String reason) {
    log.info("Cancelling appointment id={}", id);
    Appointment appointment =
        appointmentGateway
            .findById(id)
            .orElseThrow(
                () -> new AppointmentNotFoundDomainException("Appointment not found: " + id));

    if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
      log.warn("Appointment {} is already cancelled", id);
      return new CancellationResultOutput(AppointmentOutputMapper.toOutput(appointment), null);
    }

    appointment.cancel(reason);
    appointmentGateway.update(appointment);
    AppointmentOfferOutput offerOutput = maybeCreateOffer(appointment);
    log.info("Appointment {} cancelled successfully", id);
    return new CancellationResultOutput(AppointmentOutputMapper.toOutput(appointment), offerOutput);
  }

  private AppointmentOfferOutput maybeCreateOffer(Appointment openedAppointment) {
    if (appointmentOfferGateway == null) {
      return null;
    }

    return appointmentGateway
        .findNextConfirmedAfter(
            openedAppointment.getDateTime(),
            openedAppointment.getAppointmentType(),
            openedAppointment.getServiceName(),
            openedAppointment.getPatientId())
        .map(candidate -> createOffer(openedAppointment, candidate))
        .orElse(null);
  }

  private AppointmentOfferOutput createOffer(Appointment openedAppointment, Appointment candidate) {
    AppointmentOffer offer = AppointmentOffer.create(openedAppointment, candidate);
    appointmentOfferGateway.save(offer);
    log.info(
        "Appointment offer {} created for patientId={} from appointmentId={}",
        offer.getId(),
        offer.getCandidatePatientId(),
        candidate.getId());
    return AppointmentOfferOutputMapper.toOutput(offer);
  }
}
