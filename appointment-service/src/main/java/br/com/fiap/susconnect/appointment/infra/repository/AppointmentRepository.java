/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.repository;

import br.com.fiap.susconnect.appointment.infra.entity.AppointmentJpa;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** Spring Data JPA Repository for Appointment */
@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentJpa, UUID> {

  Optional<AppointmentJpa> findByPatientIdAndDateTimeAfter(UUID patientId, LocalDateTime dateTime);

  @Query(
      "SELECT a FROM AppointmentJpa a WHERE a.dateTime >= ?1 AND a.dateTime < ?2 AND a.status = 'CONFIRMED'")
  List<AppointmentJpa> findAvailableSlots(LocalDateTime start, LocalDateTime end);
}
