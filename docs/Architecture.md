# Architecture

## Layers

Platform
- Device Layer
- Signal Layer
- Capture Layer
- Capability Layer
- Transport Layer

## Device Layer
Abstracts Android hardware and external plugins (USB, BLE, MQTT, ESP32, etc.).

## Signal Layer
Normalises raw and derived signals independent of source.

## Capture Layer
Implements reusable capture strategies:
- Instant
- Manual
- Average
- Stable
- Threshold
- Trigger
- Continuous

Supports logical conditions, timeouts, overrides and provenance.

## Capability Layer
Two families:
- Measurement capabilities (GPS, light, sound, scales)
- Interaction capabilities (DCEs, body maps, timelines, lesion mapping)

## Transport Layer
ODK intents, appearance syntax, JSON, Kotlin and future transports.

## Design Principles
- Declarative
- Transparent
- Composable
- Plugin-first
- Transport-independent
- Research-grade provenance
- Graceful degradation
