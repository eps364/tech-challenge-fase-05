---
name: data-ethics
description: Work safely with SUS evidence and aggregate APS demonstration data.
---

# Data And Ethics Skill

Use this skill for analytics, demo data, dashboards, API payloads, and claims
about SUS outcomes.

## Allowed Data Shape

- Territory or UBS name/identifier.
- Data competency and source metadata.
- Aggregate linkage, indicator, target, action count, and action-progress data.
- Fictitious demonstrative territories and teams.

## Disallowed Data Shape

- Names, CPF, addresses, phones, medical record IDs, or patient-level events.
- Diagnoses, clinical histories, prescriptions, exam results, or individual
  clinical risk scores.
- A payload that can re-identify a person by combining sparse facts.

## Claim Discipline

| Type | Example |
| --- | --- |
| Evidence | "The analysis found municipalities with aggregate linkage below the reference." |
| Interpretation | "This can indicate a place worth operational review." |
| Hypothesis | "A coordinator may find territorial ranking useful for organizing outreach." |
| Unsupported claim | "The tool prevents hospitalizations." |

When a source is updated, document the source, period, coverage, processing,
and limitations. Never replace a documented limit with a stronger causal claim.
