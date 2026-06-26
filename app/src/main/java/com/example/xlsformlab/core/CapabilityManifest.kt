package com.example.xlsformlab.core

enum class CapabilityCategory {
    Measurement,
    Camera,
    DCE,
    NFC,
    Sensors,
    Mapping,
    Utilities
}

enum class CapabilityStatus {
    Experimental,
    Beta,
    Stable,
    Deprecated
}

data class CapabilityManifest(

    val id: String,

    val name: String,

    val description: String,

    val version: String,

    val category: CapabilityCategory,

    val status: CapabilityStatus = CapabilityStatus.Experimental
)