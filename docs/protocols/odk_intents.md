# ODK Intent Protocol

## Basic pattern

ODK launches XLSForm Lab using an Android intent.

```text
intent:#Intent;
action=com.example.xlsformlab.RUN_MODULE;
package=com.example.xlsformlab;
S.module_id=calibrated_scale;
S.return_mode=fields;
end
```

## Temporary overrides

ODK may include settings overrides.

```text
S.question=Rate your pain
f.minimum=0
f.maximum=100
B.show_numbers=true
```

These overrides apply only to the current invocation.

## Return modes

- `fields` for simple values
- `json` for complex outputs

## Recommended XLSForm pattern

Use a calculate or external app appearance to call the intent and store returned values in dedicated fields.
