/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.repository;

import br.com.fiap.susconnect.appointment.infra.entity.PatientJpa;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientJpa, UUID> {

  List<PatientJpa> findAllByOrderByFullNameAsc();
}
