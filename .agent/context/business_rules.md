# Business Rules - Manchester Protocol v3.0

## Clinical Triage Classification System

### Overview
The Manchester Protocol is a **5-tier risk classification system** used to prioritize patients in emergency care. The system evaluates presenting symptoms and vital signs to assign a risk level and recommended consultation timeframe.

### Risk Levels and Service Times

| Level | Color | Priority | Service Time | Example |
|-------|-------|----------|--------------|---------|
| 1 | RED | Immediate | < 10 minutes | Cardiac arrest, severe trauma |
| 2 | ORANGE | Very Urgent | 10-60 minutes | Acute chest pain, severe dehydration |
| 3 | YELLOW | Urgent | 1-3 hours | Moderate pain, fever 38°C+ |
| 4 | GREEN | Normal | 3-24 hours | Minor injuries, mild symptoms |
| 5 | BLUE | Non-urgent | 24+ hours | Follow-up, low-risk complaints |

---

## Triage Process Flow

### Phase 1: Initial Assessment
```
Patient Arrival
    ↓
Vital Signs Check (BP, HR, SpO2, Temp, Pain)
    ↓
Chief Complaint Recording
    ↓
Symptom Evaluation
```

### Phase 2: Decision Tree
```
Does patient meet RED criteria?
├─ YES → RED (Immediate)
└─ NO ↓
   Does patient meet ORANGE criteria?
   ├─ YES → ORANGE (Very Urgent)
   └─ NO ↓
      Does patient meet YELLOW criteria?
      ├─ YES → YELLOW (Urgent)
      └─ NO ↓
         Does patient meet GREEN criteria?
         ├─ YES → GREEN (Normal)
         └─ NO → BLUE (Non-urgent)
```

### Phase 3: Appointment Scheduling
```
Risk Level Assigned
    ↓
Service Time Window Calculated
    ↓
Professional Availability Check
    ↓
Appointment Slot Created
    ↓
Patient Notification
```

---

## Triage Criteria

### RED (Immediate - < 10 minutes)

**Vital Signs Red Flags**:
- Systolic BP < 90 mmHg or > 220 mmHg
- Heart Rate < 40 or > 130 bpm
- Respiratory Rate < 8 or > 30
- SpO2 < 90%
- Glasgow Coma Scale < 9

**Presenting Problems**:
- Loss of consciousness
- Acute shortness of breath
- Acute severe chest pain
- Severe hemorrhage
- Severe trauma
- Poisoning/Overdose
- Acute allergic reaction

---

### ORANGE (Very Urgent - 10-60 minutes)

**Vital Signs Abnormalities**:
- Systolic BP 90-99 mmHg or 200-219 mmHg
- Heart Rate 40-50 or 120-129 bpm
- Respiratory Rate 8-9 or 28-29
- SpO2 90-94%
- Fever > 39°C

**Presenting Problems**:
- Acute chest pain (stable vitals)
- Acute severe abdominal pain
- Acute severe headache
- Signs of stroke/TIA
- Acute severe asthma
- Severe allergic reaction (stable)
- Acute severe psychiatric symptoms

---

### YELLOW (Urgent - 1-3 hours)

**Vital Signs**:
- Moderate abnormalities
- Fever 38.0-39.0°C
- Moderate tachycardia

**Presenting Problems**:
- Moderate chest pain
- Moderate abdominal pain
- Moderate headache
- Moderately severe asthma
- Mild hemorrhage
- Minor trauma with significant pain
- Moderate psychiatric symptoms

---

### GREEN (Normal Priority - 3-24 hours)

**Vital Signs**: All normal or minimal abnormalities

**Presenting Problems**:
- Mild pain
- Minor injuries
- Common cold symptoms
- Minor skin conditions
- Minor musculoskeletal complaints
- Follow-up visits

---

### BLUE (Non-urgent - 24+ hours)

**Characteristics**:
- No acute symptoms
- All vital signs normal
- Administrative visits
- Vaccination
- Prescription refills
- Health education

---

## System Business Rules

### Appointment Scheduling Rules

1. **Immediate Confirmation** (RED)
   - Direct admission to emergency
   - No appointment slot needed
   - Immediate professional assignment

2. **Priority Slot Allocation** (ORANGE)
   - Guaranteed slot within 1 hour
   - Available professionals queried
   - Confirmation sent to patient

3. **Regular Scheduling** (YELLOW)
   - Slot within 1-3 hours
   - Professional availability considered
   - Confirmation email/SMS sent

4. **Standard Scheduling** (GREEN)
   - Flexible scheduling
   - Available in next 24 hours
   - Can be scheduled for future date

5. **Non-urgent Booking** (BLUE)
   - Can be scheduled for future date
   - Confirmation on next business day
   - No priority

