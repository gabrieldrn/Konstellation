package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.lifecycle.ViewModel
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.configuration.properties.Smoothing
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet

class LineChartDemoViewModel(
    properties: LineChartProperties = getChartProperties()
) : ViewModel() {

    var dataset: Dataset by mutableStateOf(datasetOf())
        private set
    var properties: LineChartProperties by mutableStateOf(properties)
        private set

    init {
        generateNewFancyDataset()
    }

    fun generateNewFancyDataset() {
        dataset = randomFancyDataSet()
    }

    fun generateNewRandomDataset() {
        dataset = randomDataSet()
    }

    fun addHighlightPosition(contentPosition: HighlightContentPosition) {
        properties = properties.copy(
            highlightContentPositions = properties.highlightContentPositions.toMutableSet().apply {
                add(contentPosition)
            }
        )
    }

    fun removeHighlightPosition(contentPosition: HighlightContentPosition) {
        properties = properties.copy(
            highlightContentPositions = properties.highlightContentPositions.toMutableSet().apply {
                remove(contentPosition)
            }
        )
    }

    fun addAxis(axis: ChartAxis) {
        properties = properties.copy(
            axes = properties.axes.toMutableSet().apply {
                add(axis)
            }
        )
    }

    fun removeAxis(axis: ChartAxis) {
        properties = properties.copy(
            axes = properties.axes.toMutableSet().apply {
                remove(axis)
            }
        )
    }

    fun updateDrawFrame(value: Boolean) {
        properties = properties.copy(drawFrame = value)
    }

    fun updateDrawZeroLines(value: Boolean) {
        properties = properties.copy(drawZeroLines = value)
    }

    fun updateDrawLines(value: Boolean) {
        properties = properties.copy(drawLines = value)
    }

    fun updateDrawPoints(value: Boolean) {
        properties = properties.copy(drawPoints = value)
    }

    fun changeSmoothing(smoothing: Smoothing) {
        properties = properties.copy(smoothing = smoothing)
    }

    fun changeFillingBrush(brush: Brush?) {
        properties = properties.copy(fillingBrush = brush)
    }
}
