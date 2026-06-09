/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.gateway;

import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import br.com.fiap.susconnect.appointment.core.domain.entity.AppointmentStatus;
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
        .triageId(a.getTriageId())
        .patientId(a.getPatientId())
        .professionalId(a.getProfessionalId())
        .dateTime(a.getDateTime())
        .status(a.getStatus())
        .createdAt(a.getCreatedAt())
        .build();
  }

  private Appointment toDomain(AppointmentJpa jpa) {
    return Appointment.reconstruct(
        jpa.getId(),
        jpa.getTriageId(),
        jpa.getPatientId(),
        jpa.getProfessionalId(),
        jpa.getDateTime(),
        jpa.getStatus(),
        jpa.getCreatedAt());
  }
}
