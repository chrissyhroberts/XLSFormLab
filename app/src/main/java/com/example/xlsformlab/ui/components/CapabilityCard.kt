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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.xlsformlab.core.CapabilityOutput
import com.example.xlsformlab.intents.OdkIntentBuilder
import com.example.xlsformlab.settings.SettingsState

@Composable
fun CapabilityCard(
    capability: Capability,
    modifier: Modifier = Modifier
) {
    val settingsState = remember(capability.manifest.id) {
        SettingsState(capability.settings)
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

    var returnMode by remember {
        mutableStateOf("json")
    }

    var launchTarget by remember {
        mutableStateOf("appearance")
    }

    val returnPreview = formatReturnPreview(
        output = output,
        returnMode = returnMode
    )

    val appearancePreview = OdkIntentBuilder.buildAppearanceColumnValue(
        capability = capability,
        settingsState = settingsState,
        returnMode = returnMode
    )

    val intentColumnPreview = OdkIntentBuilder.buildIntentColumnValue(
        capability = capability,
        settingsState = settingsState,
        returnMode = returnMode
    )

    val androidIntentPreview = OdkIntentBuilder.buildAndroidIntentUri(
        capability = capability,
        settingsState = settingsState,
        returnMode = returnMode
    )

    val kotlinIntentPreview = OdkIntentBuilder.buildKotlinIntentSnippet(
        capability = capability,
        settingsState = settingsState,
        returnMode = returnMode
    )

    val launchPreview = when (launchTarget) {
        "appearance" -> appearancePreview
        "intent_column" -> intentColumnPreview
        "android_intent" -> androidIntentPreview
        "kotlin_intent" -> kotlinIntentPreview
        else -> appearancePreview
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Return format",
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Row {
                ModeButton("Single", "single", returnMode) { returnMode = it }
                Spacer(Modifier.width(6.dp))
                ModeButton("Fields", "fields", returnMode) { returnMode = it }
            }

            Row(
                modifier = Modifier.padding(top = 6.dp)
            ) {
                ModeButton("JSON", "json", returnMode) { returnMode = it }
                Spacer(Modifier.width(6.dp))
                ModeButton("Datapoints", "datapoints", returnMode) { returnMode = it }
            }
        }

        Text(
            text = "Launch target",
            modifier = Modifier.padding(top = 12.dp),
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            ModeButton("Appearance", "appearance", launchTarget) { launchTarget = it }
            Spacer(Modifier.width(6.dp))
            ModeButton("Intent column", "intent_column", launchTarget) { launchTarget = it }
        }

        Row(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            ModeButton("Android URI", "android_intent", launchTarget) { launchTarget = it }
            Spacer(Modifier.width(6.dp))
            ModeButton("Kotlin", "kotlin_intent", launchTarget) { launchTarget = it }
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

@Composable
private fun ModeButton(
    label: String,
    value: String,
    selectedValue: String,
    onSelected: (String) -> Unit
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

private fun formatReturnPreview(
    output: CapabilityOutput,
    returnMode: String
): String {
    return when (returnMode) {
        "single" -> output.fields.values.firstOrNull()?.toString() ?: ""
        "fields" -> output.fields.entries.joinToString("\n") { (key, value) ->
            "$key=$value"
        }
        "json" -> output.fields.entries.joinToString(
            prefix = "{\n",
            separator = ",\n",
            postfix = "\n}"
        ) { (key, value) ->
            "  \"$key\": ${formatJsonValue(value)}"
        }
        "datapoints" -> output.fields.entries.mapIndexed { index, entry ->
            "${index + 1},${entry.key},${entry.value}"
        }.joinToString("\n")
        else -> output.fields.toString()
    }
}

private fun formatJsonValue(value: Any?): String {
    return when (value) {
        null -> "null"
        is Number -> value.toString()
        is Boolean -> value.toString()
        else -> "\"${value.toString().replace("\"", "\\\"")}\""
    }
}
