Here’s the consolidated roadmap.

ResearchOS Roadmap

What is ResearchOS?

ResearchOS is an open platform for building reusable digital research methods and lightweight protocol workflows.

Instead of creating a separate application for every research project, ResearchOS provides a library of reusable methods that can be combined into study-specific workflows. A method performs a single well-defined research task, such as reading an NFC tag, recording a GPS location, measuring something with a calibrated scale, verifying a participant, scanning a QR code, opening a web form or logging completion of a protocol step.

ResearchOS can act in two complementary ways.

First, it can be a companion app that other systems call when they need the phone to do something. For example, an ODK form might ask ResearchOS to scan an NFC tag, verify a fingerprint and return named fields such as "scan_nfc_tag_id", "scan_nfc_timestamp" and "fingerprint_verified".

Second, it can be a personal protocol companion for a participant or fieldworker. For example, a participant can scan a QR code that installs a personalised study timeline. ResearchOS then knows that on day 1, day 5 and day 10 the participant needs to complete specific web forms. It sends reminders, opens the correct form, records completion state, logs missed windows and shows the participant their progress.

ResearchOS is not initially intended to be a central study controller for large multi-site studies. The immediate goal is local, phone-based protocol support: one phone, one participant or enumerator, one local timeline, with robust offline-first state and detailed provenance.

---

Current Status

ResearchOS Runtime 1.x

The first implementation of the AS1.00 runtime is complete.

Implemented components include:

- Execution Engine
- Method Registry
- Device Service framework
- Signal model
- Presentation separation
- ResearchOS project identity
- Native ResearchOS Methods

Current native methods include:

- NFC Read
- NFC Write
- Calibrated Scale
- GPS / Locate Target

These demonstrate observation, intervention, measurement and sensor integration using the same execution model.

---

Phase 2 – Intent Language and API

The next priority is to define how other systems talk to ResearchOS.

ResearchOS needs a human-readable, cross-platform intent language that can be carried through Android intents, JSON, URLs, QR codes, NFC tags, web callbacks or future APIs.

The design principle is:

«Human-readable intent, canonical internal objects, configurable external field names.»

An external system should be able to ask for something simple:

scan NFC, verify PIN

or:

{
  "intent": "scan_nfc",
  "verify": "pin"
}

ResearchOS then resolves that into canonical methods, device services, signals, observations and provenance records.

Configurable outputs

ResearchOS must return detailed provenance and metadata, but the caller should control the field names.

Example:

{
  "intent": "scan_nfc",
  "context": {
    "study_id": "trial_001",
    "visit_id": "day_7",
    "participant_id": "${participant_id}"
  },
  "options": {
    "verify": "fingerprint",
    "timeout_seconds": 30,
    "provenance": "full"
  },
  "return": {
    "tag_id": "scan_nfc_tag_id",
    "timestamp": "scan_nfc_timestamp",
    "device.id": "scan_nfc_device_id",
    "execution.id": "scan_nfc_execution_id",
    "verification.status": "fingerprint_verified",
    "verification.timestamp": "fingerprint_verified_timestamp",
    "provenance": "scan_nfc_provenance_json"
  }
}

This allows ODK, KoBoToolbox, web forms and other systems to request simple actions while receiving precisely named fields suitable for their own data models.

API components

Planned API components include:

- Intent parser
- Method invocation API
- Method discovery API
- Device service discovery API
- Return-field mapping
- Provenance policy configuration
- Error schema
- Event schema
- Android intent transport
- JSON transport
- URL/deep-link transport
- Web callback support

---

Phase 3 – Personal Protocol Orchestration

ResearchOS should support local protocol workflows without pretending to be a central study server.

This is not a separate architectural layer. It is an implementation of the existing ResearchOS model using knowledge objects, methods, signals, device services, execution and presentation.

Immediate use case

A participant joins a study and installs ResearchOS.

They scan a QR code defining their personalised protocol:

participant: jim
study: demo_trial
start_date: 2026-06-30

tasks:
  - day: 1
    action: open_web_form
    form: baseline

  - day: 5
    action: open_web_form
    form: symptoms

  - day: 10
    action: open_web_form
    form: followup

ResearchOS stores this as a local protocol timeline.

On day 5 it sends a notification:

Your day 5 study form is ready.

When the participant taps it, ResearchOS opens the correct web form. The form may call back to ResearchOS for additional actions, such as fingerprint verification or NFC scanning. ResearchOS returns the requested values and records what happened.

The participant can then see a progress view:

Day 1 complete
Day 5 complete
Day 10 upcoming

If a notification is declined, ResearchOS can schedule a reminder. If the task window is missed, ResearchOS can record a missed task and optionally submit or prepare a did-not-complete event.

Core objects

