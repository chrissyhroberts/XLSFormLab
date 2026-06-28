# XLSForm Lab

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
