package com.example.xlsformlab.settings

import androidx.compose.runtime.mutableStateMapOf

class SettingsState(
    settings: List<CapabilitySetting>
) {
    private val values = mutableStateMapOf<String, Any>()

    init {
        settings.forEach { setting ->
            values[setting.id] = setting.defaultValue()
        }
    }

    fun getString(id: String): String =
        values[id] as? String ?: ""

    fun setString(id: String, value: String) {
        values[id] = value
    }

    fun getBoolean(id: String): Boolean =
        values[id] as? Boolean ?: false

    fun setBoolean(id: String, value: Boolean) {
        values[id] = value
    }

    fun getFloat(id: String): Float =
        values[id] as? Float ?: 0f

    fun setFloat(id: String, value: Float) {
        values[id] = value
    }

    fun getInt(id: String): Int =
        values[id] as? Int ?: 0

    fun setInt(id: String, value: Int) {
        values[id] = value
    }

    fun asMap(): Map<String, Any> =
        values.toMap()
}

fun CapabilitySetting.defaultValue(): Any {
    return when (this) {
        is CapabilitySetting.BooleanSetting -> defaultValue
        is CapabilitySetting.IntSetting -> defaultValue
        is CapabilitySetting.FloatSetting -> defaultValue
        is CapabilitySetting.TextSetting -> defaultValue
        is CapabilitySetting.ChoiceSetting -> defaultValue
    }
}