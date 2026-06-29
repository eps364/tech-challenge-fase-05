/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentNotFoundDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOffer;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.dto.CancellationResultOutput;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentOfferGateway;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancelAppointmentUseCaseTest {

  @Mock private AppointmentGateway appointmentGateway;
  @Mock private AppointmentOfferGateway appointmentOfferGateway;

  private CancelAppointmentUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new CancelAppointmentUseCase(appointmentGateway);
  }

  @Test
  void shouldCancelAppointmentSuccessfully() {
    UUID id = UUID.randomUUID();
    Appointment appointment =
        Appointment.reconstruct(
            id,
            UUID.randomUUID(),
            null,
            LocalDateTime.now().plusHours(1),
            AppointmentStatus.CONFIRMED,
            LocalDateTime.now());
    when(appointmentGateway.findById(id)).thenReturn(Optional.of(appointment));

    AppointmentOutput output = useCase.execute(id);

    assertThat(output.status()).isEqualTo("CANCELLED");
    verify(appointmentGateway).update(appointment);
  }

  @Test
  void shouldThrowNotFoundExceptionForMissingAppointment() {
    UUID id = UUID.randomUUID();
    when(appointmentGateway.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.execute(id))
        .isInstanceOf(AppointmentNotFoundDomainException.class);

    verify(appointmentGateway, never()).update(any());
  }

  @Test
  void shouldCreateOfferForFutureCandidateWhenPatientCannotAttend() {
    UUID id = UUID.randomUUID();
    UUID openedPatientId = UUID.randomUUID();
    UUID candidatePatientId = UUID.randomUUID();
    LocalDateTime openedDateTime = LocalDateTime.now().plusHours(4);
    LocalDateTime candidateDateTime = openedDateTime.plusDays(10);
    Appointment openedAppointment =
        Appointment.reconstruct(
            id,
            openedPatientId,
            null,
            openedDateTime,
            AppointmentStatus.CONFIRMED,
            LocalDateTime.now());
    Appointment candidateAppointment =
        Appointment.reconstruct(
            UUID.randomUUID(),
            candidatePatientId,
            null,
            candidateDateTime,
            AppointmentStatus.CONFIRMED,
            LocalDateTime.now());
    CancelAppointmentUseCase useCaseWithOffers =
        new CancelAppointmentUseCase(appointmentGateway, appointmentOfferGateway);

    when(appointmentGateway.findById(id)).thenReturn(Optional.of(openedAppointment));
    when(appointmentGateway.findNextConfirmedAfter(
            openedDateTime,
            openedAppointment.getAppointmentType(),
            openedAppointment.getServiceName(),
            openedPatientId))
        .thenReturn(Optional.of(candidateAppointment));

    CancellationResultOutput output =
        useCaseWithOffers.executeWithReallocation(id, "Paciente informou indisponibilidade");

    assertThat(output.appointment().status()).isEqualTo("CANCELLED");
    assertThat(output.generatedOffer()).isNotNull();
    assertThat(output.generatedOffer().candidateAppointmentId())
        .isEqualTo(candidateAppointment.getId());
    assertThat(output.generatedOffer().candidatePatientId()).isEqualTo(candidatePatientId);
    assertThat(output.generatedOffer().status()).isEqualTo("PENDING");
    verify(appointmentOfferGateway).save(any(AppointmentOffer.class));
  }
}
