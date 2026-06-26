# Adding a Capability

## Step 1: Create a module folder

```text
app/src/main/java/com/example/xlsformlab/modules/mycapability/
```

## Step 2: Define the manifest

The manifest describes the capability.

```kotlin
val myCapabilityManifest = CapabilityManifest(
    id = "my_capability",
    name = "My Capability",
    shortName = "Mine",
    version = "1.0.0",
    description = "What the capability does.",
    categories = listOf(CapabilityCategory.Utilities),
    actions = listOf(...),
    returnModes = listOf(ReturnMode.Fields, ReturnMode.Json)
)
```

## Step 3: Define settings

Declare admin defaults using the settings schema.

## Step 4: Implement demo

Every capability must have a standalone demo.

## Step 5: Implement execution

Convert effective settings into a result.

## Step 6: Define returns

Document returned fields and JSON.

## Step 7: Add help

Help should be embedded and accessible offline.

## Step 8: Register the module

Add the module to the registry.

## Step 9: Test

Confirm:

- app builds
- module appears in explorer
- demo works
- settings render
- example intent is generated
- return values match schema
