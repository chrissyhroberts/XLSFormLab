package com.example.xlsformlab.core

import com.example.xlsformlab.settings.SettingsState

/**
 * Canonical runtime request. Existing capabilities can still use CapabilityRequest directly;
 * this wrapper is the stable ResearchOS-aligned execution envelope.
 */
data class CapabilityExecutionRequest(
    val capabilityId: String,
    val context: ResearchContext = ResearchContext(),
    val parameters: Map<String, String> = emptyMap(),
    val transport: String? = null
)

data class CapabilityExecutionResult(
    val success: Boolean,
    val artifact: EvidenceArtifact? = null,
    val errorMessage: String? = null,
    val warnings: List<String> = emptyList()
)

object CapabilityRuntime {

    fun execute(
        capability: Capability,
        request: CapabilityExecutionRequest,
        settingsState: SettingsState? = null
    ): CapabilityExecutionResult {
        val missingContext = request.context.missing(capability.manifest.requiredContext)
        val contextWarnings = missingContext.map { key -> "Missing context: $key" }

        val legacyResult = capability.execute(
            CapabilityRequest(parameters = request.parameters + request.context.values)
        )

        if (!legacyResult.success) {
            return CapabilityExecutionResult(
                success = false,
                errorMessage = legacyResult.errorMessage ?: "Capability execution failed.",
                warnings = contextWarnings
            )
        }

        val output = when {
            legacyResult.fields.isNotEmpty() -> CapabilityOutput(legacyResult.fields)
            settingsState != null -> capability.buildOutput(settingsState)
            else -> CapabilityOutput(
                legacyResult.json?.let { mapOf("json" to it) } ?: emptyMap()
            )
        }

        val validation = CapabilityOutputValidator.validate(
            schema = capability.outputSchema,
            output = output
        )

        val provenance = Provenance(
            capabilityId = capability.manifest.id,
            capabilityVersion = capability.manifest.version,
            activityIds = capability.manifest.activities.map { it.id },
            transport = request.transport,
            warnings = contextWarnings + validation.messages
        )

        return CapabilityExecutionResult(
            success = validation.valid,
            artifact = EvidenceArtifact(
                output = output,
                schema = capability.outputSchema,
                context = request.context,
                provenance = provenance,
                validation = validation
            ),
            errorMessage = if (validation.valid) null else validation.messages.joinToString("; "),
            warnings = contextWarnings
        )
    }
}
