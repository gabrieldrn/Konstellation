package com.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.charts.line.configuration.ChartWindow
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import kotlin.reflect.KProperty1

/**
 * This composable will be used to display the settings related to the chart paddings.
 */
@Suppress("MagicNumber", "UNUSED_PARAMETER")
@Composable
fun LineChartPaddingsSetting(
    datasetXRange: ClosedFloatingPointRange<Float>,
    datasetYRange: ClosedFloatingPointRange<Float>,
    chartPaddingValues: PaddingValues,
    chartWindow: ChartWindow?,
    onUpdateProperty: (KProperty1<LineChartProperties, Any?>, Any?) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Paddings", modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Padding\n${chartPaddingValues.calculateTopPadding().value.toInt()}dp",
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
    }
}
