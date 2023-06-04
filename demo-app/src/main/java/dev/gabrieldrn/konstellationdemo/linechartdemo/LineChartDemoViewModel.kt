package dev.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.by
import dev.gabrieldrn.konstellation.plotting.datasetOf
import dev.gabrieldrn.konstellation.plotting.yRange
import dev.gabrieldrn.konstellation.util.randomDataSet
import dev.gabrieldrn.konstellation.util.randomFancyDataSet
import dev.gabrieldrn.konstellation.util.distance
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
    properties: LineChartProperties = LineChartProperties(),
) : ViewModel() {

    /**
     * The current UI state.
     */
    var uiState: UiState by mutableStateOf(
        UiState(
            dataset = initialDataset,
            properties = properties.copy(
                chartWindow = getWindowFromDataset(initialDataset)
            )
        )
    )

    private fun getWindowFromDataset(dataset: Dataset): ChartWindow {
        val clearance = dataset.yRange.distance
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
    fun <T> updateProperty(property: KProperty1<LineChartProperties, T>, newValue: T) {
        when (property) {
            LineChartProperties::chartPaddingValues -> uiState.properties
                .copy(chartPaddingValues = newValue as PaddingValues)

            LineChartProperties::chartWindow -> uiState.properties
                .copy(chartWindow = newValue as ChartWindow)

            else -> null
        }?.let {
            uiState = uiState.copy(properties = it)
        }
    }

    /**
     * Data class representing the UI state of the LineChartDemoActivity.
     *
     * @property dataset The dataset to be drawn in the chart.
     * @property properties The properties of the chart.
     */
    data class UiState(
        val dataset: Dataset,
        val properties: LineChartProperties,
    )
}
