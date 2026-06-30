package com.example.xlsformlab.core

data class MethodOutputSchema(
    val fields: List<MethodField> = emptyList()
) {
    fun fieldIds(): Set<String> =
        fields.map { it.id }.toSet()
}
