package com.example.xlsformlab.intents

import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.settings.SettingsState
import java.net.URLEncoder

object OdkIntentBuilder {

    private const val APP_PACKAGE = "com.example.xlsformlab"
    private const val ACTION_RUN_CAPABILITY = "com.example.xlsformlab.RUN_CAPABILITY"

    fun buildIntentUri(
        capability: Capability,
        settingsState: SettingsState,
        returnMode: String
    ): String {
        val builder = StringBuilder()

        builder.append("intent:#Intent;")
        builder.append("action=").append(ACTION_RUN_CAPABILITY).append(";")
        builder.append("package=").append(APP_PACKAGE).append(";")
        builder.append("S.capability_id=").append(encode(capability.manifest.id)).append(";")
        builder.append("S.return_mode=").append(encode(returnMode)).append(";")

        settingsState.asMap()
            .toSortedMap()
            .forEach { (key, value) ->
                appendExtra(
                    builder = builder,
                    key = key,
                    value = value
                )
            }

        builder.append("end")
        return builder.toString()
    }

    private fun appendExtra(
        builder: StringBuilder,
        key: String,
        value: Any
    ) {
        when (value) {
            is Boolean -> {
                builder.append("B.")
                    .append(key)
                    .append("=")
                    .append(value)
                    .append(";")
            }

            is Int -> {
                builder.append("i.")
                    .append(key)
                    .append("=")
                    .append(value)
                    .append(";")
            }

            is Float -> {
                builder.append("f.")
                    .append(key)
                    .append("=")
                    .append(value)
                    .append(";")
            }

            is Double -> {
                builder.append("d.")
                    .append(key)
                    .append("=")
                    .append(value)
                    .append(";")
            }

            else -> {
                builder.append("S.")
                    .append(key)
                    .append("=")
                    .append(encode(value.toString()))
                    .append(";")
            }
        }
    }

    private fun encode(value: String): String {
        return URLEncoder
            .encode(value, "UTF-8")
            .replace("+", "%20")
    }
}
