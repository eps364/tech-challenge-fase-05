# ADR 001 - Territorial Prioritization Boundary

Status: Accepted

Date: 2026-07-18

## Context

The evidence review found aggregate signals of weak APS linkage and preventive
follow-up in multiple municipalities. The team selected a small, demonstrable
opportunity: help an APS coordinator prioritize territorial active-outreach
work and track aggregate execution.

The repository also contains an earlier triage-oriented project history. That
history has broader clinical and integration assumptions which do not match the
selected hackathon MVP.

## Decision

The active MVP will use the territory/UBS as its only prioritization unit.
It will store aggregate indicators and aggregate action progress only.

The initial priority rule uses two transparent signals:

1. APS linkage below a configured target.
2. At least one preventive indicator below its configured target.

The result is an operational priority (`HIGH`, `MEDIUM`, or `LOW`) with an
explanation of the contributing signals.

## Consequences

- The service does not manage individual patients, clinical records, diagnoses,
  risk scores, appointments, or messages.
- There is no real-time integration requirement for e-SUS APS, SISAB, CNES, or
  hospital systems in the MVP flow.
- The system must state that priority supports an operational decision and does
  not predict clinical outcomes or prove impact.
- Features that introduce person-level data or clinical decisions require an
  explicit product decision before implementation.

## References

- `docs/produto/especificacao-requisitos.md`
- `docs/dados/oportunidades-descobertas.md`
- `.agents/context/product-scope.md`
