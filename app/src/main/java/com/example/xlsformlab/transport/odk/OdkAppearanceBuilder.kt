package com.example.xlsformlab.transport.odk

import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.settings.SettingsState
import com.example.xlsformlab.transport.ReturnMode
import com.example.xlsformlab.transport.encodeTransportValue

object OdkAppearanceBuilder {

    fun build(
        capability: Capability,
        settingsState: SettingsState,
        returnMode: ReturnMode
    ): String {
        val parts = mutableListOf<String>()

        parts += "capability=${encodeTransportValue(capability.manifest.id)}"
        parts += "return_mode=${encodeTransportValue(returnMode.id)}"

        settingsState.asMap()
            .toSortedMap()
            .forEach { (key, value) ->
                parts += "${encodeTransportValue(key)}=${encodeTransportValue(value)}"
            }

        return "xlsformlab(${parts.joinToString(";")})"
    }
}
