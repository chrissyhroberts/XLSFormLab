# Versioning

## Why versioning matters

Scientific data collection tools may be used in long-running studies. A change in module behaviour can affect comparability, reproducibility and auditability.

## App version

The app has a semantic version:

```text
XLSForm Lab 0.2.0
```

## Capability version

Each capability has its own version:

```text
calibrated_scale@1.0.0
```

## Compatibility rule

A form may request a specific module version.

```text
S.module_id=calibrated_scale
S.module_version=1.0.0
```

If no version is specified, the latest compatible version is used.

## Breaking changes

Breaking changes require a major version increment.

Examples:

- changed meaning of a returned field
- changed scale interpretation
- changed default behaviour in a way that affects data
- removed accepted parameters

## Non-breaking changes

Minor or patch releases may include:

- UI improvements
- new optional settings
- additional return metadata
- bug fixes that do not change expected semantics

## Deprecation

Deprecated versions should remain available where feasible, but marked clearly in the capability explorer.
