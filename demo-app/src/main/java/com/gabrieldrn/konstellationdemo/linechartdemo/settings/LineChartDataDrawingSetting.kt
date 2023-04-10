package com.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.Commit
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.math.CubicXPathInterpolator
import com.gabrieldrn.konstellation.charts.line.math.CubicYPathInterpolator
import com.gabrieldrn.konstellation.charts.line.math.LinearPathInterpolator
import com.gabrieldrn.konstellation.charts.line.math.MonotoneXPathInterpolator
import com.gabrieldrn.konstellation.charts.line.math.PathInterpolator
import com.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import kotlin.reflect.KProperty1

@Composable
internal fun LineChartDataDrawingSetting(
    drawLines: Boolean,
    drawPoints: Boolean,
    interpolator: PathInterpolator,
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
                    {
                        onUpdateProperty(LineChartProperties::drawPoints, it)
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
                val interpolators by remember {
                    mutableStateOf(
                        listOf(
                            MonotoneXPathInterpolator(),
                            LinearPathInterpolator(),
                            CubicXPathInterpolator(),
                            CubicYPathInterpolator()
                        )
                    )
                }
                SettingIconButton(
                    onClick = {
                        val index = interpolators
                            .indexOfFirst { it::class.java == interpolator::class.java }
                            .plus(1)
                            .takeIf { it in interpolators.indices }
                            ?: 0
                        onUpdateProperty(
                            LineChartProperties::pathInterpolator, interpolators[index]
                        )
                    },
                    icon = Icons.Default.AutoAwesome,
                    text = "Smoothing"
                )
            }
        }
    }
}
