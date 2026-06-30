package com.example.xlsformlab.core

import com.example.xlsformlab.core.as100.runtime.As100MethodRegistry

/**
 * Legacy compatibility registry.
 *
 * The canonical registry is now As100MethodRegistry. This object remains so the
 * existing UI and transport code can migrate gradually without breaking imports.
 */
object CapabilityRegistry {

    fun all(): List<Capability> =
        As100MethodRegistry.legacyCapabilities()

    fun byCategory(category: CapabilityCategory): List<Capability> =
        As100MethodRegistry.legacyByCategory(category)

    fun find(id: String): Capability? =
        As100MethodRegistry.legacyFind(id)

    fun categoriesInUse(): List<CapabilityCategory> =
        As100MethodRegistry.legacyCategoriesInUse()

    fun manifests(): List<CapabilityManifest> =
        As100MethodRegistry.legacyManifests()

    fun require(id: String): Capability =
        As100MethodRegistry.legacyRequire(id)
}
