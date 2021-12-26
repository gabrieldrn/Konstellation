package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.runtime.*
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellationdemo.ui.viewmodel.ChartViewModel

class LineChartDemoViewModel(
    properties: LineChartProperties = LineChartProperties()
) : ChartViewModel() {

    var highlightPositions by mutableStateOf(setOf(HighlightPosition.POINT))
        private set

    var properties by mutableStateOf(properties)

    init {
        dataset = randomFancyDataSet()
    }

    fun generateNewFancyDataset() {
        dataset = randomFancyDataSet()
    }

    fun generateNewRandomDataset() {
        dataset = randomDataSet()
    }

    fun setNewHighlightPositions(positions: Set<HighlightPosition>) {
        highlightPositions = positions
    }
}
