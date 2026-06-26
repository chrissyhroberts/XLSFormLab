# XLSForm Lab Architecture

## Purpose

XLSForm Lab is an Android capability platform for extending XLSForm-based data collection. Its first client is ODK Collect, but the core design is intentionally broader: any system capable of launching Android intents or exchanging structured requests could use the same capabilities.

The application is divided into two main parts:

1. **The shell** — stable application infrastructure.
2. **Capabilities** — self-contained modules that provide specific functions.

The shell does not know about pain scales, NFC, image maps, DCEs, or sensors. It only knows how to discover, configure, display, invoke and return results from capabilities.

## Core components

```text
XLSForm Lab
│
├── Capability Engine
│   ├── Module registry
│   ├── Settings renderer
│   ├── Intent router
│   ├── Result serializer
│   ├── Help renderer
│   └── Developer diagnostics
│
├── Shell UI
│   ├── Capability explorer
│   ├── Module demo launcher
│   ├── Settings screens
│   ├── Help screens
│   └── Copy-intent tools
│
└── Capabilities
    ├── Calibrated Scale
    ├── NFC
    ├── DCE
    ├── Image Map
    ├── Body Map
    └── Sensors
```

## The Capability Engine

The Capability Engine is the runtime that hosts modules. It is responsible for:

- reading module manifests
- grouping modules into categories
- rendering common UI
- storing admin defaults
- applying temporary intent overrides
- validating incoming requests
- invoking modules
- returning results to the caller
- generating example XLSForm snippets
- exposing diagnostics

The engine should contain no module-specific logic.

## The shell

The shell is the Android application container. It provides:

- the main activity
- the home screen
- navigation
- module browsing
- settings access
- help access
- ODK intent entry points

The shell delegates actual work to capabilities.

## Capabilities

A capability is a self-contained unit of functionality. Each capability defines:

- identity
- manifest
- category membership
- settings schema
- accepted intent parameters
- return schema
- demo UI
- help text
- execution behaviour

Examples include:

- calibrated scale
- NFC reader
- QR scanner
- DCE task
- body map
- image map
- GPS point capture
- protocol completeness checker

## Request flow

```text
ODK Collect
    │
    │ Android Intent
    ▼
Intent Router
    │
    │ Normalised request
    ▼
CapabilityRequest
    │
    ▼
Capability Module
    │
    │ CapabilityResult
    ▼
Result Serializer
    │
    ▼
ODK Collect
```

## Configuration model

Each capability has persistent admin defaults. Incoming intents may temporarily override those defaults for a single invocation.

```text
Admin defaults
      +
Intent overrides
      =
Effective settings for this run
```

Intent overrides must never permanently alter admin defaults.

## Design constraint

If the shell needs to know about an individual capability, the architecture has failed. Improve the archetype or registry rather than adding special cases.
