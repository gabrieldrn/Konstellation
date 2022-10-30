package com.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.configuration.properties.Smoothing
import com.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import kotlin.reflect.KProperty1

@Composable
internal fun LineChartDataDrawingSetting(
    drawLines: Boolean,
    drawPoints: Boolean,
    smoothing: Smoothing,
    onUpdateProperty: (KProperty1<LineChartProperties, Any>, Any) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Data drawing", modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ToggleIconButton(
                    toggled = drawLines,
                    onToggleChange = {
                        onUpdateProperty(LineChartProperties::drawLines, it)
                    },
                    imageVector = Icons.Outlined.ShowChart
                )
                Text(text = "Lines", textAlign = TextAlign.Center)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ToggleIconButton(
                    toggled = drawPoints,
                    onToggleChange =
                    { onUpdateProperty(LineChartProperties::drawPoints, it)
                    },
                    imageVector = Icons.Outlined.Commit
                )
                Text(text = "Points", textAlign = TextAlign.Center)
            }

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val methods by remember { mutableStateOf(Smoothing.values()) }
                SettingIconButton(
                    onClick = {
                        val index = (methods.indexOf(smoothing) + 1)
                            .takeIf { it in methods.indices } ?: 0
                        onUpdateProperty(LineChartProperties::smoothing, methods[index])
                    },
                    icon = Icons.Default.AutoAwesome,
                    text = "Smoothing"
                )
            }
        }
    }
}
