/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentOffer;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOfferOutput;
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
class AcceptAppointmentOfferUseCaseTest {

  @Mock private AppointmentGateway appointmentGateway;
  @Mock private AppointmentOfferGateway appointmentOfferGateway;

  private AcceptAppointmentOfferUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new AcceptAppointmentOfferUseCase(appointmentGateway, appointmentOfferGateway);
  }

  @Test
  void shouldAcceptOfferAndMoveCandidateAppointmentToOpenedSlot() {
    LocalDateTime openedDateTime = LocalDateTime.now().plusHours(2);
    LocalDateTime originalCandidateDateTime = openedDateTime.plusDays(8);
    Appointment openedAppointment =
        Appointment.reconstruct(
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            openedDateTime,
            AppointmentStatus.CANCELLED,
            LocalDateTime.now());
    Appointment candidateAppointment =
        Appointment.reconstruct(
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            originalCandidateDateTime,
            AppointmentStatus.CONFIRMED,
            LocalDateTime.now());
    AppointmentOffer offer = AppointmentOffer.create(openedAppointment, candidateAppointment);

    when(appointmentOfferGateway.findById(offer.getId())).thenReturn(Optional.of(offer));
    when(appointmentGateway.existsByDateTimeAndStatus(openedDateTime, AppointmentStatus.CONFIRMED))
        .thenReturn(false);
    when(appointmentGateway.findById(candidateAppointment.getId()))
        .thenReturn(Optional.of(candidateAppointment));

    AppointmentOfferOutput output = useCase.execute(offer.getId());

    assertThat(output.status()).isEqualTo("ACCEPTED");
    assertThat(candidateAppointment.getDateTime()).isEqualTo(openedDateTime);
    assertThat(candidateAppointment.getRescheduledFrom()).isEqualTo(originalCandidateDateTime);
    verify(appointmentGateway).update(candidateAppointment);
    verify(appointmentOfferGateway).update(offer);
  }
}
