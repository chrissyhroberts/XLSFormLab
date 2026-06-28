package com.example.xlsformlab.core

data class CapabilityOutputValidation(
    val valid: Boolean,
    val messages: List<String> = emptyList()
)

object CapabilityOutputValidator {

    fun validate(
        schema: CapabilityOutputSchema,
        output: CapabilityOutput
    ): CapabilityOutputValidation {
        val messages = mutableListOf<String>()

        schema.fields
            .filter { it.required }
            .forEach { field ->
                if (!output.fields.containsKey(field.id)) {
                    messages.add("Missing required output: ${field.id}")
                }
            }

        output.fields.keys
            .filter { key -> schema.fields.isNotEmpty() && key !in schema.fieldIds() }
            .forEach { key ->
                messages.add("Output not declared in schema: $key")
            }

        schema.fields.forEach { field ->
            val value = output.fields[field.id]
            if (value != null && !matchesType(value, field.type)) {
                messages.add("Output ${field.id} has wrong type. Expected ${field.type}.")
            }
        }

        return CapabilityOutputValidation(
            valid = messages.isEmpty(),
            messages = messages
        )
    }

    private fun matchesType(
        value: Any,
        type: CapabilityFieldType
    ): Boolean {
        return when (type) {
            CapabilityFieldType.Text -> value is String
            CapabilityFieldType.Integer -> value is Int || value is Long
            CapabilityFieldType.Float -> value is Float || value is Double || value is Int || value is Long
            CapabilityFieldType.Boolean -> value is Boolean
            CapabilityFieldType.Json -> true
        }
    }
}
