package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.ViewModel
import com.gabrieldrn.konstellation.charts.line.configuration.ChartWindow
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.math.PathInterpolator
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.highlighting.HighlightLinePosition
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.by
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.plotting.yRange
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellation.util.rawRange
import kotlin.reflect.KProperty1

private val initialDataset = datasetOf(
    -3f by -1f,
    -2f by 0f,
    -1f by 0f,
    0f by 2f,
    1f by 2f,
    2f by 1f,
    3f by 1f,
)

/**
 * ViewModel for the LineChartDemoActivity.
 */
class LineChartDemoViewModel(
    properties: LineChartProperties
) : ViewModel() {

    /**
     * The current UI state.
     */
    var uiState: UiState by mutableStateOf(
        UiState(
            properties = properties.copy(
                chartWindow = getWindowFromDataset(initialDataset)
            ),
            dataset = initialDataset
        )
    )

    private fun getWindowFromDataset(dataset: Dataset): ChartWindow {
        val clearance = dataset.yRange.rawRange
        return ChartWindow.fromDataset(dataset).copy(
            yWindow = dataset.yRange.start - clearance..dataset.yRange.endInclusive + clearance,
        )
    }

    /**
     * Generates a new random "fancy" dataset.
     */
    fun generateNewFancyDataset() {
        val newDataset = randomFancyDataSet()
        uiState = uiState.copy(
            dataset = newDataset,
            properties = uiState.properties.copy(
                chartWindow = getWindowFromDataset(newDataset),
            ),
        )
    }

    /**
     * Generates a new random dataset.
     */
    fun generateNewRandomDataset() {
        val newDataset = randomDataSet()
        uiState = uiState.copy(
            dataset = newDataset,
            properties = uiState.properties.copy(
                chartWindow = getWindowFromDataset(newDataset),
            ),
        )
    }

    /**
     * Updates the chart properties with the given [newValue] for the given [property].
     */
    @Suppress("UNCHECKED_CAST", "CyclomaticComplexMethod")
    fun <T> updateProperty(property: KProperty1<LineChartProperties, T>, newValue: T) {
        when (property) {
            LineChartProperties::axes -> uiState.properties
                .copy(axes = newValue as Set<ChartAxis>)

            LineChartProperties::chartPaddingValues -> uiState.properties
                .copy(chartPaddingValues = newValue as PaddingValues)

            LineChartProperties::chartWindow -> uiState.properties
                .copy(chartWindow = newValue as ChartWindow)

            LineChartProperties::highlightContentPositions -> uiState.properties
                .copy(highlightContentPositions = newValue as Set<HighlightContentPosition>)

            LineChartProperties::highlightLinePosition -> uiState.properties
                .copy(highlightLinePosition = newValue as HighlightLinePosition)

            LineChartProperties::hapticHighlight -> uiState.properties
                .copy(hapticHighlight = newValue as Boolean)

            LineChartProperties::drawFrame -> uiState.properties
                .copy(drawFrame = newValue as Boolean)

            LineChartProperties::drawZeroLines -> uiState.properties
                .copy(drawZeroLines = newValue as Boolean)

            LineChartProperties::drawLines -> uiState.properties
                .copy(drawLines = newValue as Boolean)

            LineChartProperties::drawPoints -> uiState.properties
                .copy(drawPoints = newValue as Boolean)

            LineChartProperties::pathInterpolator -> uiState.properties
                .copy(pathInterpolator = newValue as PathInterpolator)

            LineChartProperties::fillingBrush -> uiState.properties
                .copy(fillingBrush = newValue as? Brush?)

            else -> null
        }?.let {
            uiState = uiState.copy(properties = it)
        }
    }

    /**
     * Data class representing the UI state of the LineChartDemoActivity.
     * @property dataset The dataset to be drawn in the chart.
     * @property properties The properties of the chart.
     */
    data class UiState(
        val dataset: Dataset,
        val properties: LineChartProperties,
    )
}
