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
import com.example.xlsformlab.settings.SettingsRepository
import com.example.xlsformlab.settings.SettingsState
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

    var returnMode by remember {
        mutableStateOf(ReturnMode.Json)
    }

    var launchTarget by remember {
        mutableStateOf(LaunchTarget.Appearance)
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
            text = "Return format",
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
