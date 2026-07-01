# ResearchOS

ResearchOS is an open platform for building, executing and orchestrating digital research workflows.

It provides a stable execution runtime for research methods together with a platform-independent language for interoperability. ResearchOS is designed to complement existing electronic data capture systems rather than replace them, allowing specialised research operations to be shared across projects and platforms.

---

# Project Status

| Component | Status |
|-----------|--------|
| Architecture Standard (AS1.00) | ✅ Stable |
| Runtime 1.x | ✅ Implemented |
| Native Methods | ✅ Initial implementation |
| ResearchOS Intent Language (RIL v0.03) | ✅ Conceptually complete |
| JSON Binding | 🚧 Planned |
| Android Intent Binding | 🚧 Planned |
| ResearchOS Orchestrator | 🚧 Planned |
| Protocol Definition Language | 📋 Planned |

---

# Current Status

## ResearchOS Runtime 1.x

The first implementation of the **Architecture Standard v1.00 (AS1.00)** runtime is complete and provides the reference implementation of the ResearchOS execution model.

Implemented components include:

- AS1.00 execution engine
- Method registry
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

These demonstrate observation, intervention, measurement and sensor integration through a common execution model.

---

## Phase 2 – ResearchOS Intent Language (RIL)

The conceptual design of **RIL v0.03** is complete.

RIL is a platform-independent language for requesting research operations. Every request is expressed using five independent concerns:

- **WHAT** – requested actions
- **WHEN** – temporal behaviour
- **WHERE** – spatial constraints
- **HOW** – execution policies
- **RESULT** – returned information

### Design Philosophy

ResearchOS separates **language** from **transport**.

A RIL request has the same meaning regardless of whether it is transmitted using Android Intents, JSON, HTTP APIs, URLs, QR codes, NFC tags or future transport mechanisms.

Bindings define how requests are represented.

RIL defines what those requests mean.

---

## Current Focus

Development is now moving from language design to implementation.

Current priorities are:

- RIL JSON Binding v0.01
- Android Intent Binding
- HTTP Binding
- Core Resources Specification
- Core Types Specification
- Core Policies Specification

These specifications will provide concrete representations of RIL while preserving a single platform-independent language.

---

## Phase 3 – ResearchOS Orchestrator

The ResearchOS Orchestrator will execute research protocols by scheduling and issuing RIL requests over time.

Responsibilities include:

- protocol scheduling
- participant timelines
- reminders and notifications
- workflow progression
- retries and missed windows
- protocol branching
- event-driven execution

Example workflow:

1. A participant enrols by scanning a QR code.
2. The QR code installs a personalised protocol.
3. The Orchestrator schedules future activities.
4. Notifications launch the appropriate application or web form.
5. External systems request specialised operations using RIL.
6. ResearchOS returns requested data together with configurable provenance.
7. Participants and researchers can monitor protocol progress throughout the study.

---

## Interoperability

ResearchOS is designed as a companion platform for existing research ecosystems.

Systems such as ODK, KoBoToolbox, web applications and future electronic data capture platforms can invoke ResearchOS through RIL while continuing to use their own data models.

ResearchOS maintains canonical execution records, methods and provenance internally while allowing callers to define their own field names and output mappings.

---

## Specification Family

The platform is defined by a family of complementary specifications:

- Architecture Standard (AS1.00)
- ResearchOS Intent Language (RIL)
- Core Verbs Specification
- Core Resources Specification *(planned)*
- Core Types Specification *(planned)*
- Core Policies Specification *(planned)*
- Protocol Definition Language *(planned)*
- RIL JSON Binding *(planned)*
- Android Intent Binding *(planned)*
- HTTP Binding *(planned)*
- ResearchOS Orchestrator Specification *(planned)*

Together these specifications define the ResearchOS platform independently of any specific programming language, operating system or transport mechanism.
