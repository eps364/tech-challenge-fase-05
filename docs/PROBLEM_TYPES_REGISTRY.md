# Problem Types Registry

## Overview

This document provides a complete reference of all custom problem type URIs defined across the microservices architecture. Each problem type is standardized per [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457.html).

**Base URI**: `https://api.example.com/problems`

---

## Gateway Service

**Namespace**: `/gateway`

### Rate Limiting

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/gateway/rate-limited` |
| **HTTP Status** | 429 Too Many Requests |
| **Title** | Too Many Requests |
| **Description** | Client has exceeded rate limit for this endpoint |
| **Suggested Extensions** | `retryAfter` (seconds), `limit` (requests per window), `window` (time window in seconds) |

### Service Unavailable

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/gateway/service-unavailable` |
| **HTTP Status** | 503 Service Unavailable |
| **Title** | Service Unavailable |
| **Description** | Downstream microservice is unreachable or not responding |
| **Suggested Extensions** | `serviceName` (name of unavailable service), `retryAfter` (seconds) |

### Invalid Request

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/gateway/invalid-request` |
| **HTTP Status** | 400 Bad Request |
| **Title** | Invalid Request |
| **Description** | Request is malformed or violates API contract |
| **Suggested Extensions** | `errors` (array of validation errors) |

---

## Authentication Service

**Namespace**: `/auth`

### Unauthorized

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/auth/unauthorized` |
| **HTTP Status** | 401 Unauthorized |
| **Title** | Unauthorized |
| **Description** | Missing or invalid authentication credentials |
| **When Thrown** | No Authorization header, invalid format, expired token, wrong credentials |
| **Suggested Extensions** | `scheme` (expected auth scheme), `realm` (protection realm) |

### Forbidden

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/auth/forbidden` |
| **HTTP Status** | 403 Forbidden |
| **Title** | Forbidden |
| **Description** | Authenticated user lacks required permissions or roles |
| **When Thrown** | Valid token but insufficient privileges for requested operation |
| **Suggested Extensions** | `requiredRoles` (array), `requiredPermissions` (array) |

### Token Expired

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/auth/token-expired` |
| **HTTP Status** | 401 Unauthorized |
| **Title** | Token Expired |
| **Description** | JWT or session token has expired |
| **When Thrown** | Token's `exp` claim indicates expiration time is in the past |
| **Suggested Extensions** | `expiresAt` (ISO 8601 expiration time), `refreshTokenAvailable` (boolean) |

### Invalid Token

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/auth/invalid-token` |
| **HTTP Status** | 401 Unauthorized |
| **Title** | Invalid Token |
| **Description** | JWT token is malformed, invalid signature, or corrupted |
| **When Thrown** | Token cannot be decoded, signature verification fails, required claims missing |
| **Suggested Extensions** | `tokenType` (e.g., "Bearer"), `issue` (reason token invalid) |

### Validation Error

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/auth/validation-error` |
| **HTTP Status** | 400 Bad Request |
| **Title** | Validation Error |
| **Description** | Invalid login credentials, registration data, or password format |
| **When Thrown** | Password too short, email invalid, username already exists, etc. |
| **Suggested Extensions** | `errors` (array with field names and validation messages) |

---

## Appointment Service

**Namespace**: `/appointments`

### Not Found

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/appointments/not-found` |
| **HTTP Status** | 404 Not Found |
| **Title** | Appointment Not Found |
| **Description** | Requested appointment does not exist in the system |
| **When Thrown** | GET/PUT/DELETE appointment with invalid ID, appointment was deleted |
| **Suggested Extensions** | `appointmentId` (requested ID), `searchCriteria` (if found via search) |

### Conflict

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/appointments/conflict` |
| **HTTP Status** | 409 Conflict |
| **Title** | Appointment Conflict |
| **Description** | Requested time slot is already booked or unavailable |
| **When Thrown** | Creating/updating appointment with conflicting time |
| **Suggested Extensions** | `conflictingAppointmentId` (existing ID), `availableSlots` (array of available times), `requestedTime` (ISO 8601) |

### Invalid Time

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/appointments/invalid-time` |
| **HTTP Status** | 422 Unprocessable Content |
| **Title** | Invalid Appointment Time |
| **Description** | Proposed appointment time is in the past, outside operating hours, or otherwise invalid |
| **When Thrown** | Requested time < current time, outside clinic hours, invalid format |
| **Suggested Extensions** | `proposedTime` (ISO 8601), `validRange` (object with start/end), `minimumAdvanceNotice` (seconds) |

### Validation Error

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/appointments/validation-error` |
| **HTTP Status** | 400 Bad Request |
| **Title** | Validation Error |
| **Description** | Appointment data validation failed |
| **When Thrown** | Missing required fields, invalid types, constraint violations |
| **Suggested Extensions** | `errors` (array with field validation details) |

---

## Triage Service

**Namespace**: `/triage`

### Not Found

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/triage/not-found` |
| **HTTP Status** | 404 Not Found |
| **Title** | Triage Record Not Found |
| **Description** | Requested triage record does not exist |
| **When Thrown** | GET/PUT/DELETE triage with invalid ID, record was deleted |
| **Suggested Extensions** | `triageId` (requested ID), `patientId` (if known) |

### Invalid Risk Level

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/triage/invalid-risk-level` |
| **HTTP Status** | 422 Unprocessable Content |
| **Title** | Invalid Risk Level |
| **Description** | Risk level value is not recognized |
| **When Thrown** | Risk level not in [low, medium, high] |
| **Suggested Extensions** | `providedValue` (invalid value), `validValues` (array of allowed values) |

