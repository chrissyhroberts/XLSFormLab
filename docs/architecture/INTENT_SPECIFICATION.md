# Intent Specification

## Purpose

The intent specification defines how external clients, especially ODK Collect, invoke XLSForm Lab capabilities.

## Standard action

```text
com.example.xlsformlab.RUN_MODULE
```

## Required extras

| Extra | Type | Meaning |
|---|---|---|
| `module_id` | String | Stable capability id |
| `return_mode` | String | `fields` or `json` |

## Optional standard extras

| Extra | Type | Meaning |
|---|---|---|
| `module_version` | String | Requested module version |
| `action_id` | String | Specific action inside module |
| `request_id` | String | Caller-provided request identifier |
| `profile` | String | Named settings profile |
| `debug` | Boolean | Enable diagnostic output |

## Setting overrides

Each module setting may be overridden using intent extras. Android intent type prefixes should be used consistently:

| Prefix | Type |
|---|---|
| `S.` | String |
| `B.` | Boolean |
| `i.` | Integer |
| `f.` | Float |
| `d.` | Double |

Example:

```text
intent:#Intent;
action=com.example.xlsformlab.RUN_MODULE;
package=com.example.xlsformlab;
S.module_id=calibrated_scale;
S.return_mode=fields;
S.question=Rate your pain;
f.minimum=0;
f.maximum=100;
S.orientation=horizontal;
B.show_numbers=true;
end
```

## Effective settings

The engine builds effective settings in this order:

1. Module built-in defaults
2. Stored admin defaults
3. Named profile, if provided
4. Intent overrides

Only layer 2 is persistent. Layers 3 and 4 are temporary for the current invocation.

## Error handling

If a request is invalid, the capability should return a structured error rather than crashing.

Standard error fields:

| Field | Meaning |
|---|---|
| `success` | false |
| `error_code` | Stable machine-readable code |
| `error_message` | Human-readable message |
| `module_id` | Module that produced the error |
| `request_id` | Request id if supplied |

## Version negotiation

If `module_version` is omitted, the latest compatible version should be used. If a requested version is unavailable, the engine should return a version error with available versions.
