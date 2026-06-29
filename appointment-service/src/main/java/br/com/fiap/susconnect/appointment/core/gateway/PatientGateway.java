/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.gateway;

import br.com.fiap.susconnect.appointment.core.domain.entity.Patient;
import java.util.List;

public interface PatientGateway {

  List<Patient> findAll();
}
