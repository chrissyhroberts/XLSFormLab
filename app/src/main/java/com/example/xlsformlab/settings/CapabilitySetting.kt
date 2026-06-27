package com.example.xlsformlab.settings

sealed class CapabilitySetting {

    abstract val id: String
    abstract val label: String
    abstract val description: String?

    data class BooleanSetting(
        override val id: String,
        override val label: String,
        override val description: String? = null,
        val defaultValue: Boolean
    ) : CapabilitySetting()

    data class IntSetting(
        override val id: String,
        override val label: String,
        override val description: String? = null,
        val defaultValue: Int,
        val minimum: Int? = null,
        val maximum: Int? = null
    ) : CapabilitySetting()

    data class FloatSetting(
        override val id: String,
        override val label: String,
        override val description: String? = null,
        val defaultValue: Float,
        val minimum: Float? = null,
        val maximum: Float? = null
    ) : CapabilitySetting()

    data class TextSetting(
        override val id: String,
        override val label: String,
        override val description: String? = null,
        val defaultValue: String
    ) : CapabilitySetting()

    data class ChoiceSetting(
        override val id: String,
        override val label: String,
        override val description: String? = null,
        val defaultValue: String,
        val choices: List<String>
    ) : CapabilitySetting()
}