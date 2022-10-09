package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.lifecycle.ViewModel
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.plotting.Axis
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet

class LineChartDemoViewModel(
    properties: LineChartProperties = LineChartProperties()
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

    fun updateDrawPoints(value: Boolean) {
        properties = properties.copy(drawPoints = value)
    }

    fun changeFillingBrush(brush: Brush?) {
        properties = properties.copy(fillingBrush = brush)
    }
}
