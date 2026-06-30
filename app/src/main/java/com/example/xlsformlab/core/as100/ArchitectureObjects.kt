package com.example.xlsformlab.core.as100

import java.util.UUID

/**
 * AS1.00 canonical architectural objects.
 *
 * These classes are deliberately independent of Android, ODK, Compose and storage.
 * Platform-specific code should enter the architecture by producing Signals; methods
 * should then interpret Signals into Observations and explicit Transformations.
 */

enum class ArchitectureDomain {
    Knowledge,
    Method,
    Time,
    Space,
    Transformation
}

enum class KnowledgeObjectType {
    Entity,
    Attribute,
    Observation,
    Relationship,
    Classification
}

enum class MethodObjectType {
    Capability,
    DeviceService,
    SignalInterpreter,
    Rule,
    Calculation,
    Workflow
}

enum class TransformationStatus {
    Requested,
    Succeeded,
    Failed,
    Cancelled,
    Unsupported
}

data class ArchitectureId(
    val value: String = UUID.randomUUID().toString()
)

data class ArchitectureRef(
    val id: ArchitectureId,
    val type: String,
    val label: String? = null
)

data class TemporalContext(
    val eventTimeEpochMs: Long? = null,
    val observationTimeEpochMs: Long? = null,
    val executionTimeEpochMs: Long? = null,
    val effectiveTimeEpochMs: Long? = null,
    val systemTimeEpochMs: Long = System.currentTimeMillis()
)

data class SpatialContext(
    val referenceSystem: String? = null,
    val location: Map<String, String> = emptyMap(),
    val region: Map<String, String> = emptyMap()
)

data class ProvenanceContext(
    val provider: String,
    val methodId: String? = null,
    val methodVersion: String? = null,
    val deviceId: String? = null,
    val operatorId: String? = null,
    val softwareVersion: String? = null,
    val protocolVersion: String? = null,
    val extra: Map<String, String> = emptyMap()
)

/**
 * Transient information emitted by a Device Service.
 *
 * A Signal is not persistent knowledge. It becomes architectural knowledge only
 * after a Method, usually a SignalInterpreter, produces one or more Observations.
 */
data class Signal(
    val id: ArchitectureId = ArchitectureId(),
    val signalType: String,
    val sourceService: String,
    val payload: Map<String, String>,
    val temporalContext: TemporalContext = TemporalContext(),
    val spatialContext: SpatialContext? = null,
    val provenance: ProvenanceContext
)

data class Entity(
    val id: ArchitectureId = ArchitectureId(),
    val entityType: String,
    val attributes: Map<String, String> = emptyMap(),
    val temporalContext: TemporalContext = TemporalContext(),
    val spatialContext: SpatialContext? = null
)

data class Observation(
    val id: ArchitectureId = ArchitectureId(),
    val phenomenon: String,
    val subject: ArchitectureRef? = null,
    val values: Map<String, String>,
    val sourceSignal: ArchitectureRef? = null,
    val temporalContext: TemporalContext = TemporalContext(),
    val spatialContext: SpatialContext? = null,
    val provenance: ProvenanceContext
)

data class Relationship(
    val id: ArchitectureId = ArchitectureId(),
    val relationshipType: String,
    val from: ArchitectureRef,
    val to: ArchitectureRef,
    val attributes: Map<String, String> = emptyMap(),
    val temporalContext: TemporalContext = TemporalContext()
)

data class State(
    val id: ArchitectureId = ArchitectureId(),
    val subject: ArchitectureRef,
    val stateType: String,
    val values: Map<String, String> = emptyMap(),
    val temporalContext: TemporalContext = TemporalContext()
)

/**
 * An explicit AS1.00 state transition record.
 */
data class Transformation(
    val id: ArchitectureId = ArchitectureId(),
    val action: String,
    val method: ArchitectureRef,
    val inputs: List<ArchitectureRef> = emptyList(),
    val outputs: List<ArchitectureRef> = emptyList(),
    val previousState: State? = null,
    val resultingState: State? = null,
    val status: TransformationStatus,
    val diagnostics: Map<String, String> = emptyMap(),
    val temporalContext: TemporalContext = TemporalContext(),
    val provenance: ProvenanceContext
)

data class ExecutionRequest(
    val id: ArchitectureId = ArchitectureId(),
    val action: String,
    val method: ArchitectureRef,
    val context: Map<String, String> = emptyMap(),
    val signals: List<Signal> = emptyList(),
    val inputs: List<ArchitectureRef> = emptyList(),
    val temporalContext: TemporalContext = TemporalContext()
)

data class ExecutionResult(
    val request: ExecutionRequest,
    val status: TransformationStatus,
    val observations: List<Observation> = emptyList(),
    val transformations: List<Transformation> = emptyList(),
    val states: List<State> = emptyList(),
    val diagnostics: Map<String, String> = emptyMap()
)

interface DeviceService {
    val serviceId: String
    fun describe(): Map<String, String>
}

fun interface SignalInterpreter {
    fun interpret(signal: Signal): List<Observation>
}
