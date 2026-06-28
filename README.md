# XLSForm Lab capability design notes

This document records the current design principles for XLSForm Lab and the rules for adding new capabilities.

## Purpose

XLSForm Lab is a workbench for building Android capabilities that can be configured visually, previewed live, launched from ODK/XLSForm, and return structured data.

The core idea is:

```text
Capability
  -> Settings
  -> Live demo
  -> Output
  -> Transport
  -> ODK / clipboard / Android intent / future systems
```

A capability should not know about ODK, clipboard, JSON transport, or Android intent formatting. It should describe itself, render its UI, and produce a standard `CapabilityOutput`. The framework handles the rest.

## Current platform components

### Capability engine

Each capability provides:

- `manifest`
- `settings`
- `Demo(settingsState)`
- `Help()`
- `execute(request)`
- `buildOutput(settingsState)`
- output schema, where implemented

Capabilities are registered centrally in `CapabilityRegistry`.

### Settings engine

Settings are declarative. A capability should define settings as metadata, not hand-code its own settings screen.

Supported setting types currently include:

- `TextSetting`
- `BooleanSetting`
- `FloatSetting`
- `IntSetting`
- `ChoiceSetting`

Settings can include metadata such as:

- `id`
- `label`
- `description`
- `group`
- `minimum`
- `maximum`
- `step`
- `unit`
- `decimals`

The generic settings renderer should be responsible for UI layout, grouping, sliders, numeric text entry, switches and persistence.

### Settings persistence

Settings are persisted per capability using DataStore. A capability should not implement its own persistence unless there is a strong reason.

### Device calibration

Physical measurement should use the shared calibration service.

The current calibration concept is:

```text
Device calibration:
  dp_per_mm

Capability setting:
  real-world size, e.g. vas_length_mm = 100

Renderer:
  screen length = vas_length_mm * dp_per_mm
```

Important rule: if the physical size of a UI element is part of a validated instrument, it belongs in the capability configuration. The device calibration factor belongs to the device, not to the form.

### Output engine

Capabilities produce a `CapabilityOutput`. The output should be transport-independent.

Do not make capabilities produce raw ODK strings, Android intent URIs, JSON strings, or clipboard text directly.

The output layer supports multiple return formats:

- single value
- named fields
- JSON
- datapoints / multiple values

### Transport builders

Transport logic is separated from capabilities.

Current transport targets include:

- ODK appearance column
- ODK intent column
- Android Intent URI
- Kotlin Intent code
- JSON / return preview
- Clipboard copy

Future targets may include:

- FHIR
- REDCap
- OpenMRS
- REST API
- CSV
- QR/NFC encoding

### Intent parser

The workbench can parse generated appearance/intent strings and repopulate the settings UI. This makes the app a round-trip editor, not just a generator.

## Rules for writing a new capability

### 1. Keep the capability transport-independent

A capability should not know whether it is being used by ODK, KoBo, clipboard, Android, JSON, or another system.

Good:

```text
buildOutput() returns fields
```

Bad:

```text
Capability constructs XLSForm appearance text
```

### 2. Use declarative settings

Add configuration through `CapabilitySetting` objects.

Do not write a custom settings UI unless the setting genuinely cannot be expressed through the common setting model.

### 3. Use stable IDs

Setting IDs and output field IDs should be stable, lowercase, and machine-readable.

Good:

```text
target_latitude
target_longitude
arrival_radius_m
confirmed
timestamp_ms
```

Bad:

```text
Target Latitude
lat now maybe
value1
```

Changing IDs will break saved settings, generated intents, and existing XLSForms.

### 4. Group settings

Use setting groups to keep the UI readable.

Suggested common groups:

- `Appearance`
- `Input`
- `Scale`
- `Range`
- `Display`
- `Output`
- `Advanced`
- `Permissions`
- `Sensor`
- `Target`

### 5. Declare units and steps

Numeric settings should include units and sensible step sizes.

Examples:

```text
VAS length: unit = "mm", step = 0.5
Arrival radius: unit = "m", step = 1
Latitude: decimals = 6
Bearing: unit = "°", decimals = 1
```

### 6. Prefer reusable platform services

Do not reimplement platform concerns inside capabilities.

Use shared services for:

- device calibration
- settings persistence
- output formatting
- ODK transport
- Android intent formatting
- permissions, where possible
- sensors, where possible

### 7. Make outputs explicit

A capability should make clear what it returns.

Examples:

