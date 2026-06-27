package com.example.xlsformlab.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.settings.CapabilitySetting
import com.example.xlsformlab.settings.SettingsState

@Composable
fun SettingsRenderer(
    settings: List<CapabilitySetting>,
    settingsState: SettingsState
) {
    Column {
        settings.forEach { setting ->
            when (setting) {
                is CapabilitySetting.TextSetting -> {
                    OutlinedTextField(
                        value = settingsState.getString(setting.id),
                        onValueChange = { settingsState.setString(setting.id, it) },
                        label = { Text(setting.label) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }

                is CapabilitySetting.BooleanSetting -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(setting.label)

                        Switch(
                            checked = settingsState.getBoolean(setting.id),
                            onCheckedChange = { settingsState.setBoolean(setting.id, it) }
                        )
                    }
                }

                is CapabilitySetting.FloatSetting -> {
                    Text("${setting.label}: ${"%.1f".format(settingsState.getFloat(setting.id))}")

                    Slider(
                        value = settingsState.getFloat(setting.id),
                        onValueChange = { settingsState.setFloat(setting.id, it) },
                        valueRange = (setting.minimum ?: 0f)..(setting.maximum ?: 100f)
                    )
                }

                is CapabilitySetting.IntSetting -> {
                    Text("${setting.label}: ${settingsState.getInt(setting.id)}")

                    Slider(
                        value = settingsState.getInt(setting.id).toFloat(),
                        onValueChange = { settingsState.setInt(setting.id, it.toInt()) },
                        valueRange = (setting.minimum ?: 0).toFloat()..
                            (setting.maximum ?: 100).toFloat()
                    )
                }

                is CapabilitySetting.ChoiceSetting -> {
                    Text("${setting.label}: ${settingsState.getString(setting.id)}")
                    Text(
                        setting.choices.joinToString(" • "),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}