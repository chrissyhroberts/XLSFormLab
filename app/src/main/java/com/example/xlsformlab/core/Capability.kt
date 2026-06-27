package com.example.xlsformlab.core

import androidx.compose.runtime.Composable
import com.example.xlsformlab.settings.CapabilitySetting

interface Capability {

    val manifest: CapabilityManifest

    val settings: List<CapabilitySetting>

    @Composable
    fun Demo()

    @Composable
    fun Settings()

    @Composable
    fun Help()

    fun execute(
        request: CapabilityRequest
    ): CapabilityResult
}