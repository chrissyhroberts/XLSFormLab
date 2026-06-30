package com.example.xlsformlab.core.as100.runtime

import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.CapabilityCategory
import com.example.xlsformlab.core.CapabilityManifest
import com.example.xlsformlab.core.as100.CapabilityContract
import com.example.xlsformlab.core.as100.MethodDescriptor
import com.example.xlsformlab.modules.adminfingerprint.AdminFingerprintCapability
import com.example.xlsformlab.modules.calibratedscale.CalibratedScaleCapability
import com.example.xlsformlab.modules.calibratedscale.CalibratedScaleMethod
import com.example.xlsformlab.modules.gpstargetnavigator.GpsTargetNavigatorCapability
import com.example.xlsformlab.modules.nfc.NfcReadCapability
import com.example.xlsformlab.modules.nfc.NfcReadMethod
import com.example.xlsformlab.modules.nfc.NfcWriteCapability

/**
 * Canonical AS1.00-facing registry of executable methods.
 *
 * During migration, existing Capability implementations are owned here and
 * exposed as As100Method instances via a legacy adapter. The old
 * CapabilityRegistry now delegates to this registry so the conceptual centre of
 * the app is Method, not Capability.
 */
object As100MethodRegistry {

    private val legacyCapabilities: List<Capability> by lazy {
        listOf(
            CalibratedScaleCapability(),
            AdminFingerprintCapability(),
            GpsTargetNavigatorCapability(),
            NfcReadCapability(),
            NfcWriteCapability()
        )
    }

    private val methods: List<As100Method> by lazy {
        listOf(NfcReadMethod, CalibratedScaleMethod) + legacyCapabilities
            .filterNot { capability -> capability.manifest.id in setOf(NfcReadMethod.ID, CalibratedScaleMethod.ID) }
            .map { capability -> As100CapabilityMethod(capability) }
    }

    fun all(): List<As100Method> = methods

    fun find(id: String): As100Method? =
        methods.firstOrNull { it.id == id }

    fun require(id: String): As100Method =
        find(id) ?: error("No AS1.00 method registered with id: $id")

    fun descriptors(): List<MethodDescriptor> =
        methods.map { it.descriptor }

    fun contracts(): List<CapabilityContract> =
        methods.map { it.contract }

    /** Legacy compatibility surface for UI/transport still being migrated. */
    fun legacyCapabilities(): List<Capability> = legacyCapabilities

    fun legacyFind(id: String): Capability? =
        legacyCapabilities.firstOrNull { it.manifest.id == id }

    fun legacyRequire(id: String): Capability =
        legacyFind(id) ?: error("No legacy capability registered with id: $id")

    fun legacyByCategory(category: CapabilityCategory): List<Capability> =
        legacyCapabilities.filter { it.manifest.category == category }

    fun legacyCategoriesInUse(): List<CapabilityCategory> =
        legacyCapabilities.map { it.manifest.category }.distinct()

    fun legacyManifests(): List<CapabilityManifest> =
        legacyCapabilities.map { it.manifest }
}