Protocol orchestration will use explicit knowledge and state objects, including:

- ProtocolDefinition
- ParticipantTimeline
- ScheduledTask
- TaskWindow
- TaskState
- TaskOutcome
- ReminderPolicy
- CompletionRule
- MissedWindowRule
- ProtocolEvent
- ProvenanceRecord

Core methods

Initial protocol methods may include:

- ImportProtocolFromQrMethod
- ScheduleProtocolTasksMethod
- NotifyParticipantMethod
- OpenWebFormMethod
- RecordTaskOutcomeMethod
- MarkTaskMissedMethod
- CheckTimelineStatusMethod
- VerifyParticipantMethod
- SubmitProtocolEventMethod

Device services and integrations

Protocol orchestration will use existing and future device services, including:

- Notification service
- Browser/app launch service
- Biometric verification service
- NFC service
- QR scanner service
- Local storage service
- Network/API connector

Scope

The first version should focus on:

- one phone;
- one participant or enumerator;
- local task state;
- phone notifications;
- web form or ODK launch;
- callbacks from forms into ResearchOS;
- completion dashboard;
- detailed provenance.

Future server-side orchestration can build on the same model, but is not required for the first implementation.

---

Phase 4 – Platform Maturity

This phase focuses on making ResearchOS easier to develop, extend and maintain.

Documentation

Planned documentation includes:

- Architecture Guide
- Knowledge Model
- Intent Language and API Guide
- Protocol Orchestration Guide
- Device Service Guide
- Adding New Methods
- Presentation Framework
- Testing Guide

Repository organisation

The repository will gradually be reorganised into clear architectural domains including:

- Runtime
- Knowledge
- API
- Protocols
- Methods
- Device Services
- Presentation
- Documentation
- Examples
- Tests

Testing

Expand automated testing with:

- Runtime tests
- Method tests
- Intent/API tests
- Protocol timeline tests
- Device service tests
- Knowledge model tests

The goal is for most of the platform to be testable independently of Android.

---

Phase 5 – Knowledge Framework

ResearchOS will represent research information as reusable knowledge objects rather than application-specific data structures.

Planned knowledge object types include:

- Entities
- Observations
- Measurements
- Transformations
- States
- Relationships
- Methods
- Signals
- Protocol definitions
- Intents
- Events
- Provenance records

These objects will allow methods and protocol workflows to compose naturally while remaining independent of user interface, hardware implementation or calling system.

---

Phase 6 – Core Research Domains

ResearchOS will grow through reusable domains rather than isolated application features.

Identification

- NFC
- QR Codes
- Barcodes
- Bluetooth Low Energy
- RFID
- Identity verification

Location

- GPS
- Compass
- Indoor positioning
- Geofencing
- Augmented reality positioning

Measurement

- Calibrated visual scales
- Distance estimation
- Height estimation
- Light measurement
- Sound measurement
- Accelerometer-derived measurements
- Environmental sensors

Imaging

- Camera capture
- Image annotation
- Body maps
- Lesion mapping
- Spatial image overlays

Workflow

- Participant context
- Protocol completeness
- Visit scheduling
- Study reminders
- Form orchestration
- Cross-form state management

---

Phase 7 – Research Integrations

ResearchOS is intended to interoperate with existing research ecosystems rather than replace them.

Planned integrations include:

- ODK
- KoBoToolbox
- XLSForms
- Web forms
- Home Assistant
- External sensors
- Bluetooth peripherals
- Laboratory instruments
- Cloud services
- Local-first data stores

---

Phase 8 – Multi-platform Runtime

Although development currently targets Android, the runtime architecture has been designed to support multiple hosts.

Future runtime implementations may include:

- Android
- Desktop
- Server
- Command line tools
- Embedded devices

Methods, intents and protocol definitions should execute consistently across hosts wherever possible.

---

Design Principles

ResearchOS development follows a small number of core principles.

- Methods should be reusable.
- Intents should be human-readable.
- External field names should be configurable.
- Provenance should be detailed, explicit and configurable.
- Device interactions should be isolated behind Device Services.
- Presentation should remain separate from execution.
- Protocol orchestration should use the same core model rather than become a separate layer.
- Knowledge should be represented using explicit, reusable objects.
- Other systems should be able to call ResearchOS through stable intents and APIs.
- ResearchOS should be useful locally on one phone before becoming a server-side study controller.
- New functionality should fit the architecture rather than require architectural redesign.
- The platform should remain open, modular and interoperable.

---

Contributing

Contributions are welcome.

The preferred approach is to add new functionality as reusable ResearchOS Methods, Intents, Device Services or Protocol objects that integrate cleanly with the existing runtime rather than as application-specific features.

As the platform evolves, maintaining architectural consistency is considered as important as adding new functionality.