### Validation Error

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/triage/validation-error` |
| **HTTP Status** | 400 Bad Request |
| **Title** | Validation Error |
| **Description** | Triage data validation failed |
| **When Thrown** | Missing required fields, invalid types, business rule violations |
| **Suggested Extensions** | `errors` (array with field validation details) |

### Duplicate Entry

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/triage/duplicate-entry` |
| **HTTP Status** | 409 Conflict |
| **Title** | Duplicate Triage Entry |
| **Description** | Patient already has an active triage record |
| **When Thrown** | Creating triage for patient with existing active record |
| **Suggested Extensions** | `existingTriageId` (current record), `existingCreatedAt` (ISO 8601), `existingRiskLevel` |

---

## Medical Record Service

**Namespace**: `/medical-records`

### Not Found

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/medical-records/not-found` |
| **HTTP Status** | 404 Not Found |
| **Title** | Medical Record Not Found |
| **Description** | Requested medical record does not exist |
| **When Thrown** | GET/PUT/DELETE record with invalid ID, record was deleted/archived |
| **Suggested Extensions** | `recordId` (requested ID), `patientId` (if known) |

### Access Denied

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/medical-records/access-denied` |
| **HTTP Status** | 403 Forbidden |
| **Title** | Access Denied |
| **Description** | User lacks permission to access this medical record |
| **When Thrown** | Trying to access record without proper authorization |
| **Suggested Extensions** | `recordId`, `requiredRoles` (array), `reason` (e.g., "not physician", "different organization") |

### Validation Error

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/medical-records/validation-error` |
| **HTTP Status** | 400 Bad Request |
| **Title** | Validation Error |
| **Description** | Record data validation failed |
| **When Thrown** | Missing required fields, invalid formats, business rule violations |
| **Suggested Extensions** | `errors` (array with field validation details) |

### Conflict

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/medical-records/conflict` |
| **HTTP Status** | 409 Conflict |
| **Title** | Conflicting Record |
| **Description** | Record conflicts with existing medical data |
| **When Thrown** | Creating/updating record that conflicts with existing entries |
| **Suggested Extensions** | `conflictingRecordId`, `conflictReason`, `suggestedResolution` |

---

## Registry Service

**Namespace**: `/registry`

### Not Found

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/registry/not-found` |
| **HTTP Status** | 404 Not Found |
| **Title** | Registry Entry Not Found |
| **Description** | Requested registry entry does not exist |
| **When Thrown** | GET/PUT/DELETE registry entry with invalid ID |
| **Suggested Extensions** | `entryId` (requested ID), `entryType` (if known) |

### Conflict

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/registry/conflict` |
| **HTTP Status** | 409 Conflict |
| **Title** | Conflicting Entry |
| **Description** | Entry conflicts with existing registry data |
| **When Thrown** | Creating entry with duplicate key/unique constraint violation |
| **Suggested Extensions** | `conflictingEntryId`, `constraintName` (e.g., "unique_email"), `conflictingValue` |

### Validation Error

| Field | Value |
|-------|-------|
| **Type URI** | `https://api.example.com/problems/registry/validation-error` |
| **HTTP Status** | 400 Bad Request |
| **Title** | Validation Error |
| **Description** | Registry data validation failed |
| **When Thrown** | Missing required fields, invalid formats, constraint violations |
| **Suggested Extensions** | `errors` (array with field validation details) |

---

## Client Consumption Guidelines

### Handling Problem Details

1. **Check Content-Type**: Verify response is `application/problem+json`
2. **Extract Type**: Use `type` field to determine error category
3. **Read Title**: For logging and user-facing messages
4. **Read Detail**: For user-specific context and resolution guidance
5. **Preserve Instance**: For support reference and incident tracking
6. **Use Extensions**: Parse domain-specific extensions as needed
7. **Ignore Unknown Fields**: Follow "must-ignore" pattern for forward compatibility

### Retry Logic

- **429 (Rate Limited)**: Use `retryAfter` extension to determine wait time
- **503 (Service Unavailable)**: Use `retryAfter` extension and implement exponential backoff
- **Others**: Generally do not retry without user intervention

### Logging Best Practices

Always log:
- `type` (problem URI)
- `instance` (unique occurrence identifier)
- `traceId` (for trace aggregation)
- `correlationId` (for business transaction tracking)

Example log entry:
```
ERROR: Problem Detail - type=https://api.example.com/problems/triage/invalid-risk-level, instance=/api/triage, traceId=4ba2b033-..., correlationId=req-2024-12-15-001
```

---

## Problem Type Evolution

### Adding New Problem Types

When adding a new problem type:

1. Document in this registry
2. Create corresponding domain exception with `@ProblemType` annotation
3. Add GlobalExceptionHandler mapping
4. Add unit tests for exception → problem detail conversion
5. Update API documentation
6. Do NOT use versions in URI (e.g., avoid `/v1/problems/...`)

### Versioning Strategy

- Problem type URIs are **permanent** and never versioned
- Extensions can be added without changing type URI
- Clients must ignore unknown extensions
- Breaking changes require new type URI with breaking version in documentation

---

## References

- **RFC 9457 Specification**: https://www.rfc-editor.org/rfc/rfc9457.html
- **IANA Problem Types Registry**: https://www.iana.org/assignments/http-problem-types/
- **Implementation Guide**: See [docs/RFC_9457.md](./RFC_9457.md)

---

**Registry Version**: 1.0  
**Last Updated**: December 15, 2024  
**Status**: Active
