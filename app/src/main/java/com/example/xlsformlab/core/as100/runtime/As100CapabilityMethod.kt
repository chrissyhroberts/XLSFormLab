package com.example.xlsformlab.core.as100.runtime

import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.as100.ArchitectureId
import com.example.xlsformlab.core.as100.ArchitectureRef
import com.example.xlsformlab.core.as100.CapabilityContract
import com.example.xlsformlab.core.as100.ExecutionRequest
import com.example.xlsformlab.core.as100.ExecutionResult
import com.example.xlsformlab.core.as100.KnowledgeObjectType
import com.example.xlsformlab.core.as100.MethodDescriptor
import com.example.xlsformlab.core.as100.MethodObjectType
import com.example.xlsformlab.core.as100.Signal
import com.example.xlsformlab.settings.SettingsState

/**
 * AS1.00 method wrapper for an existing XLSForm Lab capability.
 *
 * This is the migration seam from the old Capability interface to the AS1.00
 * method/runtime model. The legacy capability remains available for the current
 * launcher, but AS1.00 callers can now treat it as a headless method with an
 * explicit descriptor, contract and execution request.
 */
class As100CapabilityMethod(
    val capability: Capability
) {
    val id: String = capability.manifest.id

    val ref: ArchitectureRef = As100CapabilityRuntime.methodRef(capability)

    val descriptor: MethodDescriptor = MethodDescriptor(
        id = ArchitectureId(capability.manifest.id),
        methodType = MethodObjectType.Capability,
        name = capability.manifest.name,
        version = capability.manifest.version,
        description = capability.manifest.description,
        inputs = capability.manifest.requiredContext,
        outputs = capability.outputSchema.fields.map { it.id },
        parameters = mapOf(
            "category" to capability.manifest.category.name,
            "status" to capability.manifest.status.name
        )
    )

    val contract: CapabilityContract = CapabilityContract(
        capability = ref,
        acceptedSignals = emptyList(),
        requiredContext = capability.manifest.requiredContext,
        producedKnowledgeTypes = listOf(KnowledgeObjectType.Observation),
        producedFields = capability.outputSchema.fields.map { it.id }
    )

    fun request(
        action: String = capability.manifest.id,
        context: Map<String, String> = emptyMap(),
        signals: List<Signal> = emptyList(),
        inputs: List<ArchitectureRef> = emptyList()
    ): ExecutionRequest = As100ExecutionEngine.requestFor(
        capability = capability,
        action = action,
        context = context,
        signals = signals,
        inputs = inputs
    )

    fun execute(
        request: ExecutionRequest = request(),
        settingsState: SettingsState? = null,
        transport: String? = null
    ): ExecutionResult = As100ExecutionEngine.executeCapability(
        capability = capability,
        request = request,
        settingsState = settingsState,
        transport = transport
    )
}
