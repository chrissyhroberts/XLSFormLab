package com.example.xlsformlab.intents

import com.example.xlsformlab.core.Method
import com.example.xlsformlab.settings.SettingsState
import java.net.URLEncoder

object OdkIntentBuilder {

    fun buildAppearanceColumnValue(
        method: Method,
        settingsState: SettingsState,
        returnMode: String
    ): String {
        val settings = settingsState.asMap()
            .toSortedMap()
            .map { (key, value) ->
                "$key=${encode(value.toString())}"
            }
            .joinToString(";")

        return "xlsformlab(method=${method.manifest.id};return_mode=$returnMode;$settings)"
    }

    fun buildIntentColumnValue(
        method: Method,
        settingsState: SettingsState,
        returnMode: String
    ): String {
        return buildAndroidIntentUri(
            method = method,
            settingsState = settingsState,
            returnMode = returnMode
        )
    }

    fun buildAndroidIntentUri(
        method: Method,
        settingsState: SettingsState,
        returnMode: String
    ): String {
        val extras = mutableListOf<String>()

        extras += "S.method_id=${method.manifest.id}"
        extras += "S.return_mode=$returnMode"

        settingsState.asMap().toSortedMap().forEach { (key, value) ->
            extras += when (value) {
                is Boolean -> "B.$key=$value"
                is Int -> "i.$key=$value"
                is Float -> "f.$key=$value"
                is Double -> "f.$key=$value"
                else -> "S.$key=${encode(value.toString())}"
            }
        }

        return buildString {
            append("intent:#Intent;")
            append("action=com.example.xlsformlab.RUN_METHOD;")
            append("package=com.example.xlsformlab;")
            append(extras.joinToString(separator = ";"))
            append(";end")
        }
    }

    fun buildKotlinIntentSnippet(
        method: Method,
        settingsState: SettingsState,
        returnMode: String
    ): String {
        val extras = mutableListOf<String>()
        extras += ".putExtra(\"method_id\", \"${method.manifest.id}\")"
        extras += ".putExtra(\"return_mode\", \"$returnMode\")"

        settingsState.asMap().toSortedMap().forEach { (key, value) ->
            val renderedValue = when (value) {
                is String -> "\"$value\""
                else -> value.toString()
            }
            extras += ".putExtra(\"$key\", $renderedValue)"
        }

        return buildString {
            append("Intent(\"com.example.xlsformlab.RUN_METHOD\")\n")
            append("    .setPackage(\"com.example.xlsformlab\")\n")
            append(extras.joinToString("\n") { "    $it" })
        }
    }

    private fun encode(value: String): String {
        return URLEncoder.encode(value, "UTF-8").replace("+", "%20")
    }
}
