/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.repository;

import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentType;
import br.com.fiap.susconnect.appointment.infra.entity.AppointmentJpa;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring Data JPA Repository for Appointment */
@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentJpa, UUID> {

  List<AppointmentJpa> findByPatientId(UUID patientId);

  List<AppointmentJpa> findByStatusAndDateTimeBetween(
      AppointmentStatus status, LocalDateTime start, LocalDateTime end);

  Optional<AppointmentJpa>
      findFirstByStatusAndAppointmentTypeAndServiceNameAndPatientIdNotAndDateTimeAfterOrderByDateTimeAsc(
          AppointmentStatus status,
          AppointmentType appointmentType,
          String serviceName,
          UUID excludedPatientId,
          LocalDateTime dateTime);

  boolean existsByDateTimeAndStatus(LocalDateTime dateTime, AppointmentStatus status);
}
