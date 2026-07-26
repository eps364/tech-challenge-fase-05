/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.config;

import br.com.fiap.susconnect.aps.core.domain.ActionStatus;
import br.com.fiap.susconnect.aps.core.domain.PreventiveFocus;
import br.com.fiap.susconnect.aps.core.domain.PreventiveIndicator;
import br.com.fiap.susconnect.aps.core.domain.SearchAction;
import br.com.fiap.susconnect.aps.core.domain.Territory;
import br.com.fiap.susconnect.aps.core.gateway.SearchActionGateway;
import br.com.fiap.susconnect.aps.core.gateway.TerritoryGateway;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoDataConfig {

  public static final UUID JARDIM_ESPERANCA_ID =
      UUID.fromString("10000000-0000-0000-0000-000000000001");
  public static final UUID VILA_NOVA_ID =
      UUID.fromString("10000000-0000-0000-0000-000000000002");
  public static final UUID PARQUE_DAS_FLORES_ID =
      UUID.fromString("10000000-0000-0000-0000-000000000003");
  public static final UUID CENTRO_ID = UUID.fromString("10000000-0000-0000-0000-000000000004");

  @Bean
  @ConditionalOnProperty(prefix = "aps.demo-data", name = "enabled", havingValue = "true")
  public ApplicationRunner loadDemoData(
      TerritoryGateway territoryGateway, SearchActionGateway searchActionGateway, Clock clock) {
    return ignored -> {
      if (!territoryGateway.findAll().isEmpty()) {
        return;
      }
      LocalDate today = LocalDate.now(clock);
      LocalDateTime now = LocalDateTime.now(clock);
      YearMonth competence = YearMonth.now(clock).minusMonths(1);

      territoryGateway.save(
          territory(
              JARDIM_ESPERANCA_ID,
              "T-001",
              "Jardim Esperanca",
              "UBS Jardim Esperanca",
              "42.00",
              competence,
              List.of(
                  indicator(PreventiveFocus.CHRONIC_CONDITIONS, "32.00", "60.00"),
                  indicator(PreventiveFocus.PRENATAL_CARE, "72.00", "85.00"))));
      territoryGateway.save(
          territory(
              VILA_NOVA_ID,
              "T-002",
              "Vila Nova",
              "UBS Vila Nova",
              "78.00",
              competence,
              List.of(
                  indicator(PreventiveFocus.CHRONIC_CONDITIONS, "45.00", "60.00"),
                  indicator(PreventiveFocus.CHILDHOOD_VACCINATION, "94.00", "90.00"))));
      territoryGateway.save(
          territory(
              PARQUE_DAS_FLORES_ID,
              "T-003",
              "Parque das Flores",
              "UBS Parque das Flores",
              "44.00",
              competence,
              List.of(
                  indicator(PreventiveFocus.PRENATAL_CARE, "90.00", "85.00"),
                  indicator(PreventiveFocus.CERVICAL_SCREENING, "71.00", "70.00"))));
      territoryGateway.save(
          territory(
              CENTRO_ID,
              "T-004",
              "Centro",
              "UBS Centro",
              "81.00",
              competence,
              List.of(
                  indicator(PreventiveFocus.CHRONIC_CONDITIONS, "72.00", "60.00"),
                  indicator(PreventiveFocus.CHILDHOOD_VACCINATION, "96.00", "90.00"))));

      searchActionGateway.save(
          action(
              "20000000-0000-0000-0000-000000000001",
              JARDIM_ESPERANCA_ID,
              PreventiveFocus.CHRONIC_CONDITIONS,
              "Reconnect people with chronic conditions to preventive follow-up",
              "ESF Jardim Esperanca",
              today.minusDays(3),
              today.plusDays(4),
              80,
              54,
              ActionStatus.IN_PROGRESS,
              "Aggregated demonstration data. No patient records are stored.",
              "54 contacts completed and 31 people reconnected.",
              now.minusDays(3),
              now.minusHours(2)));
      searchActionGateway.save(
          action(
              "20000000-0000-0000-0000-000000000002",
              VILA_NOVA_ID,
              PreventiveFocus.CHRONIC_CONDITIONS,
              "Organize a chronic care outreach day",
              "ESF Vila Nova",
              today.minusDays(7),
              today.minusDays(1),
              40,
              0,
              ActionStatus.PLANNED,
              null,
              null,
              now.minusDays(7),
              now.minusDays(7)));
      searchActionGateway.save(
          action(
              "20000000-0000-0000-0000-000000000003",
              CENTRO_ID,
              PreventiveFocus.CHILDHOOD_VACCINATION,
              "Update childhood vaccination outreach",
              "ESF Centro",
              today.minusDays(15),
              today.minusDays(5),
              50,
              47,
              ActionStatus.COMPLETED,
              null,
              "47 aggregated contacts were completed.",
              now.minusDays(15),
              now.minusDays(4)));
    };
  }

  private Territory territory(
      UUID id,
      String code,
      String name,
      String unitName,
      String linkage,
      YearMonth competence,
      List<PreventiveIndicator> indicators) {
    return Territory.reconstruct(
        id, code, name, unitName, new BigDecimal(linkage), competence, indicators);
  }

  private PreventiveIndicator indicator(PreventiveFocus focus, String score, String target) {
    return new PreventiveIndicator(focus, new BigDecimal(score), new BigDecimal(target));
  }

  private SearchAction action(
      String id,
      UUID territoryId,
      PreventiveFocus focus,
      String objective,
      String responsibleTeam,
      LocalDate plannedStart,
      LocalDate plannedEnd,
      int targetCount,
      int performedCount,
      ActionStatus status,
      String notes,
      String resultNotes,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    return SearchAction.reconstruct(
        UUID.fromString(id),
        territoryId,
        focus,
        objective,
        responsibleTeam,
        plannedStart,
        plannedEnd,
        targetCount,
        performedCount,
        status,
        notes,
        resultNotes,
        createdAt,
        updatedAt);
  }
}
