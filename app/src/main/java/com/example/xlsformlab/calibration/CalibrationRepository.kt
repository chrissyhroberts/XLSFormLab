package com.example.xlsformlab.calibration

object CalibrationRepository {

    private var calibration = DeviceCalibration()

    fun current(): DeviceCalibration {
        return calibration
    }

    fun update(dpPerMm: Float) {
        calibration = DeviceCalibration(
            dpPerMm = dpPerMm,
            calibrated = true
        )
    }
}