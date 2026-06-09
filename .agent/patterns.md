# Padrões de Código — SUS-Connect Triagem

Exemplos canônicos a seguir em toda implementação. Copie e adapte o nome do domínio.

---

## Padrão 1 — Entidade JPA com Auditoria (Infra Layer)

```java
package br.com.fiap.susconnect.triage.infra.entity;

import br.com.fiap.susconnect.triage.core.domain.entity.RiskLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "triage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriageJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID patientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RiskLevel riskLevel;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

**Nota**: Entidades JPA devem estar em `infra/entity/` com sufixo `Jpa`. O pacote `core/entity/` contém apenas entidades de domínio (Java puro, sem anotações).

---

## Padrão 2 — DTO com Validações

```java
package br.com.fiap.susconnect.triagem.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriagemRequest {

    @NotBlank(message = "Paciente ID é obrigatório")
    @Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres")
    private String pacienteId;

    @NotNull(message = "Unidade de saúde é obrigatória")
    @Positive(message = "ID da unidade deve ser positivo")
    private Long unidadeSaudeId;

    @NotNull(message = "Sintoma principal é obrigatório")
    private String sintomaPrincipal;
}
```

```java
package br.com.fiap.susconnect.triagem.dto;

import br.com.fiap.susconnect.triagem.model.Cor;
import br.com.fiap.susconnect.triagem.model.StatusTriagem;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TriagemResponse {

    private Long id;
    private String pacienteId;
    private Long unidadeSaudeId;
    private Cor corClassificacao;
    private Integer tempoMaximoEsperaMinutos;
    private StatusTriagem status;
    private LocalDateTime dataCriacao;
}
```

---

## Padrão 3 — Repository com Queries Customizadas

```java
package br.com.fiap.susconnect.triagem.repository;

import br.com.fiap.susconnect.triagem.model.StatusTriagem;
import br.com.fiap.susconnect.triagem.model.Triagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TriagemRepository extends JpaRepository<Triagem, Long> {

    Optional<Triagem> findByIdAndPacienteId(Long id, String pacienteId);

    List<Triagem> findByPacienteIdOrderByDataCriacaoDesc(String pacienteId);

    Page<Triagem> findByStatus(StatusTriagem status, Pageable pageable);

    @Query("""
        SELECT t FROM Triagem t
        WHERE t.unidadeSaudeId = :unidadeId
          AND t.dataCriacao BETWEEN :inicio AND :fim
          AND (:status IS NULL OR t.status = :status)
        ORDER BY t.dataCriacao DESC
    """)
    Page<Triagem> findByUnidadeAndPeriodo(
        @Param("unidadeId") Long unidadeId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("status") StatusTriagem status,
        Pageable pageable
    );
}
```

---

## Padrão 4 — Service com Lógica de Negócio

```java
package br.com.fiap.susconnect.triagem.service;

import br.com.fiap.susconnect.triagem.dto.TriagemRequest;
import br.com.fiap.susconnect.triagem.dto.TriagemResponse;
import br.com.fiap.susconnect.triagem.exception.TriagemNaoEncontradaException;
import br.com.fiap.susconnect.triagem.model.Triagem;
import br.com.fiap.susconnect.triagem.repository.TriagemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TriagemService {

    private final TriagemRepository triagemRepository;

    @Transactional
    public TriagemResponse iniciarTriagem(TriagemRequest request) {
        log.info("Iniciando triagem para paciente: {}", request.getPacienteId());

        Triagem triagem = new Triagem();
        triagem.setPacienteId(request.getPacienteId());
        triagem.setUnidadeSaudeId(request.getUnidadeSaudeId());

        Triagem saved = triagemRepository.save(triagem);
        log.info("Triagem iniciada com ID: {}", saved.getId());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TriagemResponse buscarPorId(Long id) {
        return triagemRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new TriagemNaoEncontradaException(id));
    }

    private TriagemResponse toResponse(Triagem triagem) {
        return TriagemResponse.builder()
            .id(triagem.getId())
            .pacienteId(triagem.getPacienteId())
            .unidadeSaudeId(triagem.getUnidadeSaudeId())
            .corClassificacao(triagem.getCorClassificacao())
            .status(triagem.getStatus())
            .dataCriacao(triagem.getDataCriacao())
            .build();
    }
}
```

---

## Padrão 5 — REST Controller com OpenAPI

```java
package br.com.fiap.susconnect.triagem.controller;

