# Product Scope - Active Outreach Prioritization in APS

## The Problem

APS coordinators may need to choose where to start preventive active outreach
while indicators are fragmented across spreadsheets and operational systems.
Limited team capacity makes this prioritization hard to explain, repeat, and
follow up.

The MVP provides a simple, territorial answer: rank the areas or UBS units that
need attention first, explain the aggregate signals behind the ranking, and
track the execution of an outreach action.

## User And Outcome

Primary user: a municipal APS coordinator or UBS coordinator.

Secondary user: ESF/ACS teams that update an aggregate action result.

The user must be able to complete this flow quickly:

1. Open the dashboard and identify high-priority territories.
2. Inspect a territory and understand its priority rule.
3. Create an active-outreach action for a preventive focus.
4. Update its aggregate progress.
5. Return to the dashboard to follow the operational work.

## Tangible Case

Joao is a fictitious 58-year-old person who once began hypertension and diabetes
follow-up but later lost periodic contact with care. Years later he seeks urgent
care with a worse condition. The product does not store Joao's record or decide
his treatment. The case illustrates why a coordinator might prioritize a
territory where linkage to APS and chronic-condition follow-up indicators are
both weak, then organize an aggregate outreach action.

Do not claim that the action would have prevented Joao's outcome. Its value is
to support an earlier operational response and make its execution visible.

## Domain Language

| Term | Meaning In This MVP |
| --- | --- |
| Territory | An aggregate operational area or UBS reference unit. Never a patient list. |
| Linkage | Percentage used as an APS connection/coverage signal for the territory. |
| Preventive indicator | Aggregate percentage for a defined preventive focus and its target. |
| Priority | Explainable operational classification: `HIGH`, `MEDIUM`, or `LOW`. |
| Active-outreach action | Planned, in-progress, completed, or cancelled territorial work. |
| Progress | Aggregate performed count compared with a planned target. |

## Priority Rule

- `HIGH`: linkage below the configured target and at least one preventive
  indicator below its target.
- `MEDIUM`: exactly one of those signals is below its target.
- `LOW`: linkage and all available indicators meet their targets.

This is a transparent operational heuristic. It is not a clinical model.

## Explicit Exclusions

- Individual patient registration, records, and clinical history.
- Risk scoring, diagnosis, clinical recommendation, or treatment decision.
- Patient scheduling, prescriptions, referrals, beds, or regulation.
- Message sending or automatic contact with citizens.
- Real-time connection to e-SUS APS, SISAB, CNES, hospitals, or regulation.
- Claims of clinical, financial, or causal impact not supported by evaluation.

## Evidence And Communication

The project used official aggregated sources as evidence of an opportunity, not
as proof of an individual-level cause. Preserve this language:

- Say: "supports operational prioritization".
- Say: "aggregate indicator below a configured target".
- Say: "hypothesis to validate with the local team".
- Do not say: "predicts deterioration" or "prevents hospitalization".

See `docs/produto/especificacao-requisitos.md` for the
complete requirements, constraints, and acceptance criteria.