### Professional Assignment Rules

1. **Specialty Matching**
   - Cardiologist for chest pain
   - Orthopedist for musculoskeletal
   - Neurologist for neurological complaints
   - General practitioner as fallback

2. **Availability Validation**
   - Check professional schedule
   - Validate location proximity
   - Confirm medical clearance

3. **Load Balancing**
   - Distribute appointments fairly
   - Prevent single professional overload
   - Consider patient preferences

---

## Data Validation Rules

### Patient Data
- **ID**: Valid UUID format
- **Age**: 0-150 years
- **Vital Signs Range**:
  - BP: 40/20 - 250/150 mmHg
  - HR: 0-300 bpm
  - RR: 0-100 breaths/min
  - Temp: 35-43°C
  - SpO2: 0-100%

### Appointment Data
- **Date**: Future date/time only
- **Duration**: 15-120 minutes
- **Professional**: Assigned and available
- **Status**: Valid enum value

### Medical Record Data
- **Diagnosis**: Non-empty, max 1000 chars
- **Prescription**: Valid medication format
- **Notes**: Non-empty, max 2000 chars

---

## Concurrent Access Rules

### Appointment Conflicts
- **Prevention**: Database unique constraint on (professional_id, date_time, duration)
- **Resolution**: First-come-first-served
- **Notification**: Automatic rescheduling offer

### Patient Multiple Triages
- **Rule**: One active triage per patient
- **Override**: Manual cancellation required
- **History**: All triages retained for audit

---

## System Performance Rules

### Timeframe Compliance
- **RED**: Must be seen within 10 minutes (measured from triage completion)
- **ORANGE**: Must be seen within 60 minutes
- **YELLOW**: Must be seen within 180 minutes (3 hours)
- **GREEN**: Must be seen within 1440 minutes (24 hours)
- **BLUE**: Can be seen within 24+ hours

### Triage Completion Time
- **Target**: < 5 minutes per patient
- **Measurement**: From first data entry to risk level assignment
- **Reporting**: Tracked for performance monitoring

---

## Exception Handling Rules

### Escalation Procedures
1. **Vital Signs Unstable**: Escalate to RED immediately
2. **Patient Deterioration**: Upgrade risk level, reschedule
3. **Professional Unavailable**: Alternative professional search
4. **No Available Slots**: Queue management, waiting list

### Cancellation Rules
- **Patient Initiated**: 24-hour notice recommended
- **System Initiated**: Automatic notification, rescheduling offer
- **No-show**: After 30 minutes, appointment marked as NO_SHOW
- **Record Retention**: 7 years for audit compliance

---

## Audit & Compliance Rules

### Data Retention
- **Patient Records**: 7 years minimum
- **Triage History**: Permanent
- **Appointments**: 5 years
- **Medical Records**: Lifetime access

### LGPD Compliance
- **Data Minimization**: Only necessary data collected
- **Consent**: Explicit for data processing
- **Anonymization**: HIPAA-compliant identifiers
- **Right to be Forgotten**: Retained per legal requirements

### Security & Privacy
- **Encryption**: At-rest and in-transit (TLS 1.3)
- **Access Control**: Role-based (RBAC)
- **Audit Logs**: All access logged
- **Breach Notification**: LGPD 72-hour requirement

---

## Integration Rules

### Kafka Event Publishing
1. **Triage Created**: Publish with risk level
2. **Appointment Confirmed**: Publish with date/time
3. **Medical Record Created**: Publish with diagnosis
4. **Status Changes**: Publish state transitions

### Service Communication
- **Synchronous**: OpenFeign REST calls
- **Asynchronous**: Kafka topic subscriptions
- **Failure Handling**: Circuit breaker patterns
- **Retry Logic**: Exponential backoff (3 attempts)

---

## Business Metrics

### Key Performance Indicators (KPIs)

| Metric | Target | Measurement |
|--------|--------|-------------|
| **RED Service Time** | < 10 min | 95th percentile |
| **ORANGE Service Time** | < 60 min | 95th percentile |
| **YELLOW Service Time** | < 3 hours | 95th percentile |
| **Appointment No-Show Rate** | < 5% | Monthly |
| **Triage Accuracy** | > 95% | Clinical review |
| **System Uptime** | > 99.5% | Monthly |
| **Triage Completion Time** | < 5 min | Average |

---

## Future Enhancements

### Phase 2
- AI-assisted risk prediction
- Vital sign trend analysis
- Predictive patient flow modeling
- Telehealth appointment options

### Phase 3
- Machine learning risk scoring
- Outcome tracking and validation
- Comparative effectiveness research
- Population health analytics

---

**Version**: 1.0.0  
**Last Updated**: May 31, 2024  
**Compliance**: LGPD (Brazil), HIPAA (United States)
