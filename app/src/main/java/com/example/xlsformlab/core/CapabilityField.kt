package com.example.xlsformlab.core

enum class CapabilityFieldType {
    Text,
    Integer,
    Float,
    Boolean,
    Json
}

data class CapabilityField(
    val id: String,
    val label: String,
    val type: CapabilityFieldType,
    val required: Boolean = true,
    val description: String? = null
)
