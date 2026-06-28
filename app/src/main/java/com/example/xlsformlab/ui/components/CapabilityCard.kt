package com.example.xlsformlab.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.CapabilityOutputValidator
import com.example.xlsformlab.settings.CapabilitySetting
import com.example.xlsformlab.settings.SettingsRepository
import com.example.xlsformlab.settings.SettingsState
import com.example.xlsformlab.transport.LaunchConfigParser
import com.example.xlsformlab.transport.LaunchTarget
import com.example.xlsformlab.transport.OutputFormatter
import com.example.xlsformlab.transport.ReturnMode
import com.example.xlsformlab.transport.android.AndroidIntentUriBuilder
import com.example.xlsformlab.transport.android.KotlinIntentSnippetBuilder
import com.example.xlsformlab.transport.odk.OdkAppearanceBuilder
import com.example.xlsformlab.transport.odk.OdkIntentColumnBuilder

@Composable
fun CapabilityCard(
    capability: Capability,
    modifier: Modifier = Modifier
) {
    val settingsState = remember(capability.manifest.id) {
        SettingsState(
            settings = capability.settings,
            onValueChanged = { settingId, value ->
                SettingsRepository.save(
                    capabilityId = capability.manifest.id,
                    settingId = settingId,
                    value = value
                )
            }
        )
    }

    LaunchedEffect(capability.manifest.id) {
        val restoredSettings = SettingsRepository.load(
            capabilityId = capability.manifest.id,
            settings = capability.settings
        )
        settingsState.restore(restoredSettings)
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = capability.manifest.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = capability.manifest.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${capability.manifest.status} • v${capability.manifest.version}",
                style = MaterialTheme.typography.labelMedium
            )

            ExpandableSection(
                title = "Demo",
                initiallyExpanded = true
            ) {
                capability.Demo(
                    settingsState = settingsState
                )
            }

            ExpandableSection(
                title = "Settings"
            ) {
                SettingsRenderer(
                    settings = capability.settings,
                    settingsState = settingsState
                )
            }

            ExpandableSection(
                title = "Help"
            ) {
                capability.Help()
            }

            ExpandableSection(
                title = "Output"
            ) {
                CapabilityOutputPanel(
                    capability = capability,
                    settingsState = settingsState
                )
            }

            ExpandableSection(
                title = "Diagnostics"
            ) {
                Text("ID: ${capability.manifest.id}")
                Text("Version: ${capability.manifest.version}")
                Text("Category: ${capability.manifest.category}")
                Text("Status: ${capability.manifest.status}")
                Text("Settings: ${settingsState.asMap()}")
            }
        }
    }
}

