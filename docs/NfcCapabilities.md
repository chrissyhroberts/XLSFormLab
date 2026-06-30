# NFC capabilities

This implementation treats NFC as a ResearchOS/XLSForm Lab capability pair, not as an ODK module.

## Hard boundaries

- The NFC capabilities do not know about ODK, XLSForm appearance syntax, return intents, or transport-specific field mapping.
- Transport remains the responsibility of the XLSForm Lab shell and transport layer.
- NFC read produces an `Observation` plus an immutable NFC tag `Artifact`.
- NFC write produces an `Intervention` plus a post-write `Observation` and `Artifact`.
- Formatting non-NDEF tags is deliberately not part of the write capability. It can be added later as a platform operation.

## Implemented layers

```text
Device    Android NFC adapter / Android Tag / Ndef technology
Signal    tag discovered, UID, tech list, NDEF message and NDEF state
Capture   instant/manual NFC capture through reader mode
Capability nfc_tag_read and nfc_tag_write
Evidence  Observation for read; Intervention plus post-write observation for write
Artifact  immutable structured NFC tag JSON artifact
Transport handled outside this module
```

## Read capability

`nfc_tag_read` exposes standard tag datapoints:

- `tag_uid_hex`
- `tag_uid_dec`
- `tech_list`
- `ndef_supported`
- `ndef_message_size_bytes`
- `ndef_max_size_bytes`
- `ndef_is_writable`
- `ndef_can_make_read_only`
- `ndef_record_count`
- `ndef_text`
- `ndef_uri`
- `ndef_mime_types`
- `ndef_external_types`
- `ndef_payload_hex_all`
- `ndef_payload_utf8_all`
- `ndef_first_payload_hex`
- `ndef_first_payload_utf8`
- `ndef_records_json`
- `tag_summary`

It also exposes ResearchOS envelope fields:

- evidence semantics
- provenance JSON
- capture outcome JSON
- quality JSON
- validation JSON
- artifact JSON
- evidence JSON
- execution JSON

## Write capability

`nfc_tag_write` accepts text, URI, MIME and external NDEF records through capability settings/runtime context. It returns:

- write success
- write message
- write record type
- write size
- intervention JSON
- post-write evidence JSON
- the standard post-write tag fields

## Measurement semantics

NFC tag identity is an identifying point observation:

```text
temporalSemantics = PointObservation
aggregationSemantics = IdentifyingOnly
lineage = DeviceReported
```

The write action is an intervention/action outcome, not a measurement:

```text
action = nfc.ndef.write
lineage = ActionOutcome
```
