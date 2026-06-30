package com.example.xlsformlab.core.as100.runtime

import com.example.xlsformlab.core.as100.ArchitectureRef
import com.example.xlsformlab.core.as100.KnowledgeObject
import com.example.xlsformlab.core.as100.Signal
import com.example.xlsformlab.core.as100.Transformation

fun Signal.asRef(label: String? = signalType): ArchitectureRef =
    ArchitectureRef(id = id, type = objectType, label = label)

fun KnowledgeObject.asRef(label: String? = null): ArchitectureRef =
    ArchitectureRef(id = id, type = objectType, label = label)

fun Transformation.asRef(label: String? = action): ArchitectureRef =
    ArchitectureRef(id = id, type = objectType, label = label)
