package dev.gabrieldrn.konstellationdemo.linechartdemo.settings

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
import dev.gabrieldrn.konstellation.charts.line.math.CubicXPathInterpolator
import dev.gabrieldrn.konstellation.charts.line.math.CubicYPathInterpolator
import dev.gabrieldrn.konstellation.charts.line.math.LinearPathInterpolator
import dev.gabrieldrn.konstellation.charts.line.math.MonotoneXPathInterpolator
import dev.gabrieldrn.konstellation.charts.line.style.LineChartStyles
import dev.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton

/**
 * A setting composable that allows the user to select whether to draw lines and points and which
 * path interpolator to use.
 */
@Composable
fun LineChartDataDrawingSetting(
    styles: LineChartStyles,
    onUpdateStyles: (LineChartStyles) -> Unit,
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
                    toggled = styles.drawLines,
                    onToggleChange = {
                        onUpdateStyles(styles.copy(drawLines = it))
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
                    toggled = styles.drawPoints,
                    onToggleChange =
                    {
                        onUpdateStyles(styles.copy(drawPoints = it))
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
                            .indexOfFirst { it::class.java == styles.pathInterpolator::class.java }
                            .plus(1)
                            .takeIf { it in interpolators.indices }
                            ?: 0
                        onUpdateStyles(
                            styles.copy(pathInterpolator = interpolators[index])
                        )
                    },
                    icon = Icons.Default.AutoAwesome,
                    text = "Smoothing"
                )
            }
        }
    }
}
