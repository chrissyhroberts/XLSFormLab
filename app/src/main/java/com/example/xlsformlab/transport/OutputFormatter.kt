package com.example.xlsformlab.transport

import com.example.xlsformlab.core.CapabilityOutput

object OutputFormatter {

    fun format(
        output: CapabilityOutput,
        returnMode: ReturnMode
    ): String {
        return when (returnMode) {
            ReturnMode.Single -> output.fields.values.firstOrNull()?.toString() ?: ""

            ReturnMode.Fields -> output.fields.entries.joinToString("\n") { (key, value) ->
                "$key=$value"
            }

            ReturnMode.Json -> output.fields.entries.joinToString(
                prefix = "{\n",
                separator = ",\n",
                postfix = "\n}"
            ) { (key, value) ->
                "  \"$key\": ${formatJsonValue(value)}"
            }

            ReturnMode.Datapoints -> output.fields.entries.mapIndexed { index, entry ->
                "${index + 1},${entry.key},${entry.value}"
            }.joinToString("\n")
        }
    }

    private fun formatJsonValue(value: Any?): String {
        return when (value) {
            null -> "null"
            is Number -> value.toString()
            is Boolean -> value.toString()
            else -> "\"${value.toString().replace("\"", "\\\"")}\""
        }
    }
}
