package com.example.xlsformlab.transport.odk

import com.example.xlsformlab.core.Method
import com.example.xlsformlab.settings.SettingsState
import com.example.xlsformlab.transport.ReturnMode
import com.example.xlsformlab.transport.encodeTransportValue

object OdkAppearanceBuilder {

    fun build(
        method: Method,
        settingsState: SettingsState,
        returnMode: ReturnMode
    ): String {
        val parts = mutableListOf<String>()

        parts += "method=${encodeTransportValue(method.manifest.id)}"
        parts += "return_mode=${encodeTransportValue(returnMode.id)}"

        settingsState.asMap()
            .toSortedMap()
            .forEach { (key, value) ->
                parts += "${encodeTransportValue(key)}=${encodeTransportValue(value)}"
            }

        return "researchos(${parts.joinToString(";")})"
    }
}
