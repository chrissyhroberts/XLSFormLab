# XLSForm Lab Documentation

XLSForm Lab is a capability platform for scientific and field data collection. It provides reusable Android capabilities that can be launched from ODK Collect and other clients through a standard request/response interface.

This documentation set defines the architecture, terminology, lifecycle, intent protocol, settings schema and developer conventions for the platform.

## Core idea

Everything is a capability.

A capability is a self-contained module that can:

- describe itself through a manifest
- run in standalone demo mode
- expose admin-configurable defaults
- accept temporary runtime overrides from an intent
- return results as fields or JSON
- generate example XLSForm intent text
- provide embedded help and diagnostics

## Documentation map

- `architecture/ARCHITECTURE.md` — overall system architecture
- `architecture/PHILOSOPHY.md` — design principles
- `architecture/CAPABILITY_ARCHETYPE.md` — standard capability template
- `architecture/INTENT_SPECIFICATION.md` — ODK and Android intent protocol
- `architecture/SETTINGS_SCHEMA.md` — declarative settings model
- `architecture/MODULE_LIFECYCLE.md` — lifecycle of a capability
- `architecture/RETURN_SCHEMA.md` — field and JSON result contracts
- `architecture/VERSIONING.md` — capability and API versioning
- `developer/GETTING_STARTED.md` — developer onboarding
- `developer/ADDING_A_CAPABILITY.md` — how to create a new module
- `developer/CODING_STANDARDS.md` — code style and conventions
- `developer/TESTING.md` — testing expectations
- `modules/calibrated_scale.md` — first measurement module specification
- `modules/nfc.md` — NFC capability plan
- `modules/body_map.md` — body map capability plan
- `modules/image_map.md` — image map capability plan
- `modules/dce.md` — discrete choice experiment capability plan
- `modules/sensors.md` — sensor capability plan
- `protocols/odk_intents.md` — ODK intent examples
- `protocols/json_returns.md` — JSON return examples
- `protocols/field_returns.md` — field return examples
- `protocols/examples.md` — end-to-end examples
- `roadmap/ROADMAP.md` — staged development roadmap
- `roadmap/BACKLOG.md` — candidate module backlog
- `roadmap/FUTURE_IDEAS.md` — speculative future capabilities
