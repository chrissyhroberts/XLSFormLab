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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.CapabilityOutput
import com.example.xlsformlab.settings.SettingsState

private enum class OutputPreviewMode {
    Fields,
    Json
}

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
                OutputPreview(
                    output = capability.buildOutput(settingsState)
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
private fun OutputPreview(
    output: CapabilityOutput
) {
    var mode by remember {
        mutableStateOf(OutputPreviewMode.Fields)
    }

    val clipboardManager = LocalClipboardManager.current
    val previewText = when (mode) {
        OutputPreviewMode.Fields -> output.asFieldsPreview()
        OutputPreviewMode.Json -> output.asJsonPreview()
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            if (mode == OutputPreviewMode.Fields) {
                Button(onClick = { mode = OutputPreviewMode.Fields }) {
                    Text("Fields")
                }
            } else {
                OutlinedButton(onClick = { mode = OutputPreviewMode.Fields }) {
                    Text("Fields")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (mode == OutputPreviewMode.Json) {
                Button(onClick = { mode = OutputPreviewMode.Json }) {
                    Text("JSON")
                }
            } else {
                OutlinedButton(onClick = { mode = OutputPreviewMode.Json }) {
                    Text("JSON")
                }
            }
        }

        Text(
            text = previewText,
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = {
                clipboardManager.setText(
                    AnnotatedString(previewText)
                )
            },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Copy preview")
        }
    }
}

private fun CapabilityOutput.asFieldsPreview(): String {
    if (fields.isEmpty()) {
        return "No output fields"
    }

    return fields.entries.joinToString(separator = "\n") { (key, value) ->
        "$key = ${value ?: ""}"
    }
}

private fun CapabilityOutput.asJsonPreview(): String {
    if (fields.isEmpty()) {
        return "{}"
    }

    return fields.entries.joinToString(
        separator = ",\n",
        prefix = "{\n",
        postfix = "\n}"
    ) { (key, value) ->
        "  \"${key.escapeJson()}\": ${value.toJsonValue()}"
    }
}

private fun Any?.toJsonValue(): String {
    return when (this) {
        null -> "null"
        is Number -> this.toString()
        is Boolean -> this.toString()
        else -> "\"${this.toString().escapeJson()}\""
    }
}

private fun String.escapeJson(): String {
    return this
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
}
