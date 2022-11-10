package com.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import kotlin.reflect.KProperty1

@Composable
internal fun LineChartPaddingsSetting(
    chartPaddingValues: PaddingValues,
    datasetOffsets: DatasetOffsets?,
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
                    text = "X offset\n${datasetOffsets?.xStartOffset?.toInt() ?: 0}",
                    style = MaterialTheme.typography.bodySmall,
                )
                Slider(
                    value = datasetOffsets?.xStartOffset ?: 0f,
                    valueRange = 0f..100f,
                    onValueChange = {
                        onUpdateProperty(
                            LineChartProperties::datasetOffsets,
                            datasetOffsets?.copy(xStartOffset = it, xEndOffset = it)
                        )
                    },
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Y offset\n${datasetOffsets?.yStartOffset?.toInt() ?: 0}",
                    style = MaterialTheme.typography.bodySmall,
                )
                Slider(
                    value = datasetOffsets?.yStartOffset ?: 0f,
                    valueRange = 0f..5000f,
                    onValueChange = {
                        onUpdateProperty(
                            LineChartProperties::datasetOffsets,
                            datasetOffsets?.copy(yStartOffset = it, yEndOffset = it)
                        )
                    },
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}