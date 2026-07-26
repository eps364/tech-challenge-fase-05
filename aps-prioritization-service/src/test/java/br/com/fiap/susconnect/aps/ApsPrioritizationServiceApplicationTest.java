/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps;

import org.junit.jupiter.api.Test;

class ApsPrioritizationServiceApplicationTest {

  @Test
  void shouldStartTheApplicationFromMain() {
    ApsPrioritizationServiceApplication.main(
        new String[] {
          "--spring.profiles.active=test",
          "--spring.main.web-application-type=none",
          "--spring.datasource.url=jdbc:h2:mem:aps_main_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
          "--aps.demo-data.enabled=false"
        });
  }
}
