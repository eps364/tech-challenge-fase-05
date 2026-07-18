---
name: aps-product
description: Keep product, privacy, and evidence claims aligned with the APS active-outreach MVP.
---

# APS Product Skill

Use this skill before changing requirements, behavior, demo narratives, or
documentation connected to the SUS problem.

## Inputs To Read

1. `.agents/context/product-scope.md`
2. `docs/pesquisa/especificacao_requisitos_mvp_gestao_ativa_aps.md`
3. `docs/pesquisa/oportunidades_descobertas_para_avaliacao.md` when a change
   refers to evidence or impact.

## Working Method

1. State the operational user, decision, and aggregate outcome affected.
2. Confirm that the behavior stays within the territorial MVP boundary.
3. Label claims correctly as evidence, interpretation, hypothesis, or product
   behavior.
4. Prefer the smallest end-to-end change that improves the coordinator flow.
5. Add acceptance criteria that can be demonstrated through the API.

## Guardrails

- Do not silently expand a territory feature into patient management.
- Do not use a fictitious individual as persisted data or an API resource.
- Do not turn an aggregate priority into a medical risk label.
- Do not promise a health outcome that the available data cannot establish.

## Done When

- The changed flow still fits the eight-minute MVP demonstration.
- The documentation explains the new behavior and its limit in plain Portuguese.
- Tests verify the most important acceptance criterion.
