package dev.gabrieldrn.konstellationdemo.linechartdemo

import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dev.gabrieldrn.konstellation.charts.line.limitline.LimitLine
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import dev.gabrieldrn.konstellation.plotting.Axis
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.Label
import dev.gabrieldrn.konstellation.plotting.by
import dev.gabrieldrn.konstellation.plotting.datasetOf
import dev.gabrieldrn.konstellation.plotting.xRange
import dev.gabrieldrn.konstellation.plotting.yRange
import dev.gabrieldrn.konstellation.util.distance
import dev.gabrieldrn.konstellation.util.inc
import dev.gabrieldrn.konstellation.util.randomDataSet
import dev.gabrieldrn.konstellation.util.randomFancyDataSet
import dev.gabrieldrn.konstellationdemo.QF_MAIN_TEXT_STYLE
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
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
) : ViewModel(), KoinComponent {

    private val limitLinesColor = Color(0xFFDB8505)
    private val limitLinesStyle = LineDrawStyle(dashed = true, color = limitLinesColor)
    private val limitLinesLabelStyle by lazy {
        get<TextDrawStyle>(QF_MAIN_TEXT_STYLE).copy(color = limitLinesColor)
    }

    /**
     * The current UI state.
     */
    var uiState: UiState by mutableStateOf(
        UiState(
            dataset = initialDataset,
            properties = properties.copy(
                chartWindow = getWindowFromDataset(initialDataset)
            )
        ).withYAverageLimitLine()
    ); private set

    private fun getWindowFromDataset(dataset: Dataset): ChartWindow {
        return ChartWindow.fromDataset(dataset).copy(
            xWindow = dataset.xRange.inc(/*clearance=*/ .25f),
            yWindow = dataset.yRange.inc(/*clearance=*/ dataset.yRange.distance)
        )
    }

    private fun UiState.withYAverageLimitLine(): UiState {
        val yAvg = dataset
            .map { it.y }
            .average()
            .toFloat()

        return copy(
            limitLines = listOf(
                LimitLine(
                    value = yAvg,
                    axis = Axis.Y_RIGHT,
                    style = limitLinesStyle,
                    label = Label(
                        text = "AVG",
                        style = limitLinesLabelStyle.copy(textAlign = Paint.Align.LEFT)
                    )
                ),
            )
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
        ).withYAverageLimitLine()
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
        ).withYAverageLimitLine()
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

            LineChartProperties::panningEnabled -> uiState.properties
                .copy(panningEnabled = newValue as Boolean)

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
     * @property limitLines The limit lines to be drawn in the chart.
     */
    data class UiState(
        val dataset: Dataset,
        val properties: LineChartProperties,
        val limitLines: List<LimitLine> = listOf(),
    )
}
