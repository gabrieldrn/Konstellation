package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.lifecycle.ViewModel
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.configuration.properties.Smoothing
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.highlighting.HighlightLinePosition
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.plotting.toDataset
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import kotlin.reflect.KProperty1

class LineChartDemoViewModel(
    properties: LineChartProperties = getChartProperties()
) : ViewModel() {

    var dataset: Dataset by mutableStateOf(datasetOf())
        private set
    var properties: LineChartProperties by mutableStateOf(properties)
        private set

    init {
//        generateNewFancyDataset()
        dataset = listOf(
            1f to 1f,
            2f to 1f,
            3f to 4f,
            4f to 4f,
            5f to 2f,
            6f to 2f,
        ).toDataset()
    }

    fun generateNewFancyDataset() {
        dataset = randomFancyDataSet()
    }

    fun generateNewRandomDataset() {
        dataset = randomDataSet()
    }

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
            LineChartProperties::smoothing -> properties
                .copy(smoothing = newValue as Smoothing)
            LineChartProperties::fillingBrush -> properties
                .copy(fillingBrush = newValue as Brush?)
            else -> properties
        }
    }
}
