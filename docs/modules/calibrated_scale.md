# Calibrated Scale Module

## Purpose

The calibrated scale module provides a configurable visual analogue or numeric scale for ODK workflows.

## Core functions

- single value selection
- least/most range selection
- horizontal or vertical scale
- adjustable displayed line length
- configurable minimum and maximum
- configurable subdivisions
- optional number labels
- copyable XLSForm intent

## Settings

| Setting | Type | Example |
|---|---|---|
| `question` | Text | Rate your pain |
| `minimum` | Float | 0 |
| `maximum` | Float | 100 |
| `orientation` | Choice | horizontal |
| `show_numbers` | Boolean | true |
| `line_length_mm` | Float | 100 |
| `mode` | Choice | single |
| `least_label` | Text | Least pain |
| `most_label` | Text | Worst pain |

## Return fields

| Field | Meaning |
|---|---|
| `value` | selected value in single mode |
| `least_value` | lower selected value in range mode |
| `most_value` | upper selected value in range mode |
| `mode` | single or range |

## Example intent

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
