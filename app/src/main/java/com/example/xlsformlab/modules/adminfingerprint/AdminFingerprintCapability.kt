package com.example.xlsformlab.modules.adminfingerprint

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.CapabilityCategory
import com.example.xlsformlab.core.CapabilityField
import com.example.xlsformlab.core.CapabilityFieldType
import com.example.xlsformlab.core.CapabilityManifest
import com.example.xlsformlab.core.CapabilityOutput
import com.example.xlsformlab.core.CapabilityOutputSchema
import com.example.xlsformlab.core.CapabilityRequest
import com.example.xlsformlab.core.CapabilityResult
import com.example.xlsformlab.core.CapabilityStatus
import com.example.xlsformlab.platform.BiometricAuthHelper
import com.example.xlsformlab.settings.CapabilitySetting
import com.example.xlsformlab.settings.SettingsState
import java.time.Instant

class AdminFingerprintCapability : Capability {

    override val manifest = CapabilityManifest(
        id = "admin_fingerprint_confirmation",
        name = "Admin Fingerprint Confirmation",
        description = "Request biometric or PIN confirmation from the registered device administrator.",
        version = "0.1.0",
        category = CapabilityCategory.Utilities,
        status = CapabilityStatus.Experimental
    )

    override val settings = listOf(
        CapabilitySetting.TextSetting(
            id = "prompt_title",
            label = "Prompt title",
            group = "Prompt",
            defaultValue = "Admin confirmation required"
        ),
        CapabilitySetting.TextSetting(
            id = "prompt_subtitle",
            label = "Prompt subtitle",
            group = "Prompt",
            defaultValue = "Use fingerprint, face unlock, PIN, pattern, or password to continue"
        ),
        CapabilitySetting.TextSetting(
            id = "prompt_description",
            label = "Prompt description",
            group = "Prompt",
            defaultValue = "Confirm that a registered phone administrator is present."
        ),
        CapabilitySetting.TextSetting(
            id = "cancel_text",
            label = "Cancel text",
            group = "Prompt",
            defaultValue = "Cancel"
        ),
        CapabilitySetting.TextSetting(
            id = "confirmation_reason",
            label = "Confirmation reason",
            group = "Output",
            defaultValue = "admin_confirm"
        ),
        CapabilitySetting.BooleanSetting(
            id = "confirmation_required",
            label = "Require explicit confirmation",
            group = "Security",
            defaultValue = true
        ),
        CapabilitySetting.BooleanSetting(
            id = "allow_device_credential",
            label = "Allow PIN/pattern/password fallback",
            group = "Security",
            defaultValue = true
        )
    )

    override val outputSchema = CapabilityOutputSchema(
        fields = listOf(
            CapabilityField(
                id = "confirmed",
                label = "Confirmed",
                type = CapabilityFieldType.Boolean,
                required = true
            ),
            CapabilityField(
                id = "auth_method",
                label = "Authentication method",
                type = CapabilityFieldType.Text,
                required = true
            ),
            CapabilityField(
                id = "timestamp_ms",
                label = "Timestamp milliseconds",
                type = CapabilityFieldType.Integer,
                required = true
            ),
            CapabilityField(
                id = "timestamp_iso",
                label = "Timestamp ISO",
                type = CapabilityFieldType.Text,
                required = true
            ),
            CapabilityField(
                id = "reason",
                label = "Reason",
                type = CapabilityFieldType.Text,
                required = false
            ),
            CapabilityField(
                id = "message",
                label = "Message",
                type = CapabilityFieldType.Text,
                required = false
            )
        )
    )

    @Composable
    override fun Demo(
        settingsState: SettingsState
    ) {
        val context = LocalContext.current
        var statusText by remember {
            mutableStateOf("Not confirmed")
        }

        val allowDeviceCredential = settingsState.getBoolean("allow_device_credential")
        val availability = BiometricAuthHelper.availability(
            context = context,
            allowDeviceCredential = allowDeviceCredential
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(availability.message)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Availability code: ${availability.code}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                enabled = availability.available,
                onClick = {
                    BiometricAuthHelper.authenticate(
                        context = context,
                        title = settingsState.getString("prompt_title"),
                        subtitle = settingsState.getString("prompt_subtitle"),
                        description = settingsState.getString("prompt_description"),
                        cancelText = settingsState.getString("cancel_text"),
                        confirmationRequired = settingsState.getBoolean("confirmation_required"),
                        allowDeviceCredential = settingsState.getBoolean("allow_device_credential"),
                        onSuccess = { authMethod ->
                            val now = System.currentTimeMillis()

                            settingsState.setBoolean("confirmed", true)
                            settingsState.setInt(
                                "timestamp_ms",
                                now.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
                            )
                            settingsState.setString(
                                "timestamp_iso",
                                Instant.ofEpochMilli(now).toString()
                            )
                            settingsState.setString("auth_method", authMethod)
                            settingsState.setString("message", "Confirmed")
                            statusText = "Confirmed using $authMethod"
                        },
                        onFailure = { message ->
                            settingsState.setBoolean("confirmed", false)
                            settingsState.setString("auth_method", "none")
                            settingsState.setString("message", message)
                            statusText = message
                        }
                    )
                }
            ) {
                Text("Request confirmation")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = statusText,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    override fun buildOutput(
        settingsState: SettingsState
    ): CapabilityOutput {
        return CapabilityOutput(
            fields = mapOf(
                "confirmed" to settingsState.getBoolean("confirmed"),
                "auth_method" to settingsState.getString("auth_method").ifBlank { "none" },
                "timestamp_ms" to settingsState.getInt("timestamp_ms"),
                "timestamp_iso" to settingsState.getString("timestamp_iso"),
                "reason" to settingsState.getString("confirmation_reason"),
                "message" to settingsState.getString("message")
            )
        )
    }

    @Composable
    override fun Help() {
        Text(
            "Requests confirmation using Android BiometricPrompt. " +
                "Depending on device settings, this can use fingerprint, face unlock, PIN, pattern, or password. " +
                "No fingerprint data is accessed or stored by XLSForm Lab."
        )
    }

    override fun execute(
        request: CapabilityRequest
    ): CapabilityResult {
        return CapabilityResult(success = true)
    }
}
