package com.example.xlsformlab.core.as100.runtime

import com.example.xlsformlab.core.as100.ArchitectureRef
import com.example.xlsformlab.core.as100.CapabilityContract
import com.example.xlsformlab.core.as100.ExecutionRequest
import com.example.xlsformlab.core.as100.ExecutionResult
import com.example.xlsformlab.core.as100.MethodDescriptor
import com.example.xlsformlab.core.as100.Signal
import com.example.xlsformlab.settings.SettingsState

/**
 * AS1.00 executable method.
 *
 * This is now the runtime-facing abstraction. Legacy Capability classes may
 * still exist behind adapters during migration, but callers should depend on
 * As100Method rather than Capability.
 */
interface As100Method {
    val id: String
    val ref: ArchitectureRef
    val descriptor: MethodDescriptor
    val contract: CapabilityContract

    fun request(
        action: String = id,
        context: Map<String, String> = emptyMap(),
        signals: List<Signal> = emptyList(),
        inputs: List<ArchitectureRef> = emptyList()
    ): ExecutionRequest

    fun execute(
        request: ExecutionRequest = request(),
        settingsState: SettingsState? = null,
        transport: String? = null
    ): ExecutionResult
}
