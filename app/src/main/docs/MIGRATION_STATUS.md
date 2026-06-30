# ResearchOS Migration Status

This file records the current state of the AS1.00 migration.

## Native ResearchOS methods

- NFC Read
- NFC Write
- Calibrated Scale
- GPS / Locate Target

## Device services in use

- NFC device service
- Location device service

## Compatibility retained intentionally

- Android package name: `com.example.xlsformlab`
- Android intent package/action strings using `com.example.xlsformlab`
- Legacy `xlsformlab(...)` appearance parser alias

These remain for compatibility and should not be changed without a dedicated app identity migration.

## Cleanup status

- No source-level `Capability` terminology should remain.
- User-facing app name is ResearchOS.
- Theme name is ResearchOS.
- New provenance/output keys use ResearchOS naming.
- New method outputs should avoid XLSFormLab naming unless maintaining compatibility with existing data.

## Suggested next migrations

1. QR / Barcode Method
2. Camera Measurement Method
3. BLE Scanner Method
4. Protocol Completeness Method
5. Admin Fingerprint Method migration
