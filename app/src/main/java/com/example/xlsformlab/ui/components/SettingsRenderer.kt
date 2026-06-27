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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.settings.CapabilitySetting

@Composable
fun SettingsRenderer(
    settings: List<CapabilitySetting>
) {
    Column {

        settings.forEach { setting ->

            when (setting) {

                is CapabilitySetting.TextSetting -> {

                    val value = remember {
                        mutableStateOf(setting.defaultValue)
                    }

                    OutlinedTextField(
                        value = value.value,
                        onValueChange = { value.value = it },
                        label = { Text(setting.label) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }

                is CapabilitySetting.BooleanSetting -> {

                    val checked = remember {
                        mutableStateOf(setting.defaultValue)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(setting.label)

                        Switch(
                            checked = checked.value,
                            onCheckedChange = {
                                checked.value = it
                            }
                        )
                    }
                }

                is CapabilitySetting.FloatSetting -> {

                    val value = remember {
                        mutableFloatStateOf(setting.defaultValue)
                    }

                    Text("${setting.label}: ${"%.1f".format(value.floatValue)}")

                    Slider(
                        value = value.floatValue,
                        onValueChange = {
                            value.floatValue = it
                        },
                        valueRange = (setting.minimum ?: 0f)..(setting.maximum ?: 100f)
                    )
                }

                is CapabilitySetting.IntSetting -> {

                    val value = remember {
                        mutableIntStateOf(setting.defaultValue)
                    }

                    Text("${setting.label}: ${value.intValue}")

                    Slider(
                        value = value.intValue.toFloat(),
                        onValueChange = {
                            value.intValue = it.toInt()
                        },
                        valueRange = (setting.minimum ?: 0).toFloat()..
                                     (setting.maximum ?: 100).toFloat()
                    )
                }

                is CapabilitySetting.ChoiceSetting -> {

                    Text("${setting.label}: ${setting.defaultValue}")

                    Text(
                        setting.choices.joinToString(" • "),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}