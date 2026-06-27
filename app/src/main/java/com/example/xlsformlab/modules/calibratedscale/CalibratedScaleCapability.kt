package com.example.xlsformlab.modules.calibratedscale

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.xlsformlab.core.Capability
import com.example.xlsformlab.core.CapabilityCategory
import com.example.xlsformlab.core.CapabilityManifest
import com.example.xlsformlab.core.CapabilityRequest
import com.example.xlsformlab.core.CapabilityResult
import com.example.xlsformlab.core.CapabilityStatus

class CalibratedScaleCapability : Capability {

    override val manifest = CapabilityManifest(
        id = "calibrated_scale",
        name = "Calibrated Scale",
        description = "A configurable visual analogue and numeric scale.",
        version = "1.0.0",
        category = CapabilityCategory.Measurement,
        status = CapabilityStatus.Experimental
    )

    @Composable
    override fun Demo() {
        Text("Calibrated Scale demo coming next")
    }

    @Composable
    override fun Settings() {
        Text("Settings coming soon")
    }

    @Composable
    override fun Help() {
        Text("Help coming soon")
    }

    override fun execute(request: CapabilityRequest): CapabilityResult {
        return CapabilityResult(success = true)
    }
}
