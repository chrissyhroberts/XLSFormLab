package com.example.xlsformlab.transport.odk

import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.settings.SettingsState
import com.example.xlsformlab.transport.ReturnMode
import com.example.xlsformlab.transport.android.AndroidIntentUriBuilder

object OdkIntentColumnBuilder {

    fun build(
        capability: Capability,
        settingsState: SettingsState,
        returnMode: ReturnMode
    ): String {
        return AndroidIntentUriBuilder.build(
            capability = capability,
            settingsState = settingsState,
            returnMode = returnMode
        )
    }
}
