# Settings Schema

## Purpose

The settings schema defines persistent admin defaults and temporary intent-overridable parameters for capabilities.

Settings are declarative. The same schema should be used to:

- render settings UI
- validate values
- store defaults
- parse intent overrides
- generate documentation
- generate example XLSForm intents

## Setting types

### BooleanSetting

```kotlin
BooleanSetting(
    id = "show_numbers",
    label = "Show numbers",
    defaultValue = true
)
```

### IntegerSetting

```kotlin
IntegerSetting(
    id = "maximum_contacts",
    label = "Maximum contacts",
    defaultValue = 20,
    min = 1,
    max = 500
)
```

### FloatSetting

```kotlin
FloatSetting(
    id = "line_length_mm",
    label = "Displayed line length",
    defaultValue = 100f,
    min = 20f,
    max = 300f
)
```

### TextSetting

```kotlin
TextSetting(
    id = "question",
    label = "Question text",
    defaultValue = "Enter question text"
)
```

### ChoiceSetting

```kotlin
ChoiceSetting(
    id = "orientation",
    label = "Orientation",
    defaultValue = "horizontal",
    choices = listOf("horizontal", "vertical")
)
```

### MultiChoiceSetting

Used when more than one option may be enabled.

### ColourSetting

Used for module colours such as marker colours or overlay colours.

### JsonSetting

Used for complex nested configuration.

## Locks

Settings may be locked by the caller or administrator.

```text
orientation = vertical
orientation_locked = true
```

A locked setting may be displayed but not changed in runtime UI.

## Persistence

Admin settings are stored locally on the device. Intent overrides do not modify stored settings.

## Validation

Every setting should validate:

- type
- allowed range
- allowed choices
- required status
- dependency on other settings

Invalid settings should generate structured warnings or errors.
