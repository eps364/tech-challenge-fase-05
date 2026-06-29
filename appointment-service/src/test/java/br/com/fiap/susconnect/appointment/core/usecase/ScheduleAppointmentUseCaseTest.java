/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentConflictDomainException;
import br.com.fiap.susconnect.appointment.core.domain.AppointmentValidationDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduleAppointmentUseCaseTest {

  @Mock private AppointmentGateway appointmentGateway;

  private ScheduleAppointmentUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ScheduleAppointmentUseCase(appointmentGateway);
  }

  @Test
  void shouldScheduleAppointmentSuccessfully() {
    UUID patientId = UUID.randomUUID();
    LocalDateTime future = LocalDateTime.now().plusHours(2);

    when(appointmentGateway.existsByDateTimeAndStatus(eq(future), eq(AppointmentStatus.CONFIRMED)))
        .thenReturn(false);

    AppointmentOutput output = useCase.execute(patientId, future);

    assertThat(output).isNotNull();
    assertThat(output.patientId()).isEqualTo(patientId);
    assertThat(output.status()).isEqualTo("CONFIRMED");
    verify(appointmentGateway).save(any());
  }

  @Test
  void shouldThrowValidationExceptionWhenDateTimeIsInThePast() {
    UUID patientId = UUID.randomUUID();
    LocalDateTime past = LocalDateTime.now().minusHours(1);

    assertThatThrownBy(() -> useCase.execute(patientId, past))
        .isInstanceOf(AppointmentValidationDomainException.class)
        .hasMessageContaining("past");

    verify(appointmentGateway, never()).save(any());
  }

  @Test
  void shouldThrowConflictExceptionWhenSlotAlreadyBooked() {
    UUID patientId = UUID.randomUUID();
    LocalDateTime future = LocalDateTime.now().plusHours(1);

    when(appointmentGateway.existsByDateTimeAndStatus(eq(future), eq(AppointmentStatus.CONFIRMED)))
        .thenReturn(true);

    assertThatThrownBy(() -> useCase.execute(patientId, future))
        .isInstanceOf(AppointmentConflictDomainException.class);

    verify(appointmentGateway, never()).save(any());
  }
}
