package com.example.xlsformlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.xlsformlab.ui.HomeScreen
import com.example.xlsformlab.ui.theme.XLSFormLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XLSFormLabTheme {
                HomeScreen()
            }
        }
    }
}