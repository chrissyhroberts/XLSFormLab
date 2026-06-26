# XLSForm Lab Capability Archetype

## 1. Capability identity

Every capability must define a stable identity.

```kotlin
id = "calibrated_scale"
name = "Calibrated Scale"
version = "1.0.0"
category = CapabilityCategory.Measurement
tags = listOf("pain", "vas", "scale", "measurement")
```

The `id` must be stable once released. New behaviour should be introduced through versioning, not by changing the meaning of an existing capability.

## 2. Manifest

Each capability provides a manifest describing what it is, where it appears, what it supports, and what it returns.

```kotlin
CapabilityManifest(
    id = "example_capability",
    name = "Example Capability",
    shortName = "Example",
    version = "1.0.0",
    description = "One sentence description of what this capability does.",
    categories = listOf(CapabilityCategory.Utilities),
    actions = listOf(...),
    returnModes = listOf(ReturnMode.Json, ReturnMode.Fields),
    supportsStandalone = true,
    supportsOdk = true
)
```

## 3. Settings schema

Settings define persistent admin defaults. These are used unless temporarily overridden by an ODK intent.

```kotlin
settings = listOf(
    TextSetting(
        id = "question",
        label = "Question text",
        defaultValue = "Enter question text"
    ),
    BooleanSetting(
        id = "show_numbers",
        label = "Show numbers",
        defaultValue = true
    ),
    ChoiceSetting(
        id = "orientation",
        label = "Orientation",
        defaultValue = "horizontal",
        choices = listOf("horizontal", "vertical")
    )
)
```

## 4. Intent override schema

Every setting may optionally be overridden by an incoming intent. Intent overrides are temporary and must not alter admin defaults.

```text
Admin default → effective setting
Intent override → effective setting for this call only
```

## 5. Demo mode

Every capability must provide a standalone demo mode, usable without ODK.

```kotlin
@Composable
fun Demo()
```

Demo mode should allow the developer or form designer to test the capability, adjust settings, inspect outputs and copy an XLSForm intent template.

## 6. Settings mode

Every capability must expose configurable defaults.

```kotlin
@Composable
fun Settings()
```

Where possible, settings should be rendered automatically from the settings schema rather than manually coded per module.

## 7. Help mode

Every capability must provide embedded help.

```kotlin
@Composable
fun Help()
```

Help should include what the capability does, accepted parameters, returned values and example XLSForm usage.

## 8. Execution contract

Every capability must accept a standard request and return a standard result.

```kotlin
fun execute(request: CapabilityRequest): CapabilityResult
```

The module should not deal directly with Android intent plumbing. The shell converts Android intents into `CapabilityRequest` objects.

## 9. Return contract

Each capability must declare and document its return values.

```kotlin
CapabilityResult(
    success = true,
    returnMode = ReturnMode.Fields,
    fields = mapOf("score" to "73"),
    json = "{\"score\":73,\"module_id\":\"calibrated_scale\"}"
)
```

## 10. Copy XLSForm intent

Every capability should provide a generated intent template.

```text
intent:#Intent;
action=com.example.xlsformlab.RUN_MODULE;
package=com.example.xlsformlab;
S.module_id=example_capability;
S.return_mode=fields;
end
```

This should be generated from the manifest and settings schema, not hand-written.

## 11. Developer diagnostics

Developer mode should expose module id, version, admin defaults, incoming intent parameters, effective settings, returned fields, returned JSON, generated XLSForm intent and validation warnings.

## 12. File structure

```text
modules/
  examplecapability/
    ExampleCapabilityModule.kt
    ExampleCapabilityManifest.kt
    ExampleCapabilityDemo.kt
    ExampleCapabilityHelp.kt
    ExampleCapabilityExecution.kt
    ExampleCapabilityReturnSchema.kt
```

## 13. Rule

The shell must not contain special-case logic for individual tools. If a feature cannot be represented through the capability archetype, the archetype should be improved rather than bypassed.
