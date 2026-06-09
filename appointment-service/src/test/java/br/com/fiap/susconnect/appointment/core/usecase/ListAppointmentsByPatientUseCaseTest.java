/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.dto.AppointmentOutput;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListAppointmentsByPatientUseCaseTest {

  @Mock private AppointmentGateway appointmentGateway;

  private ListAppointmentsByPatientUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ListAppointmentsByPatientUseCase(appointmentGateway);
  }

  @Test
  void shouldReturnAppointmentsForPatient() {
    UUID patientId = UUID.randomUUID();
    UUID triageId = UUID.randomUUID();
    UUID professionalId = UUID.randomUUID();
    LocalDateTime dateTime = LocalDateTime.now().plusHours(1);
    LocalDateTime createdAt = LocalDateTime.now();

    Appointment appointment =
        Appointment.reconstruct(
            UUID.randomUUID(),
            triageId,
            patientId,
            professionalId,
            dateTime,
            AppointmentStatus.CONFIRMED,
            createdAt);

    when(appointmentGateway.findByPatientId(patientId)).thenReturn(List.of(appointment));

    List<AppointmentOutput> result = useCase.execute(patientId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).patientId()).isEqualTo(patientId);
    assertThat(result.get(0).triageId()).isEqualTo(triageId);
    assertThat(result.get(0).status()).isEqualTo("CONFIRMED");
  }

  @Test
  void shouldReturnEmptyListWhenNoAppointments() {
    UUID patientId = UUID.randomUUID();
    when(appointmentGateway.findByPatientId(patientId)).thenReturn(List.of());

    List<AppointmentOutput> result = useCase.execute(patientId);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldReturnMultipleAppointments() {
    UUID patientId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    Appointment a1 =
        Appointment.reconstruct(
            UUID.randomUUID(),
            UUID.randomUUID(),
            patientId,
            null,
            now.plusHours(1),
            AppointmentStatus.CONFIRMED,
            now);
    Appointment a2 =
        Appointment.reconstruct(
            UUID.randomUUID(),
            UUID.randomUUID(),
            patientId,
            null,
            now.plusHours(2),
            AppointmentStatus.COMPLETED,
            now);

    when(appointmentGateway.findByPatientId(patientId)).thenReturn(List.of(a1, a2));

    List<AppointmentOutput> result = useCase.execute(patientId);

    assertThat(result).hasSize(2);
    assertThat(result).extracting(AppointmentOutput::patientId).containsOnly(patientId);
  }
}
