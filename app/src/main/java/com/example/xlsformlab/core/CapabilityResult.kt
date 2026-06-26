package com.example.xlsformlab.core

data class CapabilityResult(
    val success: Boolean = true,
    val fields: Map<String, String> = emptyMap(),
    val json: String? = null,
    val errorMessage: String? = null
)