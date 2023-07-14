package dev.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.util.distance
import dev.gabrieldrn.konstellation.util.inc
import dev.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import dev.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import kotlin.reflect.KProperty1

/**
 * This composable will be used to display the settings related to the chart properties.
 */
@Suppress("MagicNumber")
@Composable
fun LineChartPropertiesSetting(
    chartPaddingValues: PaddingValues,
    chartWindow: ChartWindow,
    chartInitialWindow: ChartWindow,
    panningEnabled: Boolean,
    onUpdateProperty: (KProperty1<LineChartProperties, Any?>, Any?) -> Unit,
    modifier: Modifier = Modifier
) {
    val paddingValue = { padding: PaddingValues ->
        padding.calculateTopPadding().value
    }
    val xWindowOffset = { window: ChartWindow ->
        chartInitialWindow.xWindow.start - window.xWindow.start
    }
    val yWindowOffset = { window: ChartWindow ->
        chartInitialWindow.yWindow.start - window.yWindow.start
    }
    SettingSurface(title = "Properties", modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Padding\n${paddingValue(chartPaddingValues).toInt()}dp",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Slider(
                        value = paddingValue(chartPaddingValues),
                        valueRange = 0f..120f,
                        onValueChange = {
                            onUpdateProperty(
                                LineChartProperties::chartPaddingValues,
                                PaddingValues(it.dp)
                            )
                        },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "X offset\n${xWindowOffset(chartWindow).toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Slider(
                        value = xWindowOffset(chartWindow),
                        valueRange = chartInitialWindow.xWindow,
                        onValueChange = {
                            onUpdateProperty(
                                LineChartProperties::chartWindow,
                                chartWindow.copy(
                                    xWindow = chartInitialWindow.xWindow.inc(it)
                                ),
                            )
                        },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Y offset\n${yWindowOffset(chartWindow).toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Slider(
                        value = yWindowOffset(chartWindow),
                        valueRange = 0f..chartInitialWindow.yWindow.distance * 2,
                        onValueChange = {
                            onUpdateProperty(
                                LineChartProperties::chartWindow,
                                chartWindow.copy(
                                    yWindow = chartInitialWindow.yWindow.inc(it)
                                ),
                            )
                        },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 16.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            )
            ToggleIconButton(
                toggled = panningEnabled,
                onToggleChange = { onUpdateProperty(LineChartProperties::panningEnabled, it) },
                imageVector = Icons.Default.PanTool,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Preview
@Composable
private fun SettingsPreviews() {
    KonstellationTheme {
        LineChartPropertiesSetting(
            chartPaddingValues = PaddingValues(44.dp),
            chartWindow = ChartWindow(0f..1f, 0f..1f),
            chartInitialWindow = ChartWindow(0f..1f, 0f..1f),
            panningEnabled = true,
            onUpdateProperty = { _, _ -> }
        )
    }
}
