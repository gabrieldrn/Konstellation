package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet

class LineChartDemoViewModel(
    properties: LineChartProperties = LineChartProperties()
) : ViewModel() {

    var dataset: Dataset by mutableStateOf(datasetOf())
    var properties: LineChartProperties by mutableStateOf(properties)

    init {
        generateNewFancyDataset()
    }

    fun generateNewFancyDataset() {
        dataset = randomFancyDataSet()
    }

    fun generateNewRandomDataset() {
        dataset = randomDataSet()
    }

    fun addHighlightPosition(position: HighlightPosition) {
        properties = properties.copy(
            highlightPositions = properties.highlightPositions.toMutableSet().apply {
                add(position)
            }
        )
    }

    fun removeHighlightPosition(position: HighlightPosition) {
        properties = properties.copy(
            highlightPositions = properties.highlightPositions.toMutableSet().apply {
                remove(position)
            }
        )
    }
}
