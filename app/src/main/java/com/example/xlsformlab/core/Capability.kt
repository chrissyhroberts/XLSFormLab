package com.example.xlsformlab.core

import androidx.compose.runtime.Composable

interface Capability {

    val manifest: CapabilityManifest

    @Composable
    fun Demo()

    @Composable
    fun Settings()

    @Composable
    fun Help()

    fun execute(
        request: CapabilityRequest
    ): CapabilityResult
}