#### Calibrated scale

```text
value
lower_value
upper_value
minimum
maximum
vas_length_mm
```

#### Biometric confirmation

```text
confirmed
auth_method
timestamp_ms
message
```

#### GPS target navigator

```text
latitude
longitude
accuracy_m
target_latitude
target_longitude
distance_m
bearing_deg
arrived
timestamp_ms
```

### 8. Never store sensitive biometric data

Biometric capability rules:

- Use Android `BiometricPrompt`.
- Return confirmation status only.
- Do not access, store, export, or infer fingerprint data.
- PIN / pattern / password fallback may be supported through device credentials.
- Return the method used where Android exposes it safely.

### 9. Permissions must be visible and explainable

Capabilities requiring permissions should explain:

- what permission is needed
- why it is needed
- what happens if it is denied

Examples:

- location for GPS target navigation
- camera for AR / image annotation
- microphone for sound metering
- NFC for tag reading/writing

### 10. Prefer incremental implementation

For complex capabilities, implement in layers that each compile:

Example GPS capability:

1. target settings and output
2. live fused location
3. compass heading
4. bearing arrow
5. AR overlay
6. map view

Avoid large untested patches.

## Current capabilities

### Calibrated Scale

A physically calibrated continuous scale / VAS style capability.

Key principles:

- real-world length is a capability setting, e.g. `vas_length_mm`
- rendering uses device calibration
- supports single value and dual-scale/range style output
- can be horizontal or vertical
- supports endpoint labels and current value display
- outputs are available through the common output/transport system

### Admin Fingerprint Confirmation

A utility capability for confirmation by the current registered phone user/admin.

Key principles:

- uses Android biometric/device credential prompt
- supports fingerprint/biometric and PIN fallback where available
- returns confirmation status and metadata only
- does not access or store biometric data

### GPS Target Navigator

A utility/navigation capability for directing the user to a target coordinate.

Current status:

- target latitude/longitude settings
- location permission
- live fused location updates
- distance and bearing calculation
- arrival detection
- compass sensor and AR overlay still pending

## ODK/XLSForm integration concepts

There are separate concepts that should not be confused:

### Return format

What the capability returns.

Examples:

- single
- fields
- JSON
- datapoints

### Launch / integration format

How XLSForm/ODK launches the capability.

Examples:

- appearance column
- intent column
- Android Intent URI
- Kotlin Intent code

### Developer/debug formats

Android intent URI and Kotlin Intent snippets are useful for debugging and native Android integration, but they are not necessarily the right thing to paste directly into an XLSForm.

## Naming conventions

### Capability IDs

Use lowercase snake case.

Examples:

```text
calibrated_scale
admin_fingerprint
gps_target_navigator
```

### Setting IDs

Use lowercase snake case and include units where useful.

Examples:

```text
vas_length_mm
arrival_radius_m
target_latitude
target_longitude
show_current_value
```

### Output IDs

Use lowercase snake case.

Examples:

```text
confirmed
timestamp_ms
distance_m
bearing_deg
accuracy_m
```

## Suggested capability template

A new capability should usually include:

```text
modules/<capabilityname>/
  <CapabilityName>Capability.kt
```

For larger capabilities, split into:

```text
modules/<capabilityname>/
  <CapabilityName>Capability.kt   // manifest, settings, output
  <CapabilityName>Demo.kt         // UI
  <CapabilityName>Math.kt         // calculations
  <CapabilityName>Provider.kt     // sensors/platform bridge if needed
```

Register the capability in `CapabilityRegistry`.

## Commit discipline

Prefer small buildable commits.

Examples:

```text
feat(scale): render VAS using device calibration
feat(output): add capability output schema
refactor(output): introduce transport builders
feat(capability): add biometric confirmation with PIN fallback
feat(gps): introduce GPS target navigator capability
fix(gps): use fused location updates
```

## Near-term roadmap

1. Finish GPS Target Navigator
   - rotation-vector compass
   - smoothed heading
   - bearing arrow relative to heading
   - AR overlay
   - optional map view

2. Improve ODK validation
   - validate generated appearance/intent values
   - warn about missing required fields
   - version ODK syntax if needed

3. Add more utility capabilities
   - QR scanner/generator
   - NFC read/write
   - camera annotation
   - light meter
   - sound meter

4. Add richer clinical/research capabilities
   - body map
   - lesion map
   - timeline
   - exposure calendar
   - household roster
   - chain-of-custody
