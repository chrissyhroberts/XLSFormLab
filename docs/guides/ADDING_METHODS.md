# Adding a ResearchOS Method

This guide describes the preferred pattern for adding a new AS1.00-native ResearchOS method.

## 1. Define the method

Create a method class in the relevant module package. Prefer a name that describes the research operation:

- `ObserveNfcTagMethod`
- `WriteNfcTagMethod`
- `LocateTargetMethod`
- `CalibratedScaleMethod`
- `ScanBarcodeMethod`

The method should own method logic, validation, transformation and output construction. It should not own Compose UI or direct Android device APIs.

## 2. Define signals

If the method uses device or environmental input, create a signal model. Signals are transient and should describe what the device or service observed, not what the research conclusion is.

Examples:

- NFC tag signal
- Location fix signal
- Manual measurement signal
- Camera frame / barcode signal

## 3. Add or reuse a Device Service

Device Services adapt Android or hardware APIs into ResearchOS signals or effects.

Examples:

- Android NFC service
- Android Location service
- Future camera service
- Future BLE service

A method should consume a signal, not directly consume an Android `Tag`, `Location`, `SensorEvent` or camera object.

## 4. Register the method

Add the method to `As100MethodRegistry` so it can be discovered and executed by the runtime.

## 5. Keep presentation separate

Compose screens should live in `presentation/` or UI packages and call into the method/runtime. They should not contain the research method logic.

## 6. Preserve compatibility deliberately

If migrating an older module, keep a thin compatibility wrapper only until nothing depends on it. Do not add new compatibility wrappers for new functionality.

## Migration checklist

- [ ] Method class exists and has a clear method-oriented name.
- [ ] Method is registered in the AS1.00 method registry.
- [ ] Device interaction is behind a Device Service.
- [ ] Input from hardware is represented as a Signal.
- [ ] Outputs are explicit and include provenance where appropriate.
- [ ] Compose UI is not embedded in the method class.
- [ ] No new `Capability` terminology has been introduced.
