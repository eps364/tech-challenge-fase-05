/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.gateway;

import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Appointment Gateway Port - Interface defining persistence contract Must be implemented in infra
 * layer (adapter)
 */
public interface AppointmentGateway {

  void save(Appointment appointment);

  Optional<Appointment> findById(UUID id);

  List<Appointment> findByPatientId(UUID patientId);

  boolean existsByDateTimeAndStatus(LocalDateTime dateTime, AppointmentStatus status);

  void update(Appointment appointment);
}
