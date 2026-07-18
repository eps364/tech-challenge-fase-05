# Feature Delivery Workflow

Use this workflow for any non-trivial change. It keeps the project aligned with
the hackathon story while avoiding unnecessary scope growth.

## 1. Frame The Change

- Identify the user decision or operational task that changes.
- Link the request to an existing RF/RN when possible.
- Check `.agents/context/product-scope.md` for scope and privacy boundaries.
- Write a short feature brief with the template when the request is ambiguous.

## 2. Design The Smallest Slice

- Name the domain rule and the use case.
- Decide whether persistence or API contract changes are actually required.
- Define one happy path and meaningful validation failures.
- Prefer extending the existing territory/action model over adding a new
  subsystem.

## 3. Implement

- Keep framework code in `infra` and business rules in `core`.
- Use a Flyway migration for database changes.
- Use aggregate, fictitious data only.
- Preserve request/response compatibility unless a versioned change is agreed.

## 4. Verify

- Run unit and integration tests.
- Run the explicit JaCoCo threshold check.
- Start the APS Docker service and execute the affected API flow.
- Update Swagger-visible behavior, API docs, Bruno, and Insomnia together.

## 5. Handoff

- Summarize behavior, constraints, tests, and known limitations.
- Record exact commands and observed result in the verification template for
  material changes.
- Mention only factual test results. If the environment blocked a check, state
  the cause and the fallback validation performed.

## Avoid

- Rewriting unrelated legacy services.
- Treating an aggregate indicator as individual clinical truth.
- Introducing external real-time data dependencies into the demonstration path.
- Staging or reverting another contributor's changes.
