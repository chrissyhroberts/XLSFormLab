# Coding Standards

## General

- Keep `MainActivity` minimal.
- Keep shell code free of module-specific logic.
- Prefer declarative settings over bespoke settings screens.
- Use stable ids for modules, settings and return fields.
- Keep module code inside its own package.

## Kotlin

- Use clear data classes for contracts.
- Avoid stringly typed logic where enums are possible.
- Keep composables small.
- Separate UI from execution logic.
- Avoid global mutable state.

## Module ids

Use lowercase snake case:

```text
calibrated_scale
nfc_tags
image_map
```

## Setting ids

Use lowercase snake case:

```text
show_numbers
line_length_mm
return_mode
```

## Return fields

Use lowercase snake case and document every field.

## Commit discipline

Every commit must compile.
