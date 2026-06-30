package com.example.xlsformlab.core

enum class MethodCategory {
    Measurement,
    Camera,
    DCE,
    NFC,
    Sensors,
    Mapping,
    Protocol,
    Workflow,
    BodyMap,
    Imaging,
    Randomisation,
    Attestation,
    Utilities
}

enum class MethodStatus {
    Experimental,
    Beta,
    Stable,
    Deprecated
}

data class MethodManifest(
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val category: MethodCategory,
    val status: MethodStatus = MethodStatus.Experimental,

    /**
     * ResearchOS-facing declaration of what this method does in research terms.
     * A method may implement one activity or a small bundle of tightly-related activities.
     */
    val activities: List<ResearchActivity> = emptyList(),

    /**
     * Context keys the runtime should try to supply before execution.
     * Examples: participant_id, study_id, visit_id, protocol_id, form_id.
     */
    val requiredContext: List<String> = emptyList(),

    /**
     * Android permissions or device affordances needed by the method.
     * These are descriptive at SDK level; Android permission requests remain platform code.
     */
    val requiredDeviceFeatures: List<String> = emptyList(),

    /**
     * Short human-readable contract for protocol designers and reviewers.
     */
    val contractSummary: String? = null
) {
    fun primaryActivityKind(): ResearchActivityKind? =
        activities.firstOrNull()?.kind
}
