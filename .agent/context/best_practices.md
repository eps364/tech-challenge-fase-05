# Code Best Practices & Design Patterns

## Architecture

### Clean Architecture Layers

**Domain Layer (Core)**
```java
// Pure Java entities with business logic
public class Triage {
  private UUID id;
  private UUID patientId;
  private String riskLevel;
  
  public static Triage create(UUID patientId) {
    return new Triage(UUID.randomUUID(), patientId, "BLUE");
  }
  
  public void classify(String symptoms) {
    this.riskLevel = calculateRiskLevel(symptoms);
  }
}
```

**Gateway Interfaces (Ports)**
```java
// Define contracts, no implementation
public interface TriageGateway {
  void save(Triage triage);
  Optional<Triage> findById(UUID id);
  Optional<Triage> findByPatientId(UUID patientId);
}
```

**Use Cases (Application)**
```java
// Orchestrate business logic
public class CreateTriageUseCase {
  private final TriageGateway triageGateway;
  
  public Triage execute(UUID patientId) {
    Triage triage = Triage.create(patientId);
    triageGateway.save(triage);
    return triage;
  }
}
```

**Infrastructure Layer**
```java
// Spring-specific implementations
@Repository
public class TriageRepositoryAdapter implements TriageGateway {
  private final TriageRepository repository;
  
  @Override
  public void save(Triage triage) {
    repository.save(new TriageJpa(triage));
  }
}
```

---

## Naming Conventions

### Classes
- **Entity**: Noun (e.g., `Triage`, `Appointment`, `Patient`)
- **Service**: Noun + "Service" (e.g., `TriageService`)
- **Repository**: Entity + "Repository" (e.g., `TriageRepository`)
- **Adapter**: Descriptive + "Adapter" (e.g., `TriageRepositoryAdapter`)
- **UseCase**: Verb + Entity + "UseCase" (e.g., `CreateTriageUseCase`)
- **Controller**: Entity + "Controller" (e.g., `TriageController`)
- **DTO**: Entity + "Output"/"Input" (e.g., `TriageOutput`)

### Methods
- **Getters**: `get` prefix (e.g., `getId()`, `getRiskLevel()`)
- **Setters**: `set` prefix (e.g., `setRiskLevel()`)
- **Builders**: `build` verb (e.g., `buildTriage()`)
- **Factories**: `create`/`from` prefix (e.g., `createFromJpa()`)
- **Conversions**: `to` prefix (e.g., `toJpa()`, `toOutput()`)

### Variables
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RISK_LEVEL`)
- **Fields**: camelCase (e.g., `riskLevel`, `patientId`)
- **Parameters**: camelCase (e.g., `patientId`)

---

## Error Handling

### Exception Hierarchy
```java
public abstract class DomainException extends RuntimeException {}
public class TriageNotFoundException extends DomainException {}
public class InvalidRiskLevelException extends DomainException {}
```

### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(TriageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(TriageNotFoundException e) {
    return ResponseEntity.status(NOT_FOUND)
      .body(new ErrorResponse(e.getMessage(), NOT_FOUND.value()));
  }
  
  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> handleDomain(DomainException e) {
    return ResponseEntity.status(BAD_REQUEST)
      .body(new ErrorResponse(e.getMessage(), BAD_REQUEST.value()));
  }
}
```

---

## Testing Strategy

### Unit Tests (Domain Layer)
```java
@DisplayName("Triage Creation")
class TriageTest {
  
  @Test
  void shouldCreateTriageWithBlueRisk() {
    UUID patientId = UUID.randomUUID();
    Triage triage = Triage.create(patientId);
    
    assertThat(triage.getPatientId()).isEqualTo(patientId);
    assertThat(triage.getRiskLevel()).isEqualTo("BLUE");
  }
}
```

### Integration Tests (Infrastructure)
```java
@SpringBootTest
class TriageRepositoryAdapterTest {
  
  @Autowired
  private TriageRepositoryAdapter adapter;
  
  @Test
  void shouldPersistAndRetrieveTriage() {
    // Arrange
    Triage triage = Triage.create(UUID.randomUUID());
    
    // Act
    adapter.save(triage);
    Optional<Triage> retrieved = adapter.findById(triage.getId());
    
    // Assert
    assertThat(retrieved).isPresent();
    assertThat(retrieved.get().getId()).isEqualTo(triage.getId());
  }
}
```

---

## Code Style

### Google Java Format Rules
- **Line Length**: Maximum 100 characters
- **Indentation**: 2 spaces (not tabs)
- **Imports**: Alphabetically sorted, static imports last
- **Blank Lines**: Around class/method definitions

### Imports
```java
// Order: java → javax → org → com
import java.util.UUID;
import java.util.Optional;
import javax.persistence.Entity;
import org.springframework.stereotype.Component;
import com.example.domain.Triage;
```

