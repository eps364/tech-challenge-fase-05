/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.fiap.susconnect.aps.infra.config.DemoDataConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApsPrioritizationFlowIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldExecuteTheTerritorialActiveOutreachFlow() throws Exception {
    LocalDate today = LocalDate.now();
    String territoryId = DemoDataConfig.JARDIM_ESPERANCA_ID.toString();

    mockMvc
        .perform(get("/api/v1/dashboard"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.highPriorityTerritoryCount").value(1))
        .andExpect(jsonPath("$.topPriorities[0].id").value(territoryId));

    mockMvc
        .perform(get("/api/v1/territories").param("priority", "HIGH"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(territoryId))
        .andExpect(jsonPath("$[0].attentionFocus").value("CHRONIC_CONDITIONS"));

    mockMvc
        .perform(get("/api/v1/territories/{territoryId}", territoryId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.priority.level").value("HIGH"))
        .andExpect(jsonPath("$.priority.reasons").isArray());

    String createActionBody =
        """
        {
          "focus": "CHRONIC_CONDITIONS",
          "objective": "Reconnect people with chronic conditions to preventive follow-up",
          "responsibleTeam": "ESF Jardim Esperanca",
          "plannedStart": "%s",
          "plannedEnd": "%s",
          "targetCount": 80,
          "notes": "Demonstration action with aggregate counts only"
        }
        """.formatted(today, today.plusDays(7));

    MvcResult createdActionResult =
        mockMvc
            .perform(
                post("/api/v1/territories/{territoryId}/actions", territoryId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createActionBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("PLANNED"))
            .andExpect(jsonPath("$.performedCount").value(0))
            .andReturn();
    UUID actionId =
        UUID.fromString(
            objectMapper.readTree(createdActionResult.getResponse().getContentAsString()).path("id").asText());

    mockMvc
        .perform(
            patch("/api/v1/actions/{actionId}/progress", actionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"IN_PROGRESS\",\"performedCount\":54,\"resultNotes\":\"54 contacts made\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
        .andExpect(jsonPath("$.progressPercent").value(67.5));

    mockMvc
        .perform(
            patch("/api/v1/actions/{actionId}/progress", "20000000-0000-0000-0000-000000000002")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"COMPLETED\",\"performedCount\":0}"))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.status").value(422));

    mockMvc
        .perform(get("/api/v1/territories/{territoryId}", UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404));

    String createTerritoryBody =
        """
        {
          "code": "T-TEST",
          "name": "Territorio Teste",
          "unitName": "UBS Teste",
          "linkedPopulationPercent": 65.00,
          "dataCompetence": "2026-06",
          "indicators": [
            {"focus": "PRENATAL_CARE", "score": 88.00, "target": 85.00}
          ]
        }
        """;
    MvcResult createdTerritoryResult =
        mockMvc
            .perform(
                post("/api/v1/territories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createTerritoryBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.priority.level").value("LOW"))
            .andReturn();
    JsonNode createdTerritory =
        objectMapper.readTree(createdTerritoryResult.getResponse().getContentAsString());
    String createdTerritoryId = createdTerritory.path("id").asText();

    mockMvc
        .perform(
            put("/api/v1/territories/{territoryId}/indicators", createdTerritoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "linkedPopulationPercent": 40.00,
                      "dataCompetence": "2026-07",
                      "indicators": [
                        {"focus": "PRENATAL_CARE", "score": 50.00, "target": 85.00}
                      ]
                    }
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.priority.level").value("HIGH"));
  }
}
