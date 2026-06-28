package com.example.xlsformlab.core

import androidx.compose.runtime.Composable
import com.example.xlsformlab.settings.CapabilitySetting
import com.example.xlsformlab.settings.SettingsState

interface Capability {

    val manifest: CapabilityManifest

    val settings: List<CapabilitySetting>

    val outputSchema: CapabilityOutputSchema
        get() = CapabilityOutputSchema()

    @Composable
    fun Demo(
        settingsState: SettingsState
    )

    @Composable
    fun Help()

    fun buildOutput(
        settingsState: SettingsState
    ): CapabilityOutput {
        return CapabilityOutput()
    }

    fun execute(
        request: CapabilityRequest
    ): CapabilityResult
}
