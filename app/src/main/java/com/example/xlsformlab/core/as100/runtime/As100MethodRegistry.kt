package com.example.xlsformlab.core.as100.runtime

import com.example.xlsformlab.core.CapabilityRegistry
import com.example.xlsformlab.core.as100.CapabilityContract
import com.example.xlsformlab.core.as100.MethodDescriptor

/**
 * AS1.00-facing registry of executable methods.
 *
 * During migration, every legacy Capability is exposed as an AS1.00
 * CapabilityMethod. New AS1.00-native methods can later be registered here
 * without being forced through the old UI-heavy Capability interface.
 */
object As100MethodRegistry {

    private val methods: List<As100CapabilityMethod> by lazy {
        CapabilityRegistry.all().map { capability -> As100CapabilityMethod(capability) }
    }

    fun all(): List<As100CapabilityMethod> = methods

    fun find(id: String): As100CapabilityMethod? =
        methods.firstOrNull { it.id == id }

    fun require(id: String): As100CapabilityMethod =
        find(id) ?: error("No AS1.00 method registered with id: $id")

    fun descriptors(): List<MethodDescriptor> =
        methods.map { it.descriptor }

    fun contracts(): List<CapabilityContract> =
        methods.map { it.contract }
}
