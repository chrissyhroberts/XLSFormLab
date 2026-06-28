package com.example.xlsformlab.modules.calibratedscale

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
        CapabilitySetting.TextSetting("prompt", "Prompt", defaultValue = "Rate your pain"),
        CapabilitySetting.FloatSetting("minimum", "Minimum value", defaultValue = 0f, minimum = 0f, maximum = 100f),
        CapabilitySetting.FloatSetting("maximum", "Maximum value", defaultValue = 100f, minimum = 0f, maximum = 100f),
        CapabilitySetting.BooleanSetting("use_range", "Use two scales", defaultValue = false),
        CapabilitySetting.TextSetting("lower_label", "Lower scale label", defaultValue = "Minimum selected value"),
        CapabilitySetting.TextSetting("upper_label", "Upper scale label", defaultValue = "Maximum selected value"),
        CapabilitySetting.FloatSetting("value", "Current value", defaultValue = 50f, minimum = 0f, maximum = 100f),
        CapabilitySetting.FloatSetting("lower_value", "Lower selected value", defaultValue = 25f, minimum = 0f, maximum = 100f),
        CapabilitySetting.FloatSetting("upper_value", "Upper selected value", defaultValue = 75f, minimum = 0f, maximum = 100f),
        CapabilitySetting.IntSetting("display_scale_percent", "Display scale (%)", defaultValue = 100, minimum = 50, maximum = 200),
        CapabilitySetting.BooleanSetting("show_endpoint_labels", "Show endpoint labels", defaultValue = true),
        CapabilitySetting.BooleanSetting("show_current_score", "Show current score", defaultValue = true),
        CapabilitySetting.BooleanSetting("vertical_mode", "Vertical mode", defaultValue = false)
    )

    @Composable
    override fun Demo(settingsState: SettingsState) {
        val minimum = settingsState.getFloat("minimum")
        val maximum = settingsState.getFloat("maximum")
        val useRange = settingsState.getBoolean("use_range")
        val displayScale = settingsState.getInt("display_scale_percent").coerceIn(50, 200) / 100f
        val showEndpointLabels = settingsState.getBoolean("show_endpoint_labels")
        val showCurrentScore = settingsState.getBoolean("show_current_score")
        val verticalMode = settingsState.getBoolean("vertical_mode")

        normaliseRangeValues(settingsState, minimum, maximum)

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(settingsState.getString("prompt"))

            Spacer(modifier = Modifier.height(12.dp))

            if (useRange) {
                if (verticalMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 420.dp * displayScale),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        ScaleInput(
                            label = settingsState.getString("lower_label"),
                            valueId = "lower_value",
                            settingsState = settingsState,
                            scaleMinimum = minimum,
                            scaleMaximum = maximum,
                            displayScale = displayScale,
                            showEndpointLabels = showEndpointLabels,
                            showCurrentScore = showCurrentScore,
                            vertical = true,
                            onValueChange = { proposed ->
                                val upper = settingsState.getFloat("upper_value")
                                settingsState.setFloat("lower_value", proposed.coerceIn(minimum, upper))
                            }
                        )

                        ScaleInput(
                            label = settingsState.getString("upper_label"),
                            valueId = "upper_value",
                            settingsState = settingsState,
                            scaleMinimum = minimum,
                            scaleMaximum = maximum,
                            displayScale = displayScale,
                            showEndpointLabels = showEndpointLabels,
                            showCurrentScore = showCurrentScore,
                            vertical = true,
                            onValueChange = { proposed ->
                                val lower = settingsState.getFloat("lower_value")
                                settingsState.setFloat("upper_value", proposed.coerceIn(lower, maximum))
                            }
                        )
                    }
                } else {
                    Column {
                        ScaleInput(
                            label = settingsState.getString("lower_label"),
                            valueId = "lower_value",
                            settingsState = settingsState,
                            scaleMinimum = minimum,
                            scaleMaximum = maximum,
                            displayScale = displayScale,
                            showEndpointLabels = showEndpointLabels,
                            showCurrentScore = showCurrentScore,
                            vertical = false,
                            onValueChange = { proposed ->
                                val upper = settingsState.getFloat("upper_value")
                                settingsState.setFloat("lower_value", proposed.coerceIn(minimum, upper))
                            }
                        )

                        ScaleInput(
                            label = settingsState.getString("upper_label"),
                            valueId = "upper_value",
                            settingsState = settingsState,
                            scaleMinimum = minimum,
                            scaleMaximum = maximum,
                            displayScale = displayScale,
                            showEndpointLabels = showEndpointLabels,
                            showCurrentScore = showCurrentScore,
                            vertical = false,
                            onValueChange = { proposed ->
                                val lower = settingsState.getFloat("lower_value")
                                settingsState.setFloat("upper_value", proposed.coerceIn(lower, maximum))
                            }
                        )
                    }
                }
            } else {
                ScaleInput(
                    label = "Current value",
                    valueId = "value",
                    settingsState = settingsState,
                    scaleMinimum = minimum,
                    scaleMaximum = maximum,
                    displayScale = displayScale,
                    showEndpointLabels = showEndpointLabels,
                    showCurrentScore = showCurrentScore,
                    vertical = verticalMode,
                    onValueChange = { proposed ->
                        settingsState.setFloat("value", proposed.coerceIn(minimum, maximum))
                    }
                )
            }
        }
    }

    private fun normaliseRangeValues(
        settingsState: SettingsState,
        minimum: Float,
        maximum: Float
    ) {
        val lower = settingsState.getFloat("lower_value").coerceIn(minimum, maximum)
        val upper = settingsState.getFloat("upper_value").coerceIn(minimum, maximum)

        if (lower > upper) {
            settingsState.setFloat("lower_value", upper)
            settingsState.setFloat("upper_value", lower)
        } else {
            settingsState.setFloat("lower_value", lower)
            settingsState.setFloat("upper_value", upper)
        }
    }

    @Composable
    private fun ScaleInput(
        label: String,
        valueId: String,
        settingsState: SettingsState,
        scaleMinimum: Float,
        scaleMaximum: Float,
        displayScale: Float,
        showEndpointLabels: Boolean,
        showCurrentScore: Boolean,
        vertical: Boolean,
        onValueChange: (Float) -> Unit
    ) {
        val current = settingsState.getFloat(valueId).coerceIn(scaleMinimum, scaleMaximum)
        val scaleLength = 280.dp * displayScale

        Column(
            modifier = if (vertical) {
                Modifier
                    .height(scaleLength + 170.dp)
                    .padding(8.dp)
            } else {
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .height(72.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    textAlign = TextAlign.Center,
                    maxLines = 3
                )

                if (showCurrentScore) {
                    Text(
                        text = "Score: ${current.toInt()}",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (vertical) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showEndpointLabels) {
                        Column(
                            modifier = Modifier
                                .height(scaleLength)
                                .width(36.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(scaleMaximum.toInt().toString())
                            Text(scaleMinimum.toInt().toString())
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    CanvasScale(
                        value = current,
                        minimum = scaleMinimum,
                        maximum = scaleMaximum,
                        length = scaleLength,
                        vertical = true,
                        onValueChange = onValueChange
                    )
                }
            } else {
                CanvasScale(
                    value = current,
                    minimum = scaleMinimum,
                    maximum = scaleMaximum,
                    length = scaleLength,
                    vertical = false,
                    onValueChange = onValueChange
                )

                if (showEndpointLabels) {
                    Row(
                        modifier = Modifier
                            .width(scaleLength)
                            .height(24.dp)
                    ) {
                        Text(scaleMinimum.toInt().toString())
                        Spacer(modifier = Modifier.weight(1f))
                        Text(scaleMaximum.toInt().toString())
                    }
                }
            }
        }
    }

    @Composable
    private fun CanvasScale(
        value: Float,
        minimum: Float,
        maximum: Float,
        length: Dp,
        vertical: Boolean,
        onValueChange: (Float) -> Unit
    ) {
        val modifier = if (vertical) {
            Modifier
                .width(56.dp)
                .height(length)
        } else {
            Modifier
                .width(length)
                .height(56.dp)
        }

        Box(
            modifier = modifier.pointerInput(minimum, maximum, vertical) {
                detectDragGestures(
                    onDragStart = { offset ->
                        onValueChange(
                            if (vertical) {
                                valueFromY(offset.y, size.height.toFloat(), minimum, maximum)
                            } else {
                                valueFromX(offset.x, size.width.toFloat(), minimum, maximum)
                            }
                        )
                    },
                    onDrag = { change, _ ->
                        onValueChange(
                            if (vertical) {
                                valueFromY(change.position.y, size.height.toFloat(), minimum, maximum)
                            } else {
                                valueFromX(change.position.x, size.width.toFloat(), minimum, maximum)
                            }
                        )
                    }
                )
            }
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val proportion = if (maximum > minimum) {
                    (value - minimum) / (maximum - minimum)
                } else {
                    0f
                }.coerceIn(0f, 1f)

                if (vertical) {
                    val x = size.width / 2f
                    val top = 0f
                    val bottom = size.height
                    val y = bottom - (bottom - top) * proportion

                    drawLine(
                        color = Color.Black,
                        start = Offset(x, top),
                        end = Offset(x, bottom),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Square
                    )

                    drawCircle(
                        color = Color.Black,
                        radius = 10.dp.toPx(),
                        center = Offset(x, y)
                    )
                } else {
                    val y = size.height / 2f
                    val start = 0f
                    val end = size.width
                    val x = start + (end - start) * proportion

                    drawLine(
                        color = Color.Black,
                        start = Offset(start, y),
                        end = Offset(end, y),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Square
                    )

                    drawCircle(
                        color = Color.Black,
                        radius = 10.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }
    }

    private fun valueFromX(
        x: Float,
        width: Float,
        minimum: Float,
        maximum: Float
    ): Float {
        val proportion = (x / width).coerceIn(0f, 1f)
        return minimum + proportion * (maximum - minimum)
    }

    private fun valueFromY(
        y: Float,
        height: Float,
        minimum: Float,
        maximum: Float
    ): Float {
        val proportion = (1f - (y / height)).coerceIn(0f, 1f)
        return minimum + proportion * (maximum - minimum)
    }

    @Composable
    override fun Help() {
        Text("Help coming soon")
    }

    override fun execute(request: CapabilityRequest): CapabilityResult {
        return CapabilityResult(success = true)
    }
}