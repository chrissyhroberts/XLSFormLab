package com.example.xlsformlab.core

data class CapabilityOutputSchema(
    val fields: List<CapabilityField> = emptyList()
) {
    fun fieldIds(): Set<String> =
        fields.map { it.id }.toSet()
}
