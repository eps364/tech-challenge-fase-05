/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.gateway;

import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentType;
import br.com.fiap.susconnect.appointment.core.gateway.AppointmentGateway;
import br.com.fiap.susconnect.appointment.infra.entity.AppointmentJpa;
import br.com.fiap.susconnect.appointment.infra.repository.AppointmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppointmentRepositoryAdapter implements AppointmentGateway {

  private final AppointmentRepository appointmentRepository;

  public AppointmentRepositoryAdapter(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = appointmentRepository;
  }

  @Override
  @Transactional
  public void save(Appointment appointment) {
    appointmentRepository.save(toJpa(appointment));
  }

  @Override
  public Optional<Appointment> findById(UUID id) {
    return appointmentRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Appointment> findByPatientId(UUID patientId) {
    return appointmentRepository.findByPatientId(patientId).stream().map(this::toDomain).toList();
  }

  @Override
  public List<Appointment> findConfirmedBetween(LocalDateTime start, LocalDateTime end) {
    return appointmentRepository
        .findByStatusAndDateTimeBetween(AppointmentStatus.CONFIRMED, start, end)
        .stream()
        .map(this::toDomain)
        .toList();
  }

  @Override
  public Optional<Appointment> findNextConfirmedAfter(
      LocalDateTime dateTime,
      AppointmentType appointmentType,
      String serviceName,
      UUID excludedPatientId) {
    return appointmentRepository
        .findFirstByStatusAndAppointmentTypeAndServiceNameAndPatientIdNotAndDateTimeAfterOrderByDateTimeAsc(
            AppointmentStatus.CONFIRMED,
            appointmentType,
            serviceName,
            excludedPatientId,
            dateTime)
        .map(this::toDomain);
  }

  @Override
  public boolean existsByDateTimeAndStatus(LocalDateTime dateTime, AppointmentStatus status) {
    return appointmentRepository.existsByDateTimeAndStatus(dateTime, status);
  }

  @Override
  @Transactional
  public void update(Appointment appointment) {
    AppointmentJpa jpa = toJpa(appointment);
    jpa.setUpdatedAt(LocalDateTime.now());
    appointmentRepository.save(jpa);
  }

  private AppointmentJpa toJpa(Appointment a) {
    return AppointmentJpa.builder()
        .id(a.getId())
        .patientId(a.getPatientId())
        .professionalId(a.getProfessionalId())
        .dateTime(a.getDateTime())
        .status(a.getStatus())
        .appointmentType(a.getAppointmentType())
        .serviceName(a.getServiceName())
        .facilityName(a.getFacilityName())
        .preparationNotes(a.getPreparationNotes())
        .patientNotification(a.getPatientNotification())
        .lastNotifiedAt(a.getLastNotifiedAt())
        .rescheduledFrom(a.getRescheduledFrom())
        .cancellationReason(a.getCancellationReason())
        .createdAt(a.getCreatedAt())
        .build();
  }

  private Appointment toDomain(AppointmentJpa jpa) {
    return Appointment.reconstruct(
        jpa.getId(),
        jpa.getPatientId(),
        jpa.getProfessionalId(),
        jpa.getDateTime(),
        jpa.getStatus(),
        jpa.getAppointmentType(),
        jpa.getServiceName(),
        jpa.getFacilityName(),
        jpa.getPreparationNotes(),
        jpa.getPatientNotification(),
        jpa.getLastNotifiedAt(),
        jpa.getRescheduledFrom(),
        jpa.getCancellationReason(),
        jpa.getCreatedAt());
  }
}
