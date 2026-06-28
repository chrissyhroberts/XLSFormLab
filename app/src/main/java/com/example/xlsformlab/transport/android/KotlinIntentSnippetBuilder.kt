package com.example.xlsformlab.transport.android

import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.settings.SettingsState
import com.example.xlsformlab.transport.ReturnMode

object KotlinIntentSnippetBuilder {

    private const val action = "com.example.xlsformlab.RUN_CAPABILITY"
    private const val packageName = "com.example.xlsformlab"

    fun build(
        capability: Capability,
        settingsState: SettingsState,
        returnMode: ReturnMode
    ): String {
        val lines = mutableListOf<String>()

        lines += "Intent(\"$action\")"
        lines += "    .setPackage(\"$packageName\")"
        lines += "    .putExtra(\"capability_id\", \"${capability.manifest.id}\")"
        lines += "    .putExtra(\"return_mode\", \"${returnMode.id}\")"

        settingsState.asMap()
            .toSortedMap()
            .forEach { (key, value) ->
                lines += "    .putExtra(\"$key\", ${formatKotlinValue(value)})"
            }

        return lines.joinToString("\n")
    }

    private fun formatKotlinValue(value: Any?): String {
        return when (value) {
            null -> "null"
            is Boolean -> value.toString()
            is Number -> value.toString()
            else -> "\"${value.toString().replace("\"", "\\\"")}\""
        }
    }
}
