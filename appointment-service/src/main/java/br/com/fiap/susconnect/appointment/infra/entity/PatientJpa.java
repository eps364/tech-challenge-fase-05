/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.appointment.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientJpa {

  @Id private UUID id;

  @Column(name = "full_name", nullable = false, length = 160)
  private String fullName;

  @Column(nullable = false, length = 160)
  private String email;

  @Column(nullable = false, length = 30)
  private String phone;
}
