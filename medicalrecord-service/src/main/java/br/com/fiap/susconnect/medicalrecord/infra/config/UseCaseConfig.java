/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.config;

import br.com.fiap.susconnect.medicalrecord.core.gateway.MedicalRecordGateway;
import br.com.fiap.susconnect.medicalrecord.core.usecase.CreateMedicalRecordUseCase;
import br.com.fiap.susconnect.medicalrecord.core.usecase.GetMedicalRecordUseCase;
import br.com.fiap.susconnect.medicalrecord.core.usecase.ListMedicalRecordsByPatientUseCase;
import br.com.fiap.susconnect.medicalrecord.core.usecase.UpdateMedicalRecordUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wires core use cases with infrastructure gateways. */
@Configuration
public class UseCaseConfig {

  @Bean
  public CreateMedicalRecordUseCase createMedicalRecordUseCase(MedicalRecordGateway gateway) {
    return new CreateMedicalRecordUseCase(gateway);
  }

  @Bean
  public GetMedicalRecordUseCase getMedicalRecordUseCase(MedicalRecordGateway gateway) {
    return new GetMedicalRecordUseCase(gateway);
  }

  @Bean
  public UpdateMedicalRecordUseCase updateMedicalRecordUseCase(MedicalRecordGateway gateway) {
    return new UpdateMedicalRecordUseCase(gateway);
  }

  @Bean
  public ListMedicalRecordsByPatientUseCase listMedicalRecordsByPatientUseCase(
      MedicalRecordGateway gateway) {
    return new ListMedicalRecordsByPatientUseCase(gateway);
  }
}
