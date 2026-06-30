# ResearchOS Runtime

See `docs/Architecture.md` for the overall architecture vision.

## Purpose

XLSForm Lab is a capability platform for Android providing reusable field research capabilities,
measurement tools and interaction modules that can be launched from ODK, KoBo or any Android app.

## Developer principles

- Capabilities are transport-independent.
- Capabilities declare dependencies, outputs and settings.
- Capabilities consume platform services rather than Android APIs directly.
- Use the Signal Framework and Capture Engine wherever possible.
- Transparency and provenance are first-class design goals.

ResearchOS Roadmap

ResearchOS is an open platform for building reusable, device-independent research methods. The project is guided by Architecture Standard 1.00 (AS1.00), which separates knowledge, execution, presentation and device interaction into independent architectural layers.

This roadmap describes the planned direction of the platform rather than a fixed release schedule. Priorities may evolve as the architecture matures and new research use cases emerge.

⸻

## Current Status

### ResearchOS Runtime 1.x

The first implementation of the AS1.00 runtime is complete.

Implemented components include:

* ✅ Execution Engine
* ✅ Method Registry
* ✅ Device Service framework
* ✅ Signal model
* ✅ Presentation separation
* ✅ Native ResearchOS Methods
* ✅ ResearchOS project identity

Current native methods include:

* ✅ NFC Read
* ✅ NFC Write
* ✅ Calibrated Scale
* ✅ GPS / Locate Target

These demonstrate observation, intervention, measurement and sensor integration using the same execution model.

⸻

### Phase 2 – Platform Maturity

The next phase focuses on making ResearchOS easier to develop, extend and maintain.

#### Documentation

Planned documentation includes:

* Architecture Guide
* Knowledge Model
* Device Service Guide
* Adding New Methods
* Presentation Framework
* Testing Guide

#### Repository organisation

The repository will gradually be reorganised into clear architectural domains including:

* Runtime
* Knowledge
* Methods
* Device Services
* Presentation
* Documentation
* Examples
* Tests

#### Testing

Expand automated testing with:

* Runtime tests
* Method tests
* Device service tests
* Knowledge model tests

The goal is for most of the platform to be testable independently of Android.

⸻

### Phase 3 – Core Research Domains

ResearchOS will grow through reusable domains rather than isolated application features.

####Identification

* NFC
* QR Codes
* Barcodes
* Bluetooth Low Energy
* RFID
* Identity verification

#### Location

* GPS
* Compass
* Indoor positioning
* Geofencing
* Augmented reality positioning

#### Measurement

* Calibrated visual scales
* Distance estimation
* Height estimation
* Light measurement
* Sound measurement
* Accelerometer-derived measurements
* Environmental sensors

#### Imaging

* Camera capture
* Image annotation
* Body maps
* Lesion mapping
* Spatial image overlays

#### Workflow

* Participant context
* Protocol completeness
* Visit scheduling
* Study reminders
* Form orchestration
* Cross-form state management

⸻

### Phase 4 – Knowledge Framework

ResearchOS is evolving towards a platform where research information is represented as reusable knowledge objects rather than application-specific data structures.

Planned knowledge object types include:

* Entities
* Observations
* Measurements
* Transformations
* States
* Relationships
* Methods
* Signals
* Protocol definitions

These objects will allow methods to compose naturally while remaining independent of user interface or hardware implementation.

⸻

### Phase 5 – Research Integrations

ResearchOS is intended to interoperate with existing research ecosystems rather than replace them.

Planned integrations include:

* ODK
* KoBoToolbox
* XLSForms
* Home Assistant
* External sensors
* Bluetooth peripherals
* Laboratory instruments
* Cloud services
* Local-first data stores

⸻

### Phase 6 – Multi-platform Runtime

Although development currently targets Android, the runtime architecture has been designed to support multiple hosts.

Future runtime implementations may include:

* Android
* Desktop
* Server
* Command line tools
* Embedded devices

Methods should execute consistently across hosts wherever possible.

⸻

## Design Principles

ResearchOS development follows a small number of core principles.

* Methods should be reusable.
* Device interactions should be isolated behind Device Services.
* Presentation should remain separate from execution.
* Knowledge should be represented using explicit, reusable objects.
* New functionality should fit the architecture rather than require architectural redesign.
* The platform should remain open, modular and interoperable.

⸻

## Contributing

Contributions are welcome.

The preferred approach is to add new functionality as reusable ResearchOS Methods that integrate cleanly with the existing runtime rather than as application-specific features.

As the platform evolves, maintaining architectural consistency is considered as important as adding new functionality.
