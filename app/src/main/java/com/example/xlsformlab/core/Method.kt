package com.example.xlsformlab.core

import androidx.compose.runtime.Composable
import com.example.xlsformlab.settings.MethodSetting
import com.example.xlsformlab.settings.SettingsState

/**
 * A method is a transport-independent research instrument.
 *
 * It may be launched from ODK, demoed inside ResearchOS, or invoked by a future protocol runner.
 * Implementations should declare their manifest, settings, output schema, and a small UI/demo surface.
 */
interface Method {

    val manifest: MethodManifest

    val settings: List<MethodSetting>

    val outputSchema: MethodOutputSchema
        get() = MethodOutputSchema()

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
    ): MethodOutput {
        return MethodOutput()
    }

    /**
     * Legacy direct execution hook. Kept deliberately simple so existing modules continue to work.
     * Newer integrations should call MethodRuntime.execute so context, validation and provenance
     * are handled consistently.
     */
    fun execute(
        request: MethodRequest
    ): MethodResult {
        return MethodResult(
            success = true,
            fields = emptyMap()
        )
    }
}
