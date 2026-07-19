---
name: quality-and-demo
description: Validate APS service changes and keep the hackathon demonstration reproducible.
---

# Quality And Demo Skill

Use this skill for testing, coverage, Docker, Swagger, Bruno, Insomnia, and
handoff preparation.

## Required Checks

```bash
mvn -q -pl aps-prioritization-service -am test
mvn -q -pl aps-prioritization-service jacoco:check@coverage-check
docker compose up -d --build aps-prioritization-service
```

Confirm that the health endpoint responds `UP`, then exercise the primary flow
against the running service.

## Versioning

After validation, create one atomic Git commit for each completed change. Use a
clear conventional commit message, stage only files related to the change, and
report the resulting commit hash. Never fold unrelated existing worktree
changes into the commit.

## Contract Synchronization

For each endpoint added or modified:

1. Update `docs/API/aps-prioritization-service.md`.
2. Add or update a Bruno request in `docs/API/Aps-Prioritization/`.
3. Add or update the Insomnia export.
4. Use a stable demo ID or have the collection capture a newly created ID.
5. Confirm both success and at least one meaningful invalid request are covered
   by automated tests.

## Demonstration Narrative

The API demonstration should tell one short story:

1. The dashboard shows a high-priority territory.
2. Its detail explains the rule using aggregate indicators.
3. The coordinator creates an outreach action.
4. The team records aggregate progress.
5. The dashboard reflects the operational workload.

Do not demonstrate individual patient data, clinical decisions, or causal
outcome claims.
