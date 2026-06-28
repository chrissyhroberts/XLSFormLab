package com.example.xlsformlab.calibration

import androidx.compose.runtime.mutableStateOf

object CalibrationRepository {

    var calibration = mutableStateOf(DeviceCalibration())
        private set

    fun current(): DeviceCalibration {
        return calibration.value
    }

    fun update(dpPerMm: Float) {
        calibration.value = DeviceCalibration(
            dpPerMm = dpPerMm.coerceIn(1f, 10f),
            calibrated = true
        )
    }
}