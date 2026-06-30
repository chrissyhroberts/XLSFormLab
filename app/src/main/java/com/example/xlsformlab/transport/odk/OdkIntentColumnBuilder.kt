package com.example.xlsformlab.transport.odk

import com.example.xlsformlab.core.Method
import com.example.xlsformlab.settings.SettingsState
import com.example.xlsformlab.transport.ReturnMode
import com.example.xlsformlab.transport.android.AndroidIntentUriBuilder

object OdkIntentColumnBuilder {

    fun build(
        method: Method,
        settingsState: SettingsState,
        returnMode: ReturnMode
    ): String {
        return AndroidIntentUriBuilder.build(
            method = method,
            settingsState = settingsState,
            returnMode = returnMode
        )
    }
}
