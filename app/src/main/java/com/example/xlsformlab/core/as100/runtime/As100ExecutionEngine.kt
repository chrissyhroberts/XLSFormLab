package com.example.xlsformlab.core.as100.runtime

import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.as100.ArchitectureId
import com.example.xlsformlab.core.as100.ArchitectureRef
import com.example.xlsformlab.core.as100.Entity
import com.example.xlsformlab.core.as100.ExecutionRequest
import com.example.xlsformlab.core.as100.ExecutionResult
import com.example.xlsformlab.core.as100.Observation
import com.example.xlsformlab.core.as100.Signal
import com.example.xlsformlab.core.as100.State
import com.example.xlsformlab.core.as100.TemporalContext
import com.example.xlsformlab.core.as100.Transformation
import com.example.xlsformlab.core.as100.TransformationStatus
import com.example.xlsformlab.settings.SettingsState

/**
 * Canonical AS1.00 execution entry point.
 *
 * For now this engine has two safe responsibilities:
 * 1. Run legacy capabilities through the AS1.00 bridge.
 * 2. Assemble AS1.00-native results for code paths that already interpret
 *    signals directly, such as NFC tag reads.
 *
 * The important architectural rule is that callers should depend on this
 * engine rather than directly constructing runtime results or directly calling
 * the legacy CapabilityRuntime.
 */
object As100ExecutionEngine {



    fun request(
        action: String,
        method: ArchitectureRef,
        id: ArchitectureId = ArchitectureId(),
        context: Map<String, String> = emptyMap(),
        signals: List<Signal> = emptyList(),
        inputs: List<ArchitectureRef> = emptyList(),
        temporalContext: TemporalContext = TemporalContext()
    ): ExecutionRequest = ExecutionRequest(
        id = id,
        action = action,
        method = method,
        context = context,
        signals = signals,
        inputs = inputs,
        temporalContext = temporalContext
    )

    fun requestFor(
        capability: Capability,
        action: String = capability.manifest.id,
        context: Map<String, String> = emptyMap(),
        signals: List<Signal> = emptyList(),
        inputs: List<ArchitectureRef> = emptyList()
    ): ExecutionRequest = As100CapabilityRuntime.requestFor(
        capability = capability,
        action = action,
        context = context,
        signals = signals,
        inputs = inputs
    )

    fun executeCapability(
        capability: Capability,
        request: ExecutionRequest,
        settingsState: SettingsState? = null,
        transport: String? = null
    ): ExecutionResult = As100CapabilityRuntime.execute(
        capability = capability,
        request = request,
        settingsState = settingsState,
        transport = transport
    )

    fun complete(
        request: ExecutionRequest,
        status: TransformationStatus,
        observations: List<Observation> = emptyList(),
        transformations: List<Transformation> = emptyList(),
        entities: List<Entity> = emptyList(),
        states: List<State> = emptyList(),
        diagnostics: Map<String, String> = emptyMap()
    ): ExecutionResult = ExecutionResult(
        request = request,
        status = status,
        entities = entities,
        observations = observations,
        transformations = transformations,
        states = states,
        diagnostics = diagnostics
    )
}
