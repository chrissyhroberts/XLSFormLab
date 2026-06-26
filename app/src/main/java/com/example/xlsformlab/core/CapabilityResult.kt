package com.example.xlsformlab.core

object CapabilityRegistry {

    private val capabilities = mutableListOf<Capability>()

    fun register(capability: Capability) {
        capabilities += capability
    }

    fun all(): List<Capability> = capabilities

    fun byCategory(
        category: CapabilityCategory
    ): List<Capability> =
        capabilities.filter {
            it.manifest.category == category
        }

    fun find(id: String): Capability? =
        capabilities.firstOrNull {
            it.manifest.id == id
        }
}