# Getting Started

## Requirements

- Android Studio
- Kotlin
- Jetpack Compose
- Android SDK
- Git

## Project principle

Every feature should be implemented as a capability module.

## First build

1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the app on an emulator.
4. Confirm the home screen loads.

## First contribution

Start by adding a capability scaffold:

```text
modules/
  mycapability/
    MyCapabilityModule.kt
    MyCapabilityManifest.kt
    MyCapabilityDemo.kt
    MyCapabilityHelp.kt
```

Register the module in the capability registry.

## Rule

Every commit must compile.
