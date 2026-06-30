package com.example.xlsformlab.core

/**
 * Compatibility request used by existing capability implementations.
 * Prefer CapabilityExecutionRequest for new runtime code.
 */
data class CapabilityRequest(
    val parameters: Map<String, String> = emptyMap()
) {
    fun get(key: String): String? = parameters[key]
}
