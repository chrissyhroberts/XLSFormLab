package com.example.xlsformlab.modules.calibratedscale

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.CapabilityCategory
import com.example.xlsformlab.core.CapabilityManifest
import com.example.xlsformlab.core.CapabilityRequest
import com.example.xlsformlab.core.CapabilityResult
import com.example.xlsformlab.core.CapabilityStatus
import com.example.xlsformlab.settings.CapabilitySetting
import com.example.xlsformlab.settings.SettingsState

class CalibratedScaleCapability : Capability {

    override val manifest = CapabilityManifest(
        id = "calibrated_scale",
        name = "Calibrated Scale",
        description = "A configurable visual analogue and numeric scale.",
        version = "1.0.0",
        category = CapabilityCategory.Measurement,
        status = CapabilityStatus.Experimental
    )

    override val settings = listOf(
        CapabilitySetting.TextSetting(
            id = "question",
            label = "Question",
            defaultValue = "Rate your pain"
        ),
        CapabilitySetting.FloatSetting(
            id = "minimum",
            label = "Minimum",
            defaultValue = 0f,
            minimum = 0f,
            maximum = 100f
        ),
        CapabilitySetting.FloatSetting(
            id = "maximum",
            label = "Maximum",
            defaultValue = 100f,
            minimum = 0f,
            maximum = 100f
        ),
        CapabilitySetting.BooleanSetting(
            id = "show_numbers",
            label = "Show numbers",
            defaultValue = true
        ),
        CapabilitySetting.ChoiceSetting(
            id = "orientation",
            label = "Orientation",
            defaultValue = "Horizontal",
            choices = listOf("Horizontal", "Vertical")
        )
    )

    @Composable
    override fun Demo(settingsState: SettingsState) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(settingsState.getString("question"))

            Slider(
                value = 50f,
                onValueChange = {},
                valueRange = settingsState.getFloat("minimum")..
                    settingsState.getFloat("maximum"),
                modifier = Modifier.padding(top = 8.dp)
            )

            if (settingsState.getBoolean("show_numbers")) {
                Text(
                    text = "${settingsState.getFloat("minimum")} – ${settingsState.getFloat("maximum")}",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }

    @Composable
    override fun Help() {
        Text("Help coming soon")
    }

    override fun execute(
        request: CapabilityRequest
    ): CapabilityResult {
        return CapabilityResult(success = true)
    }
}