# Agent Context Hub

This directory contains the current, versioned operating context for agents
working on the SUS-Connect APS MVP. It is intentionally concise: it should help
an agent start correctly without replacing the product specification or source
code.

## How To Use It

1. Read the repository-level `AGENTS.md`.
2. Read `context/product-scope.md` before changing behavior or documentation.
3. Read `context/architecture-and-runtime.md` before changing Java, persistence,
   Docker, or API code.
4. Load only the relevant `skills/<name>/SKILL.md` for the task.
5. Follow `workflows/feature-delivery.md` before declaring work complete.

## Layout

| Path | Purpose |
| --- | --- |
| `context/product-scope.md` | Problem, user, MVP outcome, terminology, and strict boundaries. |
| `context/architecture-and-runtime.md` | Active modules, package rules, commands, ports, and test conventions. |
| `skills/` | Focused task playbooks that work across agent tools. |
| `decisions/` | Durable product and architecture decisions that prevent scope drift. |
| `workflows/feature-delivery.md` | Repeatable discovery-to-handoff flow. |
| `templates/` | Small artifacts for feature reasoning and verification evidence. |

## Authority And Maintenance

- The product specification in `docs/produto/` is authoritative for the MVP.
- Source code and API documentation are authoritative for implemented behavior.
- Update this directory only when a durable decision changes. Do not log routine
  implementation details here.
- The repository contains only the APS MVP; do not expand it with the former
  triage project patterns.
