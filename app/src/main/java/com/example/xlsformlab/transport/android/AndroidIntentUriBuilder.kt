package com.example.xlsformlab.transport.android

import com.example.xlsformlab.core.Method
import com.example.xlsformlab.settings.SettingsState
import com.example.xlsformlab.transport.ReturnMode
import com.example.xlsformlab.transport.androidExtraPrefix
import com.example.xlsformlab.transport.encodeTransportValue

object AndroidIntentUriBuilder {

    private const val action = "com.example.xlsformlab.RUN_METHOD"
    private const val packageName = "com.example.xlsformlab"

    fun build(
        method: Method,
        settingsState: SettingsState,
        returnMode: ReturnMode
    ): String {
        val parts = mutableListOf<String>()

        parts += "intent:#Intent"
        parts += "action=$action"
        parts += "package=$packageName"
        parts += "S.method_id=${encodeTransportValue(method.manifest.id)}"
        parts += "S.return_mode=${encodeTransportValue(returnMode.id)}"

        settingsState.asMap()
            .toSortedMap()
            .forEach { (key, value) ->
                parts += "${androidExtraPrefix(value)}.$key=${encodeTransportValue(value)}"
            }

        parts += "end"

        return parts.joinToString(";")
    }
}
