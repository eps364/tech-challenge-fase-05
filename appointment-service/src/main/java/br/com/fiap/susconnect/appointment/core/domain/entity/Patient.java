/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.core.domain.entity;

import java.util.UUID;

/** Local patient view used by the appointment-focused scope. */
public class Patient {

  private UUID id;
  private String fullName;
  private String email;
  private String phone;

  private Patient() {}

  public static Patient reconstruct(UUID id, String fullName, String email, String phone) {
    var patient = new Patient();
    patient.id = id;
    patient.fullName = fullName;
    patient.email = email;
    patient.phone = phone;
    return patient;
  }

  public UUID getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }
}
