/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.appointment.core.domain.AppointmentNotFoundDomainException;
import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAppointmentUseCaseTest {

  @Mock private AppointmentGateway appointmentGateway;

  private GetAppointmentUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new GetAppointmentUseCase(appointmentGateway);
  }

  @Test
  void shouldReturnAppointmentWhenFound() {
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

    assertThat(output.id()).isEqualTo(id);
    assertThat(output.status()).isEqualTo("CONFIRMED");
  }

  @Test
  void shouldThrowNotFoundExceptionWhenAppointmentDoesNotExist() {
    UUID id = UUID.randomUUID();
    when(appointmentGateway.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.execute(id))
        .isInstanceOf(AppointmentNotFoundDomainException.class);
  }
}
