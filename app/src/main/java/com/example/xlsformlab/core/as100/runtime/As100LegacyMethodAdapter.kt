package com.example.xlsformlab.core.as100.runtime

import com.example.xlsformlab.core.Method
import com.example.xlsformlab.core.as100.ArchitectureId
import com.example.xlsformlab.core.as100.ArchitectureRef
import com.example.xlsformlab.core.as100.MethodContract
import com.example.xlsformlab.core.as100.ExecutionRequest
import com.example.xlsformlab.core.as100.ExecutionResult
import com.example.xlsformlab.core.as100.KnowledgeObjectType
import com.example.xlsformlab.core.as100.MethodDescriptor
import com.example.xlsformlab.core.as100.MethodObjectType
import com.example.xlsformlab.core.as100.Signal
import com.example.xlsformlab.settings.SettingsState

/**
 * AS1.00 method wrapper for an existing ResearchOS method.
 *
 * This is the migration seam from the old Method interface to the AS1.00
 * method/runtime model. The legacy method remains available for the current
 * launcher, but AS1.00 callers can now treat it as a headless method with an
 * explicit descriptor, contract and execution request.
 */
class As100LegacyMethodAdapter(
    val method: Method
) : As100Method {
    override val id: String = method.manifest.id

    override val ref: ArchitectureRef = As100MethodRuntime.methodRef(method)

    override val descriptor: MethodDescriptor = MethodDescriptor(
        id = ArchitectureId(method.manifest.id),
        methodType = MethodObjectType.Method,
        name = method.manifest.name,
        version = method.manifest.version,
        description = method.manifest.description,
        inputs = method.manifest.requiredContext,
        outputs = method.outputSchema.fields.map { it.id },
        parameters = mapOf(
            "category" to method.manifest.category.name,
            "status" to method.manifest.status.name
        )
    )

    override val contract: MethodContract = MethodContract(
        method = ref,
        acceptedSignals = emptyList(),
        requiredContext = method.manifest.requiredContext,
        producedKnowledgeTypes = listOf(KnowledgeObjectType.Observation),
        producedFields = method.outputSchema.fields.map { it.id }
    )

    override fun request(
        action: String,
        context: Map<String, String>,
        signals: List<Signal>,
        inputs: List<ArchitectureRef>
    ): ExecutionRequest = As100ExecutionEngine.requestFor(
        method = method,
        action = action,
        context = context,
        signals = signals,
        inputs = inputs
    )

    override fun execute(
        request: ExecutionRequest,
        settingsState: SettingsState?,
        transport: String?
    ): ExecutionResult = As100ExecutionEngine.executeMethod(
        method = method,
        request = request,
        settingsState = settingsState,
        transport = transport
    )
}