---

## Spring Boot Configuration

### Application Properties
```yaml
# application.yml
spring:
  application:
    name: triage-service
  datasource:
    url: jdbc:postgresql://localhost:5432/triage_db
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

### Configuration Classes
```java
@Configuration
public class TriageServiceConfig {
  
  @Bean
  public CreateTriageUseCase createTriageUseCase(TriageGateway gateway) {
    return new CreateTriageUseCase(gateway);
  }
}
```

---

## Security Best Practices

### JWT Validation
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = extractToken(request);
    if (token != null && validateToken(token)) {
      SecurityContextHolder.setContext(buildContext(token));
    }
    filterChain.doFilter(request, response);
  }
}
```

### Authorization
```java
@PreAuthorize("hasRole('ROLE_ADMIN')")
@PostMapping
public ResponseEntity<TriageOutput> create(@RequestBody TriageInput input) {
  // Only ADMIN role can access
}
```

---

## Database Best Practices

### JPA Entity Mapping
```java
@Entity
@Table(name = "triage")
public class TriageJpa {
  
  @Id
  private UUID id;
  
  @Column(name = "patient_id", nullable = false)
  private UUID patientId;
  
  @Column(name = "risk_level", length = 10)
  private String riskLevel;
  
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;
}
```

### Query Methods
```java
public interface TriageRepository extends JpaRepository<TriageJpa, UUID> {
  Optional<TriageJpa> findByPatientId(UUID patientId);
  
  @Query("SELECT t FROM TriageJpa t WHERE t.riskLevel = :riskLevel")
  List<TriageJpa> findByRiskLevel(@Param("riskLevel") String riskLevel);
}
```

---

## Kafka Event Handling

### Producer
```java
@Component
public class TriageEventProducer {
  
  @Autowired
  private KafkaTemplate<String, TriageEvent> kafkaTemplate;
  
  public void publish(TriageEvent event) {
    kafkaTemplate.send("triage.risk-classification", event);
  }
}
```

### Consumer
```java
@Component
public class TriageEventConsumer {
  
  @KafkaListener(topics = "triage.risk-classification", groupId = "appointment-service")
  public void handle(TriageEvent event) {
    // Process event
  }
}
```

---

## Logging Best Practices

### SLF4J Usage
```java
@Slf4j
public class TriageService {
  
  public Triage create(UUID patientId) {
    log.debug("Creating triage for patient: {}", patientId);
    
    Triage triage = Triage.create(patientId);
    triageGateway.save(triage);
    
    log.info("Triage created successfully: {} for patient: {}", triage.getId(), patientId);
    return triage;
  }
}
```

### Log Levels
- **DEBUG**: Detailed diagnostic information
- **INFO**: Confirmation that things are working as expected
- **WARN**: Something unexpected happened
- **ERROR**: Serious error preventing operation

---

## Performance Guidelines

### Database Optimization
- Use pagination for large result sets
- Implement database indexes on frequently queried columns
- Use lazy loading for related entities
- Monitor slow queries in production

### Caching Strategy
- Cache read-heavy data (risk levels, professional info)
- Use TTL for cache entries (5-15 minutes)
- Invalidate cache on data changes

### Connection Pooling
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
```

---

## Documentation Standards

### JavaDoc
```java
/**
 * Creates a new triage assessment for a patient.
 *
 * @param patientId the unique identifier of the patient
 * @return the newly created Triage instance
 * @throws IllegalArgumentException if patientId is null
 */
public Triage create(UUID patientId) {
  // implementation
}
```

### README Format
- Overview and quick start
- Architecture diagram
- Module descriptions
- Database schema
- API endpoints with examples
- Deployment instructions
- Troubleshooting guide

---

## Version Control

### Commit Messages
Follow Conventional Commits:
```
feat(triage): implement manchester protocol
fix(appointment): correct slot validation
test(triage): add unit tests
docs(readme): update installation steps
refactor(core): simplify risk calculation
```

### Branch Strategy
- `main`: Production-ready code
- `develop`: Integration branch
- `feature/*`: Feature branches
- `bugfix/*`: Bug fix branches
- `hotfix/*`: Emergency production fixes

---

## Team Conventions

### Code Review Checklist
- [ ] Code follows Google Java Format
- [ ] All tests pass (unit and integration)
- [ ] No code duplication
- [ ] JavaDoc for public methods
- [ ] Exception handling for edge cases
- [ ] Security vulnerabilities checked
- [ ] Performance impact assessed
- [ ] Database migrations included if needed

### Pull Request Template
```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
How was this tested?

## Checklist
- [ ] Code formatted
- [ ] Tests pass
- [ ] Documentation updated
- [ ] No security issues
```

---

**Version**: 1.0.0  
**Last Updated**: May 31, 2024
