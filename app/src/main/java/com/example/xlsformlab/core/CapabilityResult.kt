package com.example.xlsformlab.core

/**
 * Compatibility result used by existing capability implementations.
 * Prefer EvidenceArtifact via CapabilityRuntime for new runtime code.
 */
data class CapabilityResult(
    val success: Boolean = true,
    val fields: Map<String, String> = emptyMap(),
    val json: String? = null,
    val errorMessage: String? = null
)
