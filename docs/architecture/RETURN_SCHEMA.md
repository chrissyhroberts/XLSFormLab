# Return Schema

## Purpose

The return schema defines how capabilities return data to ODK or other clients.

## Return modes

### Field return

Field return is used when a module returns one or more simple values.

```kotlin
fields = mapOf(
    "score" to "73",
    "module_id" to "calibrated_scale"
)
```

### JSON return

JSON return is used for complex data.

```json
{
  "module_id": "calibrated_scale",
  "score": 73,
  "minimum": 0,
  "maximum": 100
}
```

## Standard fields

| Field | Meaning |
|---|---|
| `module_id` | Capability id |
| `module_version` | Capability version |
| `success` | Whether execution succeeded |
| `request_id` | Caller request id if supplied |

## Error returns

```json
{
  "success": false,
  "module_id": "calibrated_scale",
  "error_code": "invalid_setting",
  "error_message": "maximum must be greater than minimum"
}
```

## Metadata

Capabilities may include metadata such as:

- timestamp
- device model
- app version
- module version
- settings profile
- calibration state

Metadata should be documented by each module.