@Composable
private fun CapabilityOutputPanel(
    capability: Capability,
    settingsState: SettingsState
) {
    val clipboardManager = LocalClipboardManager.current
    val output = capability.buildOutput(settingsState)
    val validation = CapabilityOutputValidator.validate(
        schema = capability.outputSchema,
        output = output
    )

    var returnMode by remember {
        mutableStateOf(ReturnMode.Json)
    }

    var launchTarget by remember {
        mutableStateOf(LaunchTarget.Appearance)
    }

    var pastedLaunchText by remember {
        mutableStateOf("")
    }

    var parserMessage by remember {
        mutableStateOf("")
    }

    val returnPreview = OutputFormatter.format(
        output = output,
        returnMode = returnMode
    )

    val launchPreview = when (launchTarget) {
        LaunchTarget.Appearance -> OdkAppearanceBuilder.build(
            capability = capability,
            settingsState = settingsState,
            returnMode = returnMode
        )

        LaunchTarget.IntentColumn -> OdkIntentColumnBuilder.build(
            capability = capability,
            settingsState = settingsState,
            returnMode = returnMode
        )

        LaunchTarget.AndroidIntentUri -> AndroidIntentUriBuilder.build(
            capability = capability,
            settingsState = settingsState,
            returnMode = returnMode
        )

        LaunchTarget.KotlinIntent -> KotlinIntentSnippetBuilder.build(
            capability = capability,
            settingsState = settingsState,
            returnMode = returnMode
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Output schema",
            fontWeight = FontWeight.Bold
        )

        if (capability.outputSchema.fields.isEmpty()) {
            Text(
                text = "No output schema declared.",
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            capability.outputSchema.fields.forEach { field ->
                Text(
                    text = "${field.id}: ${field.type}${if (field.required) " required" else " optional"}",
                    modifier = Modifier.padding(top = 2.dp),
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Text(
            text = if (validation.valid) {
                "Validation: OK"
            } else {
                "Validation: ${validation.messages.joinToString("; ")}"
            },
            modifier = Modifier.padding(top = 8.dp),
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Return format",
            modifier = Modifier.padding(top = 12.dp),
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Row {
                ModeButton(ReturnMode.Single.label, ReturnMode.Single, returnMode) { returnMode = it }
                Spacer(Modifier.width(6.dp))
                ModeButton(ReturnMode.Fields.label, ReturnMode.Fields, returnMode) { returnMode = it }
            }

            Row(
                modifier = Modifier.padding(top = 6.dp)
            ) {
                ModeButton(ReturnMode.Json.label, ReturnMode.Json, returnMode) { returnMode = it }
                Spacer(Modifier.width(6.dp))
                ModeButton(ReturnMode.Datapoints.label, ReturnMode.Datapoints, returnMode) { returnMode = it }
            }
        }

        Text(
            text = "Launch target",
            modifier = Modifier.padding(top = 12.dp),
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Row {
                ModeButton(LaunchTarget.Appearance.label, LaunchTarget.Appearance, launchTarget) { launchTarget = it }
                Spacer(Modifier.width(6.dp))
                ModeButton(LaunchTarget.IntentColumn.label, LaunchTarget.IntentColumn, launchTarget) { launchTarget = it }
            }

            Row(
                modifier = Modifier.padding(top = 6.dp)
            ) {
                ModeButton(LaunchTarget.AndroidIntentUri.label, LaunchTarget.AndroidIntentUri, launchTarget) { launchTarget = it }
                Spacer(Modifier.width(6.dp))
                ModeButton(LaunchTarget.KotlinIntent.label, LaunchTarget.KotlinIntent, launchTarget) { launchTarget = it }
            }
        }

        Text(
            text = "Import existing launch string",
            modifier = Modifier.padding(top = 12.dp),
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = pastedLaunchText,
            onValueChange = {
                pastedLaunchText = it
            },
            label = {
                Text("Paste appearance or intent")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            minLines = 2,
            maxLines = 5
        )

        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                val parsed = LaunchConfigParser.parse(pastedLaunchText)

                parserMessage = applyParsedLaunchConfig(
                    capability = capability,
                    settingsState = settingsState,
                    parsedCapabilityId = parsed.capabilityId,
                    parsedReturnMode = parsed.returnMode,
                    parsedSettings = parsed.settings
                )

                parsed.returnMode?.let {
                    returnMode = it
                }

                if (parsed.warnings.isNotEmpty()) {
                    parserMessage += " ${parsed.warnings.joinToString(" ")}"
                }
            }
        ) {
            Text("Apply pasted config")
        }

        if (parserMessage.isNotBlank()) {
            Text(
                text = parserMessage,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        Text(
            text = "Launch preview",
            modifier = Modifier.padding(top = 12.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = launchPreview,
            modifier = Modifier.padding(top = 4.dp),
            fontFamily = FontFamily.Monospace
        )

        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                clipboardManager.setText(
                    AnnotatedString(launchPreview)
                )
            }
        ) {
            Text("Copy launch preview")
        }

        Text(
            text = "Return preview",
            modifier = Modifier.padding(top = 12.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = returnPreview,
            modifier = Modifier.padding(top = 4.dp),
            fontFamily = FontFamily.Monospace
        )

        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                clipboardManager.setText(
                    AnnotatedString(returnPreview)
                )
            }
        ) {
            Text("Copy return preview")
        }
    }
}

private fun applyParsedLaunchConfig(
    capability: Capability,
    settingsState: SettingsState,
    parsedCapabilityId: String?,
    parsedReturnMode: ReturnMode?,
    parsedSettings: Map<String, String>
): String {
    if (parsedCapabilityId != null && parsedCapabilityId != capability.manifest.id) {
        return "Parsed capability '${parsedCapabilityId}' does not match '${capability.manifest.id}'."
    }

    var applied = 0
    var skipped = 0

    capability.settings.forEach { setting ->
        val value = parsedSettings[setting.id]

        if (value == null) {
            return@forEach
        }

        val success = applySettingValue(
            setting = setting,
            settingsState = settingsState,
            value = value
        )

        if (success) {
            applied += 1
        } else {
            skipped += 1
        }
    }

    val unknownCount = parsedSettings.keys
        .count { settingId ->
            capability.settings.none { it.id == settingId }
        }

    val returnModeText = if (parsedReturnMode != null) {
        " Return mode: ${parsedReturnMode.label}."
    } else {
        ""
    }

    return "Applied $applied setting(s). Skipped $skipped invalid value(s). Unknown setting(s): $unknownCount.$returnModeText"
}

private fun applySettingValue(
    setting: CapabilitySetting,
    settingsState: SettingsState,
    value: String
): Boolean {
    return when (setting) {
        is CapabilitySetting.BooleanSetting -> {
            when (value.lowercase()) {
                "true", "1", "yes" -> {
                    settingsState.setBoolean(setting.id, true)
                    true
                }

                "false", "0", "no" -> {
                    settingsState.setBoolean(setting.id, false)
                    true
                }

                else -> false
            }
        }

        is CapabilitySetting.IntSetting -> {
            value.toIntOrNull()?.let {
                settingsState.setInt(
                    setting.id,
                    it.coerceIn(
                        setting.minimum ?: Int.MIN_VALUE,
                        setting.maximum ?: Int.MAX_VALUE
                    )
                )
                true
            } ?: false
        }

        is CapabilitySetting.FloatSetting -> {
            value.toFloatOrNull()?.let {
                settingsState.setFloat(
                    setting.id,
                    it.coerceIn(
                        setting.minimum ?: -Float.MAX_VALUE,
                        setting.maximum ?: Float.MAX_VALUE
                    )
                )
                true
            } ?: false
        }

        is CapabilitySetting.TextSetting -> {
            settingsState.setString(setting.id, value)
            true
        }

        is CapabilitySetting.ChoiceSetting -> {
            if (setting.choices.contains(value)) {
                settingsState.setString(setting.id, value)
                true
            } else {
                false
            }
        }
    }
}

@Composable
private fun <T> ModeButton(
    label: String,
    value: T,
    selectedValue: T,
    onSelected: (T) -> Unit
) {
    Button(
        onClick = {
            onSelected(value)
        },
        enabled = value != selectedValue
    ) {
        Text(label)
    }
}
