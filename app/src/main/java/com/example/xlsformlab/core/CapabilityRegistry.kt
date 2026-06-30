package com.example.xlsformlab.core

import com.example.xlsformlab.modules.adminfingerprint.AdminFingerprintCapability
import com.example.xlsformlab.modules.calibratedscale.CalibratedScaleCapability
import com.example.xlsformlab.modules.gpstargetnavigator.GpsTargetNavigatorCapability

object CapabilityRegistry {

    private val capabilities = listOf(
        CalibratedScaleCapability(),
        AdminFingerprintCapability(),
        GpsTargetNavigatorCapability()
    )

    fun all(): List<Capability> = capabilities

    fun byCategory(category: CapabilityCategory): List<Capability> =
        capabilities.filter { it.manifest.category == category }

    fun find(id: String): Capability? =
        capabilities.firstOrNull { it.manifest.id == id }

    fun categoriesInUse(): List<CapabilityCategory> =
        capabilities.map { it.manifest.category }.distinct()

    fun manifests(): List<CapabilityManifest> =
        capabilities.map { it.manifest }

    fun require(id: String): Capability =
        find(id) ?: error("No XLSForm Lab capability registered with id: $id")
}
