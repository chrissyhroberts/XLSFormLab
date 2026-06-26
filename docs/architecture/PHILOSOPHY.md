# XLSForm Lab Philosophy

## Everything is a capability

The central rule of XLSForm Lab is:

> Everything is a capability.

A capability is not just a screen or widget. It is a complete unit of scientific data collection functionality with a manifest, settings, help, execution logic, return values and examples.

## Design principles

### 1. The shell should stay small

The shell provides infrastructure. It should not contain logic for individual tools.

### 2. Modules describe themselves

Capabilities should advertise what they do, which settings they accept, which results they return and how they should appear in the UI.

### 3. Admin defaults are persistent

Device-level settings represent administrator-approved defaults.

### 4. Intent overrides are temporary

A study may override settings at runtime, but those overrides apply only to the current invocation.

### 5. Demo mode is mandatory

Every capability must run without ODK. This makes testing, training and form design easier.

### 6. Copyable examples are mandatory

A user should be able to configure a capability visually and copy a ready-to-paste XLSForm intent template.

### 7. Documentation should be generated where possible

Settings schemas, return schemas and manifests should be used to generate help pages, examples and diagnostics.

### 8. ODK is the first client, not the whole architecture

The platform should remain usable by other clients that can call Android intents or structured APIs.

### 9. Research tools need auditability

Capabilities should expose diagnostics: what request was received, what settings were used, what result was returned and what version produced it.

### 10. The 50th module should be easy

The architecture should make adding a new capability predictable and boring.
