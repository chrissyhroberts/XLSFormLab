package com.example.xlsformlab

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.activity.enableEdgeToEdge
import com.example.xlsformlab.calibration.CalibrationRepository
import com.example.xlsformlab.ui.HomeScreen
import com.example.xlsformlab.ui.theme.XLSFormLabTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CalibrationRepository.initialise(applicationContext)

        enableEdgeToEdge()
        setContent {
            XLSFormLabTheme {
                HomeScreen()
            }
        }
    }
}
