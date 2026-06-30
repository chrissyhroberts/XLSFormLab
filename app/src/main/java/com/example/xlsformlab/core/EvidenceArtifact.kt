package com.example.xlsformlab.core

/**
 * ResearchOS evidence artifact: data plus enough metadata for audit, validation, and transport.
 */
data class EvidenceArtifact(
    val output: CapabilityOutput,
    val schema: CapabilityOutputSchema = CapabilityOutputSchema(),
    val context: ResearchContext = ResearchContext(),
    val provenance: Provenance,
    val validation: CapabilityOutputValidation = CapabilityOutputValidation(valid = true)
) {
    fun asFlatFields(includeProvenance: Boolean = true): Map<String, Any?> {
        if (!includeProvenance) return output.fields

        return output.fields + mapOf(
            "_xlsformlab_run_id" to provenance.runId,
            "_xlsformlab_generated_at" to provenance.generatedAt,
            "_xlsformlab_capability_id" to provenance.capabilityId,
            "_xlsformlab_capability_version" to provenance.capabilityVersion
        )
    }
}
