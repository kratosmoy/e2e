## Context

The current repository already represents a meaningful Q1 Playwright foundation: `core` owns browser/session lifecycle and runtime configuration, `CucumberHooks` captures screenshots and traces on failure, `test-suite` is split by area, and Gradle already exposes full-suite and area-specific runners. The visible baseline today is small but coherent: two feature areas (`common`, `demoapp`), two feature files, two scenario-level examples, and three runner entry points.

The change is not about adding more Playwright runtime behavior. It is about turning the current framework state into a quarterly status artifact that a team can read quickly. The requested output must be a graph, and the team uses draw.io rather than Mermaid, so the design needs a draw.io-maintained visualization that can live in the repo, be reviewed in diffs, and stay easy to update as new app areas are onboarded.

Key constraints:

- The summary must reflect repository-backed facts rather than invented health scores.
- The graph must show both current progress and next planned work in one view.
- The first implementation should stay documentation-oriented and avoid coupling quarterly reporting to test execution.

## Goals / Non-Goals

**Goals:**

- Publish a Q1 Playwright summary as a markdown artifact paired with a draw.io graph as the primary visual output.
- Separate "completed foundation", "current coverage", and "next plan" so maturity and scope are not conflated.
- Base the current-state nodes on facts already present in `core`, `test-suite`, runners, and artifact/report configuration.
- Make the artifact easy to extend when new app areas are added through the existing onboarding flow.

**Non-Goals:**

- Build a live analytics dashboard or automate collection of execution statistics.
- Change Playwright lifecycle behavior, hooks, browser configuration, or current Gradle orchestration as part of this proposal.
- Introduce speculative percentages, adoption scores, or pass-rate metrics that are not visible from the repository.

## Decisions

### 1. Use a draw.io diagram paired with a markdown summary

The implementation should produce a markdown summary document plus an editable `draw.io` source file for the graph. This keeps the content versionable and reviewable in pull requests while matching the tooling the team already has available.

Alternative considered:

- Table or bullet summary: easier to write, but weaker for showing current state versus next milestones in one glance.
- Embedded Mermaid: readable in some markdown renderers, but it does not match the tooling available to the team.

Recommended graph structure:

- Left lane: completed foundation work in `core`, hooks, and Gradle orchestration
- Middle lane: current coverage shape for `common`, `demoapp`, and runner scope
- Right lane: next milestones for app onboarding, deeper coverage, parallelism, and recurring report refresh

### 2. Use repo-backed milestones instead of numerical progress scores

The summary should describe maturity through concrete milestones, not percentages. This repo exposes structure and capabilities clearly, but it does not contain trustworthy telemetry for automation adoption, stability trends, or execution volume.

Alternative considered:

- Show a single "Q1 completion percentage": more executive-friendly, but it would be largely subjective and easy to misread as test health or release readiness.

### 3. Sequence future work along the repository's existing expansion model

The roadmap should follow the patterns already documented in `docs/new-app-onboarding.md`: add app-specific areas first, then increase scenario depth, then revisit parallel execution only after isolation is proven. A recurring report refresh comes last so reporting follows real framework growth instead of leading it.

Alternative considered:

- Prioritize parallel execution before new business coverage: this could improve speed, but the current repo has too little area coverage to justify the added concurrency risk yet.

### 4. Implement the first summary as a documentation artifact, not a generated test report

The first implementation should live under `docs/` as a maintained quarterly summary with a companion `draw.io` file. This keeps the work separate from Allure and runtime test outputs and avoids mixing framework communication with execution reporting.

Alternative considered:

- Generate the summary as part of Gradle test tasks or Allure output: more automated, but unnecessary for the first iteration and harder to maintain while the content is still curated.

## Risks / Trade-offs

- [Risk] The graph can become stale as soon as new app areas or scenarios are added. -> Mitigation: define a refresh trigger whenever area onboarding or quarterly planning is updated.
- [Risk] A repo-backed summary may be interpreted as execution health. -> Mitigation: keep wording focused on framework progress and coverage shape, not pass rate or runtime reliability.
- [Risk] Raw draw.io XML is less readable in diffs than plain markdown. -> Mitigation: keep the markdown summary concise and preserve stable node names and lane structure in the diagram.
- [Risk] Some repository viewers will not render the graph inline. -> Mitigation: link the `draw.io` file directly from the summary and README so editors can open the source graph immediately.
- [Risk] The roadmap may age quickly if priorities change. -> Mitigation: write planned items as milestone categories, not fragile ticket-by-ticket detail.

## Migration Plan

1. Create the quarterly summary markdown artifact in `docs/`.
2. Populate the graph from current repository facts and add a short legend or note if needed.
3. Link the summary from an existing discoverable location such as `README.md` or a docs index.
4. Update the summary in future quarters instead of tying it to test execution output.

## Open Questions

- Should later quarterly summaries remain hand-curated, or should the repo eventually generate the baseline counts automatically from file structure?
- Should the summary stay as one rolling artifact, or should each quarter create a separate dated report file?
