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
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import kotlin.reflect.KProperty1

/**
 * This composable will be used to display the settings related to the chart properties.
 */
@Suppress("MagicNumber", "UNUSED_PARAMETER")
@Composable
fun LineChartPropertiesSetting(
//    datasetXRange: ClosedFloatingPointRange<Float>,
//    datasetYRange: ClosedFloatingPointRange<Float>,
    chartPaddingValues: PaddingValues,
    chartWindow: ChartWindow?,
    panningEnabled: Boolean,
    onUpdateProperty: (KProperty1<LineChartProperties, Any?>, Any?) -> Unit,
    modifier: Modifier = Modifier
) {
    val paddingValue = { padding: PaddingValues ->
        padding.calculateTopPadding().value.toInt()
    }
    SettingSurface(title = "Properties", modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Padding\n${paddingValue(chartPaddingValues)}dp",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Slider(
                        value = chartPaddingValues.calculateTopPadding().value,
                        valueRange = 0f..100f,
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
                        text = "X offset\n${0}", // TODO
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Slider(
                        enabled = false,
                        value = 0f, // TODO
                        valueRange = 0f..10f,
                        onValueChange = { TODO() },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Y offset\n${0}", // TODO
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Slider(
                        enabled = false,
                        value = 0f, // TODO
                        valueRange = 0f..10f,
                        onValueChange = { TODO() },
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
