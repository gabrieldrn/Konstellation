package com.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import kotlin.reflect.KProperty1

@Composable
internal fun LineChartFillingSetting(
    brush: Brush?,
    onUpdateProperty: (KProperty1<LineChartProperties, Any?>, Any?) -> Unit,
    modifier: Modifier = Modifier
) {
    val solidColor = SolidColor(MaterialTheme.colorScheme.primary.copy(alpha = .75f))
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, Color.Transparent)
    )
    SettingSurface(title = "Filling", modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            ToggleIconButton(
                toggled = brush is SolidColor,
                onToggleChange = {
                    onUpdateProperty(LineChartProperties::fillingBrush, solidColor)
                },
                imageVector = Icons.Default.FormatColorFill
            )
            ToggleIconButton(
                toggled = brush is ShaderBrush,
                onToggleChange = {
                    onUpdateProperty(LineChartProperties::fillingBrush, gradientBrush)
                },
                imageVector = Icons.Default.Gradient
            )
            ToggleIconButton(
                toggled = brush == null,
                onToggleChange = {
                    onUpdateProperty(LineChartProperties::fillingBrush, null)
                },
                imageVector = Icons.Default.FormatColorReset
            )
        }
    }
}
