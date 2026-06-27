package com.example.xlsformlab.core

import androidx.compose.runtime.Composable
import com.example.xlsformlab.settings.CapabilitySetting
import com.example.xlsformlab.settings.SettingsState

interface Capability {

    val manifest: CapabilityManifest

    val settings: List<CapabilitySetting>

    @Composable
    fun Demo(
        settingsState: SettingsState
    )

    @Composable
    fun Help()

    fun execute(
        request: CapabilityRequest
    ): CapabilityResult
}