# Testing

## Minimum checks for each commit

- Gradle sync succeeds
- app compiles
- app launches
- no crash on home screen

## Capability checks

Each capability should be tested in:

- demo mode
- settings mode
- intent mode
- field return mode
- JSON return mode
- invalid configuration mode

## Regression tests

Each module should maintain example requests and expected results.

## Manual test template

```text
Module:
Version:
Scenario:
Input settings:
Intent overrides:
Expected result:
Actual result:
Pass/fail:
Notes:
```

## Developer diagnostics

Developer mode should make testing easier by exposing:

- incoming request
- effective settings
- result payload
- errors and warnings
