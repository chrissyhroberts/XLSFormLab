# JSON Returns

## Purpose

JSON returns support complex capability outputs.

## Example

```json
{
  "success": true,
  "module_id": "calibrated_scale",
  "module_version": "1.0.0",
  "mode": "single",
  "value": 73,
  "settings": {
    "minimum": 0,
    "maximum": 100,
    "orientation": "horizontal"
  }
}
```

## Guidelines

- include `success`
- include `module_id`
- include `module_version`
- include selected values
- include useful metadata
- avoid unnecessary personally identifiable information
