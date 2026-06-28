package com.example.xlsformlab.core

import com.example.xlsformlab.modules.calibratedscale.CalibratedScaleCapability
import com.example.xlsformlab.modules.adminfingerprint.AdminFingerprintCapability

object CapabilityRegistry {

    private val capabilities = listOf(
        CalibratedScaleCapability(),
        AdminFingerprintCapability()
    )

    fun all(): List<Capability> = capabilities

    fun byCategory(category: CapabilityCategory): List<Capability> =
        capabilities.filter { it.manifest.category == category }

    fun find(id: String): Capability? =
        capabilities.firstOrNull { it.manifest.id == id }
}