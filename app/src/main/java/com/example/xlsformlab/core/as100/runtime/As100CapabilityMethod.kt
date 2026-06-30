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
) : As100Method {
    override val id: String = capability.manifest.id

    override val ref: ArchitectureRef = As100CapabilityRuntime.methodRef(capability)

    override val descriptor: MethodDescriptor = MethodDescriptor(
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

    override val contract: CapabilityContract = CapabilityContract(
        capability = ref,
        acceptedSignals = emptyList(),
        requiredContext = capability.manifest.requiredContext,
        producedKnowledgeTypes = listOf(KnowledgeObjectType.Observation),
        producedFields = capability.outputSchema.fields.map { it.id }
    )

    override fun request(
        action: String,
        context: Map<String, String>,
        signals: List<Signal>,
        inputs: List<ArchitectureRef>
    ): ExecutionRequest = As100ExecutionEngine.requestFor(
        capability = capability,
        action = action,
        context = context,
        signals = signals,
        inputs = inputs
    )

    override fun execute(
        request: ExecutionRequest,
        settingsState: SettingsState?,
        transport: String?
    ): ExecutionResult = As100ExecutionEngine.executeCapability(
        capability = capability,
        request = request,
        settingsState = settingsState,
        transport = transport
    )
}
