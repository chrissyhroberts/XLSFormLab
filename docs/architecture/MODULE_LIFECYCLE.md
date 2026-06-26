# Module Lifecycle

## Lifecycle states

```text
Installed
  ↓
Registered
  ↓
Configured
  ↓
Available
  ↓
Demo or Intent Invocation
  ↓
Effective Settings Built
  ↓
Validated
  ↓
Executed
  ↓
Result Returned
  ↓
Diagnostics Logged
  ↓
Idle
```

## Installed

The module exists in the app source and is packaged into the APK.

## Registered

The module is listed in the capability registry.

## Configured

Admin defaults are loaded or initialised.

## Available

The module appears in the capability explorer and can be run in demo mode or invoked by intent.

## Demo invocation

The user opens the module from the shell UI. Demo mode should not require ODK.

## Intent invocation

An external client invokes the module using the standard intent protocol.

## Effective settings

The engine combines module defaults, admin defaults, named profiles and temporary intent overrides.

## Validation

The engine and module validate settings before execution.

## Execution

The module performs its core function.

## Result return

The module returns a `CapabilityResult`, which is serialized to the requested return mode.

## Diagnostics

Developer mode may expose request, effective settings, result and warnings.
