package com.example.xlsformlab.modules.nfc

import androidx.compose.runtime.Composable
import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.CapabilityCategory
import com.example.xlsformlab.core.CapabilityField
import com.example.xlsformlab.core.CapabilityFieldType
import com.example.xlsformlab.core.CapabilityManifest
import com.example.xlsformlab.core.CapabilityOutput
import com.example.xlsformlab.core.CapabilityOutputSchema
import com.example.xlsformlab.core.CapabilityRequest
import com.example.xlsformlab.core.CapabilityResult
import com.example.xlsformlab.core.CapabilityStatus
import com.example.xlsformlab.presentation.nfc.NfcReadDemoScreen
import com.example.xlsformlab.presentation.nfc.NfcReadHelpScreen
import com.example.xlsformlab.settings.CapabilitySetting
import com.example.xlsformlab.settings.SettingsState

class NfcReadCapability : Capability {

    override val manifest = CapabilityManifest(
        id = ID,
        name = "NFC Tag Read",
        description = "Identify and observe an NFC tag as structured evidence plus immutable tag artifact.",
        version = VERSION,
        category = CapabilityCategory.NFC,
        status = CapabilityStatus.Experimental
    )

    override val settings = listOf(
        CapabilitySetting.BooleanSetting(
            id = "read_once",
            label = "Read once",
            description = "Stop updating the observation after the first tag is read.",
            group = "Capture",
            defaultValue = true
        ),
        CapabilitySetting.TextSetting(
            id = "field_filter",
            label = "Field filter",
            description = "Optional comma-separated list of evidence fields to expose to transport. Leave blank for all standard tag fields.",
            group = "Output",
            defaultValue = ""
        )
    )

    override val outputSchema = CapabilityOutputSchema(
        fields = evidenceFieldSchema() + researchEnvelopeSchema()
    )

    @Composable
    override fun Demo(settingsState: SettingsState) {
        NfcReadDemoScreen(settingsState)
    }

    @Composable
    override fun Help() {
        NfcReadHelpScreen()
    }

    override fun buildOutput(settingsState: SettingsState): CapabilityOutput = CapabilityOutput()

    override fun execute(request: CapabilityRequest): CapabilityResult {
        return CapabilityResult(
            success = false,
            errorMessage = "NFC read is an interactive device capture. The shell starts a session, receives an Android Tag, and maps it to Evidence/Artifact records."
        )
    }

    companion object {
        const val ID = NfcReadMethod.ID
        const val VERSION = NfcReadMethod.VERSION
    }
}

internal fun parseFieldFilter(value: String): Set<String> =
    value.split(',').map { it.trim() }.filter { it.isNotBlank() }.toSet()

internal fun applyFieldFilter(fields: Map<String, String>, filter: Set<String>): Map<String, String> =
    if (filter.isEmpty()) fields else fields.filterKeys { it in filter }

internal fun evidenceFieldSchema(): List<CapabilityField> =
    NfcEvidenceFields.tagOutputFields.map { key ->
        CapabilityField(
            id = key,
            label = key,
            type = if (key.endsWith("_json")) CapabilityFieldType.Json else CapabilityFieldType.Text,
            required = key == NfcEvidenceFields.TAG_UID_HEX
        )
    }

internal fun researchEnvelopeSchema(): List<CapabilityField> = listOf(
    CapabilityField(ResearchOutputFields.EVIDENCE_ID, "Evidence ID", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.EVIDENCE_KIND, "Evidence kind", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.PHENOMENON, "Phenomenon", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.METHOD, "Method", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.TEMPORAL_SEMANTICS, "Temporal semantics", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.AGGREGATION_SEMANTICS, "Aggregation semantics", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.LINEAGE, "Lineage", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.PROVENANCE_JSON, "Provenance JSON", CapabilityFieldType.Json, required = true),
    CapabilityField(ResearchOutputFields.CAPTURE_OUTCOME_JSON, "Capture outcome JSON", CapabilityFieldType.Json, required = true),
    CapabilityField(ResearchOutputFields.QUALITY_JSON, "Quality JSON", CapabilityFieldType.Json, required = true),
    CapabilityField(ResearchOutputFields.VALIDATION_JSON, "Validation JSON", CapabilityFieldType.Json, required = true),
    CapabilityField(ResearchOutputFields.ARTIFACT_JSON, "Artifact JSON", CapabilityFieldType.Json, required = true),
    CapabilityField(ResearchOutputFields.EVIDENCE_JSON, "Evidence JSON", CapabilityFieldType.Json, required = true),
    CapabilityField(ResearchOutputFields.EXECUTION_JSON, "Execution JSON", CapabilityFieldType.Json, required = true),
    CapabilityField(ResearchOutputFields.AS_SIGNAL_TYPE, "AS1.00 signal type", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.AS_SIGNAL_SOURCE_SERVICE, "AS1.00 signal source service", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.AS_TRANSFORMATION_ACTION, "AS1.00 transformation action", CapabilityFieldType.Text, required = true),
    CapabilityField(ResearchOutputFields.AS_TRANSFORMATION_STATUS, "AS1.00 transformation status", CapabilityFieldType.Text, required = true)
)
