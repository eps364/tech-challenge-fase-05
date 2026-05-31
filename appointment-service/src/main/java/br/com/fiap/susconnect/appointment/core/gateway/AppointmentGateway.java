package br.com.fiap.susconnect.appointment.core.gateway;

import br.com.fiap.susconnect.appointment.core.domain.entity.Appointment;
import java.util.Optional;
import java.util.UUID;

/**
 * Appointment Gateway Port - Interface defining persistence contract
 * Must be implemented in infra layer (adapter)
 */
public interface AppointmentGateway {

  void save(Appointment appointment);

  Optional<Appointment> findById(UUID id);
}
