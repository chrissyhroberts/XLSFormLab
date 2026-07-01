# ResearchOS Architecture

ResearchOS is a layered platform for executing and orchestrating digital research workflows.

This document provides a high-level overview of the platform. It does not replace **Architecture Standard v1.00 (AS1.00)**, which remains the normative specification for the ResearchOS runtime.

---

## Platform Stack

```text
Research Protocol
        │
        ▼
Protocol Definition Language (PDL)
        │
        ▼
ResearchOS Orchestrator
        │
        ▼
ResearchOS Intent Language (RIL)
        │
        ▼
Architecture Standard (AS1.00)
        │
        ▼
Platform Runtime
        │
        ▼
Android / iOS / Desktop / Server / Embedded
```

---

## Layer Responsibilities

| Layer | Responsibility |
|------|----------------|
| Research Protocol | Defines the scientific purpose and study design. |
| PDL | Defines workflow logic, schedules, branching and repetition. |
| Orchestrator | Executes protocol workflows and issues RIL requests. |
| RIL | Defines what ResearchOS is asked to do. |
| AS1.00 | Defines how ResearchOS executes work internally. |
| Platform Runtime | Provides operating-system and hardware services. |

---

## AS1.00 Runtime

AS1.00 defines the internal execution architecture of ResearchOS.

Its core concepts include:

- Knowledge
- Methods
- Signals
- Device Services
- Time
- Space
- Transformations
- Provenance

All executable research operations should be implemented as ResearchOS Methods rather than legacy capabilities.

---

## RIL

The ResearchOS Intent Language is the external request language of the platform.

RIL answers five questions:

```text
WHAT    should happen?
WHEN    should it happen?
WHERE   should it happen?
HOW     should it be governed?
RESULT  should be returned?
```

RIL is transport-independent. The same request may be carried by Android Intents, JSON, HTTP, QR codes, NFC, audio signalling or future transports.

---

## Orchestrator

The ResearchOS Orchestrator is responsible for protocol execution.

It handles:

- scheduling
- reminders
- task state
- protocol progress
- retries
- missed windows
- branching
- event-triggered actions

The Orchestrator does not perform research operations directly. It issues RIL requests, which are executed by the AS1.00 runtime.

---

## Design Principles

ResearchOS follows these principles:

- declarative requests;
- stable architecture;
- reusable Methods;
- transport independence;
- platform independence;
- explicit provenance;
- clear separation between orchestration and execution;
- device interaction through Device Services;
- graceful degradation where possible.

---

## Specification Family

ResearchOS is defined by a family of complementary specifications:

- `Architecture_Standard_v1.00.docx`
- `RIL_Core_Verbs_v0.01.md`
- `ResearchOS_Intent_Language_v0.03.md`
- future `RIL_JSON_Binding`
- future `RIL_Android_Intent_Binding`
- future `RIL_HTTP_Binding`
- future `Protocol_Definition_Language`

---

## Deprecated Terms

Older documentation may refer to a `Capability Layer`.

This term is deprecated.

Use:

```text
Method
```

for executable research operations, and:

```text
Device Service
```

for hardware or platform-specific service abstractions.