import br.com.fiap.susconnect.triagem.dto.TriagemRequest;
import br.com.fiap.susconnect.triagem.dto.TriagemResponse;
import br.com.fiap.susconnect.triagem.service.TriagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/triagens")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Triagem", description = "Endpoints de gerenciamento de triagem clínica")
public class TriagemController {

    private final TriagemService triagemService;

    @PostMapping
    @Operation(
        summary = "Iniciar triagem",
        description = "Cria uma nova triagem para um paciente em uma unidade de saúde"
    )
    @ApiResponse(responseCode = "201", description = "Triagem iniciada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    public ResponseEntity<TriagemResponse> iniciar(@Valid @RequestBody TriagemRequest request) {
        log.info("POST /api/v1/triagens - paciente: {}", request.getPacienteId());
        TriagemResponse response = triagemService.iniciarTriagem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar triagem por ID")
    @ApiResponse(responseCode = "200", description = "Triagem encontrada")
    @ApiResponse(responseCode = "404", description = "Triagem não encontrada")
    public ResponseEntity<TriagemResponse> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/triagens/{}", id);
        return ResponseEntity.ok(triagemService.buscarPorId(id));
    }
}
```

---

## Padrão 6 — Exceção de Domínio

```java
package br.com.fiap.susconnect.triagem.exception;

import org.springframework.http.HttpStatus;

// Exemplo de nova exceção específica de negócio
public class ClassificacaoInvalidaException extends TriagemException {

    public ClassificacaoInvalidaException(String motivo) {
        super(
            "Classificação inválida: " + motivo,
            HttpStatus.UNPROCESSABLE_ENTITY,
            "CLASSIFICACAO_INVALIDA"
        );
    }
}
```

---

## Padrão 7 — Teste Unitário com Mockito

```java
package br.com.fiap.susconnect.triagem.service;

import br.com.fiap.susconnect.triagem.dto.TriagemRequest;
import br.com.fiap.susconnect.triagem.dto.TriagemResponse;
import br.com.fiap.susconnect.triagem.exception.TriagemNaoEncontradaException;
import br.com.fiap.susconnect.triagem.model.Triagem;
import br.com.fiap.susconnect.triagem.repository.TriagemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriagemServiceTest {

    @Mock
    private TriagemRepository triagemRepository;

    @InjectMocks
    private TriagemService triagemService;

    @Test
    void iniciarTriagem_deveSalvarERetornarResponse() {
        // Given
        TriagemRequest request = TriagemRequest.builder()
            .pacienteId("12345678901")
            .unidadeSaudeId(1L)
            .sintomaPrincipal("Dor de cabeça")
            .build();

        Triagem savedTriagem = new Triagem();
        savedTriagem.setId(1L);
        savedTriagem.setPacienteId("12345678901");

        when(triagemRepository.save(any(Triagem.class))).thenReturn(savedTriagem);

        // When
        TriagemResponse response = triagemService.iniciarTriagem(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getPacienteId()).isEqualTo("12345678901");
        verify(triagemRepository, times(1)).save(any(Triagem.class));
    }

    @Test
    void buscarPorId_quandoNaoEncontrado_deveLancarExcecao() {
        // Given
        when(triagemRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> triagemService.buscarPorId(99L))
            .isInstanceOf(TriagemNaoEncontradaException.class)
            .hasMessageContaining("99");
    }
}
```

---

## Padrão 8 — Teste de Integração com TestContainers

```java
package br.com.fiap.susconnect.triagem.controller;

import br.com.fiap.susconnect.triagem.dto.TriagemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class TriagemControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("triagem_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postTriagem_comDadosValidos_deveRetornar201() throws Exception {
        TriagemRequest request = TriagemRequest.builder()
            .pacienteId("12345678901")
            .unidadeSaudeId(1L)
            .sintomaPrincipal("Dor de cabeça intensa")
            .build();

        mockMvc.perform(post("/api/v1/triagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.pacienteId").value("12345678901"));
    }

    @Test
    void postTriagem_semPacienteId_deveRetornar400() throws Exception {
        TriagemRequest request = TriagemRequest.builder()
            .unidadeSaudeId(1L)
            .build();

        mockMvc.perform(post("/api/v1/triagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
```
