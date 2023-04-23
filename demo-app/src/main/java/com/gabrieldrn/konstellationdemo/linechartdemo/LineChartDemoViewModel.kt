package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.ViewModel
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.math.PathInterpolator
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.highlighting.HighlightLinePosition
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.by
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import kotlin.reflect.KProperty1

/**
 * ViewModel for the LineChartDemoActivity.
 */
class LineChartDemoViewModel(
    properties: LineChartProperties
) : ViewModel() {

    /**
     * The dataset to be used in the LineChart.
     */
    var dataset: Dataset by mutableStateOf(
        datasetOf(
            -3f by -1f,
            -2f by 0f,
            -1f by 0f,
            0f by 2f,
            1f by 2f,
            2f by 1f,
            3f by 1f,
        )
    ) ; private set

    /**
     * The properties to be used in the LineChart.
     */
    var properties: LineChartProperties by mutableStateOf(properties)
        private set

    /**
     * Generates a new random "fancy" dataset.
     */
    fun generateNewFancyDataset() {
        dataset = randomFancyDataSet()
    }

    /**
     * Generates a new random dataset.
     */
    fun generateNewRandomDataset() {
        dataset = randomDataSet()
    }

    /**
     * Updates the [properties] with the given [newValue] for the given [property].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> updateProperty(property: KProperty1<LineChartProperties, T>, newValue: T) {
        properties = when (property) {
            LineChartProperties::axes -> properties
                .copy(axes = newValue as Set<ChartAxis>)

            LineChartProperties::chartPaddingValues -> properties
                .copy(chartPaddingValues = newValue as PaddingValues)

            LineChartProperties::datasetOffsets -> properties
                .copy(datasetOffsets = newValue as DatasetOffsets)

            LineChartProperties::highlightContentPositions -> properties
                .copy(highlightContentPositions = newValue as Set<HighlightContentPosition>)

            LineChartProperties::highlightLinePosition -> properties
                .copy(highlightLinePosition = newValue as HighlightLinePosition)

            LineChartProperties::hapticHighlight -> properties
                .copy(hapticHighlight = newValue as Boolean)

            LineChartProperties::drawFrame -> properties
                .copy(drawFrame = newValue as Boolean)

            LineChartProperties::drawZeroLines -> properties
                .copy(drawZeroLines = newValue as Boolean)

            LineChartProperties::drawLines -> properties
                .copy(drawLines = newValue as Boolean)

            LineChartProperties::drawPoints -> properties
                .copy(drawPoints = newValue as Boolean)

            LineChartProperties::pathInterpolator -> properties
                .copy(pathInterpolator = newValue as PathInterpolator)

            LineChartProperties::fillingBrush -> properties
                .copy(fillingBrush = newValue as? Brush?)

            else -> properties
        }
    }
}
