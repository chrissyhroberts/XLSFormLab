package com.example.xlsformlab.core

/**
 * ResearchOS evidence artifact: data plus enough metadata for audit, validation, and transport.
 */
data class EvidenceArtifact(
    val output: MethodOutput,
    val schema: MethodOutputSchema = MethodOutputSchema(),
    val context: ResearchContext = ResearchContext(),
    val provenance: Provenance,
    val validation: MethodOutputValidation = MethodOutputValidation(valid = true)
) {
    fun asFlatFields(includeProvenance: Boolean = true): Map<String, Any?> {
        if (!includeProvenance) return output.fields

        return output.fields + mapOf(
            "_researchos_run_id" to provenance.runId,
            "_researchos_generated_at" to provenance.generatedAt,
            "_researchos_method_id" to provenance.methodId,
            "_researchos_method_version" to provenance.methodVersion
        )
    }
}
