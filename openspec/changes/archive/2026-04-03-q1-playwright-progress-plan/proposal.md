## Why

The repository already has a usable Playwright-based E2E framework, but it does not yet have a concise quarterly artifact that shows what was completed in Q1 and what should happen next. A visual Q1 summary is needed now so the team can communicate framework maturity, coverage progress, and the next implementation priorities from one consistent source.

## What Changes

- Add a change proposal for a Q1 Playwright summary artifact that combines current-state progress with the next-stage execution plan.
- Define a visual reporting format that turns repo facts into a simple graph instead of a text-only status update.
- Establish the source of truth for the summary from current framework capabilities, test area coverage, runners, and reporting/artifact support already present in the repository.
- Document the follow-up work needed to implement and maintain the summary as the framework grows beyond the current common and demoapp areas.

## Capabilities

### New Capabilities
- `playwright-progress-reporting`: Produce a quarterly Playwright progress summary that visualizes completed foundation work, current coverage, and the next planned milestones in one report-ready graph.

### Modified Capabilities
- None.

## Impact

- Affects documentation and planning under `openspec/changes/q1-playwright-progress-plan/`.
- Shapes a future implementation that will likely touch reporting or docs-oriented assets rather than core Playwright lifecycle code.
- Uses existing framework facts from `core`, `test-suite`, runners, and artifact/report configuration as the baseline for the summary.
