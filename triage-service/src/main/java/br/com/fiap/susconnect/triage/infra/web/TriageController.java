package br.com.fiap.susconnect.triage.infra.web;

import br.com.fiap.susconnect.triage.core.usecase.CreateTriageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Triage REST Controller */
@Slf4j
@RestController
@RequestMapping("/api/v1/triage")
@Tag(name = "Triage", description = "Clinical Triage Endpoints")
public class TriageController {

  private final CreateTriageUseCase createTriageUseCase;

  public TriageController(CreateTriageUseCase createTriageUseCase) {
    this.createTriageUseCase = createTriageUseCase;
  }

  @PostMapping
  @Operation(summary = "Create new triage")
  public ResponseEntity<Void> create() {
    log.info("Creating new triage");
    var triage = createTriageUseCase.execute(UUID.randomUUID());
    log.info("Triage created: {}", triage.getId());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get triage by ID")
  public ResponseEntity<String> find(@PathVariable UUID id) {
    log.info("Fetching triage: {}", id);
    return ResponseEntity.ok("OK");
  }
}
