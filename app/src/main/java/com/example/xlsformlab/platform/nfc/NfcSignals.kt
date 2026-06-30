package com.example.xlsformlab.platform.nfc

import android.nfc.Tag
import com.example.xlsformlab.core.as100.ProvenanceContext
import com.example.xlsformlab.core.as100.Signal
import com.example.xlsformlab.core.as100.TemporalContext

/**
 * AS1.00 NFC signal adapter.
 *
 * This is intentionally thin: Android Tag details are normalised into a transient
 * Signal. Interpretation into observations remains outside the device service.
 */
object AndroidNfcSignalAdapter {
    const val SERVICE_ID = "device.nfc.android"
    const val SIGNAL_TYPE_TAG_DISCOVERED = "nfc.tag.discovered"

    fun fromTag(tag: Tag): Signal {
        val emittedAt = System.currentTimeMillis()
        return Signal(
            signalType = SIGNAL_TYPE_TAG_DISCOVERED,
            sourceService = SERVICE_ID,
            payload = linkedMapOf(
                "tag_uid_hex" to tag.id.toHexString(),
                "tag_uid_dec" to tag.id.toUnsignedLongString(),
                "tech_list" to tag.techList.joinToString(",") { it.substringAfterLast('.') }
            ),
            temporalContext = TemporalContext(
                eventTimeEpochMs = emittedAt,
                systemTimeEpochMs = emittedAt
            ),
            provenance = ProvenanceContext(
                provider = "android.nfc",
                methodId = SERVICE_ID
            )
        )
    }
}

private fun ByteArray?.toHexString(): String =
    this?.joinToString(separator = "") { byte -> "%02X".format(byte) }.orEmpty()

private fun ByteArray?.toUnsignedLongString(): String {
    val bytes = this ?: return ""
    var value = 0L
    bytes.forEach { value = (value shl 8) + (it.toInt() and 0xff) }
    return value.toString()
}
