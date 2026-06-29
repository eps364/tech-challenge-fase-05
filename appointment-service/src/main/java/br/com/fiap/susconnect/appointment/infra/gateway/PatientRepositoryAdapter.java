/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.gateway;

import br.com.fiap.susconnect.appointment.core.domain.entity.Patient;
import br.com.fiap.susconnect.appointment.core.gateway.PatientGateway;
import br.com.fiap.susconnect.appointment.infra.entity.PatientJpa;
import br.com.fiap.susconnect.appointment.infra.repository.PatientRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PatientRepositoryAdapter implements PatientGateway {

  private final PatientRepository patientRepository;

  public PatientRepositoryAdapter(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  @Override
  public List<Patient> findAll() {
    return patientRepository.findAllByOrderByFullNameAsc().stream().map(this::toDomain).toList();
  }

  private Patient toDomain(PatientJpa jpa) {
    return Patient.reconstruct(jpa.getId(), jpa.getFullName(), jpa.getEmail(), jpa.getPhone());
  }
}
