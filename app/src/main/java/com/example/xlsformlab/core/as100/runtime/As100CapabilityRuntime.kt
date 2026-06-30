package com.example.xlsformlab.core.as100.runtime

import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.CapabilityExecutionRequest
import com.example.xlsformlab.core.CapabilityRuntime
import com.example.xlsformlab.core.ResearchContext
import com.example.xlsformlab.core.as100.ArchitectureId
import com.example.xlsformlab.core.as100.ArchitectureRef
import com.example.xlsformlab.core.as100.ExecutionRequest
import com.example.xlsformlab.core.as100.ExecutionResult
import com.example.xlsformlab.core.as100.MethodObjectType
import com.example.xlsformlab.core.as100.Observation
import com.example.xlsformlab.core.as100.ProvenanceContext
import com.example.xlsformlab.core.as100.Signal
import com.example.xlsformlab.core.as100.TemporalContext
import com.example.xlsformlab.core.as100.Transformation
import com.example.xlsformlab.core.as100.TransformationStatus
import com.example.xlsformlab.settings.SettingsState

/**
 * AS1.00 execution bridge for existing XLSForm Lab capabilities.
 *
 * This adapter lets new AS1.00 code use ExecutionRequest / ExecutionResult while
 * current capabilities still run through the legacy CapabilityRuntime. The only
 * lossy conversion happens at the legacy boundary, where typed AS1.00 context
 * and signal payload values are converted to strings.
 */
object As100CapabilityRuntime {

    fun methodRef(capability: Capability): ArchitectureRef = ArchitectureRef(
        id = ArchitectureId(capability.manifest.id),
        type = MethodObjectType.Capability.name,
        label = capability.manifest.name
    )

    fun requestFor(
        capability: Capability,
        action: String = capability.manifest.id,
        context: Map<String, String> = emptyMap(),
        signals: List<Signal> = emptyList(),
        inputs: List<ArchitectureRef> = emptyList(),
        temporalContext: TemporalContext = TemporalContext()
    ): ExecutionRequest = ExecutionRequest(
        action = action,
        method = methodRef(capability),
        context = context,
        signals = signals,
        inputs = inputs,
        temporalContext = temporalContext
    )

    fun execute(
        capability: Capability,
        request: ExecutionRequest,
        settingsState: SettingsState? = null,
        transport: String? = null
    ): ExecutionResult {
        val legacyContext: Map<String, String> = stringifyMap(request.context)
        val legacyParameters: Map<String, String> = buildLegacyParameters(
            context = request.context,
            signals = request.signals
        )

        val legacy = CapabilityRuntime.execute(
            capability = capability,
            request = CapabilityExecutionRequest(
                capabilityId = capability.manifest.id,
                context = ResearchContext(values = legacyContext),
                parameters = legacyParameters,
                transport = transport
            ),
            settingsState = settingsState
        )

        val status = if (legacy.success) TransformationStatus.Succeeded else TransformationStatus.Failed
        val provenance = ProvenanceContext(
            provider = "xlsformlab.capability_runtime",
            methodId = capability.manifest.id,
            methodVersion = capability.manifest.version
        )

        val diagnostics: Map<String, String> = buildMap {
            legacy.errorMessage?.let { put("error", it) }
            if (legacy.warnings.isNotEmpty()) {
                put("warnings", legacy.warnings.joinToString(" | "))
            }
        }

        val observation = legacy.artifact?.let { artifact ->
            Observation(
                phenomenon = "capability.output.${capability.manifest.id}",
                values = stringifyMap(artifact.output.fields),
                temporalContext = request.temporalContext,
                provenance = provenance
            )
        }

        val outputRefs = observation?.let {
            listOf(ArchitectureRef(it.id, "Observation", it.phenomenon))
        } ?: emptyList()

        val transformation = Transformation(
            action = request.action,
            method = request.method,
            inputs = request.inputs + request.signals.map { ArchitectureRef(it.id, "Signal", it.signalType) },
            outputs = outputRefs,
            status = status,
            diagnostics = diagnostics,
            temporalContext = request.temporalContext,
            provenance = provenance
        )

        return ExecutionResult(
            request = request,
            status = status,
            observations = observation?.let { listOf(it) } ?: emptyList(),
            transformations = listOf(transformation),
            diagnostics = diagnostics
        )
    }

    private fun buildLegacyParameters(
        context: Map<String, String>,
        signals: List<Signal>
    ): Map<String, String> {
        val parameters = linkedMapOf<String, String>()
        parameters.putAll(stringifyMap(context))
        for (signal in signals) {
            parameters.putAll(prefixMap("signal.${signal.signalType}.", signal.payload))
        }
        return parameters
    }

    private fun stringifyMap(values: Map<String, *>): Map<String, String> = buildMap {
        for ((key, value) in values) {
            put(key, value?.toString().orEmpty())
        }
    }

    private fun prefixMap(prefix: String, values: Map<String, Any?>): Map<String, String> = buildMap {
        for ((key, value) in values) {
            put(prefix + key, value?.toString().orEmpty())
        }
    }
}
