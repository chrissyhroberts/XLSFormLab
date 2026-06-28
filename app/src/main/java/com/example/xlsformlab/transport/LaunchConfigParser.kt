package com.example.xlsformlab.transport

import java.net.URLDecoder

data class ParsedLaunchConfig(
    val capabilityId: String?,
    val returnMode: ReturnMode?,
    val settings: Map<String, String>,
    val warnings: List<String> = emptyList()
)

object LaunchConfigParser {

    fun parse(
        text: String
    ): ParsedLaunchConfig {
        val trimmed = text.trim()

        return when {
            trimmed.startsWith("xlsformlab(") && trimmed.endsWith(")") ->
                parseAppearance(trimmed)

            trimmed.startsWith("intent:#Intent") ->
                parseAndroidIntentUri(trimmed)

            else ->
                ParsedLaunchConfig(
                    capabilityId = null,
                    returnMode = null,
                    settings = emptyMap(),
                    warnings = listOf("Input was not recognised as an XLSForm Lab appearance or Android intent URI.")
                )
        }
    }

    private fun parseAppearance(
        text: String
    ): ParsedLaunchConfig {
        val inside = text
            .removePrefix("xlsformlab(")
            .removeSuffix(")")

        val values = parseKeyValueParts(
            parts = inside.split(";"),
            androidPrefixes = false
        )

        return ParsedLaunchConfig(
            capabilityId = values["capability"],
            returnMode = values["return_mode"]?.let { ReturnMode.fromId(it) },
            settings = values
                .filterKeys {
                    it != "capability" && it != "return_mode"
                }
        )
    }

    private fun parseAndroidIntentUri(
        text: String
    ): ParsedLaunchConfig {
        val values = parseKeyValueParts(
            parts = text.split(";"),
            androidPrefixes = true
        )

        return ParsedLaunchConfig(
            capabilityId = values["capability_id"],
            returnMode = values["return_mode"]?.let { ReturnMode.fromId(it) },
            settings = values
                .filterKeys {
                    it != "capability_id" && it != "return_mode"
                }
        )
    }

    private fun parseKeyValueParts(
        parts: List<String>,
        androidPrefixes: Boolean
    ): Map<String, String> {
        val values = mutableMapOf<String, String>()

        parts.forEach { rawPart ->
            val part = rawPart.trim()

            if (!part.contains("=")) {
                return@forEach
            }

            val key = part.substringBefore("=")
            val value = part.substringAfter("=")

            val normalisedKey = if (androidPrefixes && key.length > 2 && key[1] == '.') {
                key.substring(2)
            } else {
                key
            }

            values[decode(normalisedKey)] = decode(value)
        }

        return values
    }

    private fun decode(
        value: String
    ): String {
        return URLDecoder.decode(value, "UTF-8")
    }
}
