package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.runtime.*
import com.gabrieldrn.konstellation.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellationdemo.ui.viewmodel.ChartViewModel

class LineChartDemoViewModel : ChartViewModel() {

    var highlightPositions by mutableStateOf(setOf(HighlightPosition.POINT))
        private set

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
