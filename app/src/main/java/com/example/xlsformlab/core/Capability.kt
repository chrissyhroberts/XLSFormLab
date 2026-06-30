package com.example.xlsformlab.core

import androidx.compose.runtime.Composable
import com.example.xlsformlab.settings.CapabilitySetting
import com.example.xlsformlab.settings.SettingsState

/**
 * A capability is a transport-independent research instrument.
 *
 * It may be launched from ODK, demoed inside XLSForm Lab, or invoked by a future protocol runner.
 * Implementations should declare their manifest, settings, output schema, and a small UI/demo surface.
 */
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

    /**
     * Preview or deterministic output generated from settings. Used by demo and launch builders.
     */
    fun buildOutput(
        settingsState: SettingsState
    ): CapabilityOutput {
        return CapabilityOutput()
    }

    /**
     * Legacy direct execution hook. Kept deliberately simple so existing modules continue to work.
     * Newer integrations should call CapabilityRuntime.execute so context, validation and provenance
     * are handled consistently.
     */
    fun execute(
        request: CapabilityRequest
    ): CapabilityResult {
        return CapabilityResult(
            success = true,
            fields = emptyMap()
        )
    }
}
