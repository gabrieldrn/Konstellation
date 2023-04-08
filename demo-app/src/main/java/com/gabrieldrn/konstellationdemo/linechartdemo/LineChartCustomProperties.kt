package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.charts.line.configuration.setAxesColor
import com.gabrieldrn.konstellation.charts.line.configuration.setAxesTypeface
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.charts.line.drawing.Smoothing
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellationdemo.QF_MAIN_TEXT_STYLE
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private val injector = object : KoinComponent {}

internal fun getChartProperties() = LineChartProperties(
    axes = setOf(),
    highlightContentPositions = setOf(HighlightContentPosition.Top, HighlightContentPosition.Start),
    chartPaddingValues = PaddingValues(),
    datasetOffsets = DatasetOffsets(0f, 0f, 5f, 5f),
    drawFrame = false,
    drawZeroLines = false,
    drawPoints = true,
    smoothing = Smoothing.MonotonicX
)

@Composable
@Suppress("SwallowedException") // No need of caught exception
internal fun getChartStyles(
    mainTextStyle: TextDrawStyle = try {
        injector.get(QF_MAIN_TEXT_STYLE)
    } catch (iae: IllegalStateException) {
        //Koin not initialized.
        TextDrawStyle()
    }
) = LineChartStyles().apply {
    lineStyle.color = MaterialTheme.colorScheme.primary
    pointStyle.color = MaterialTheme.colorScheme.primary
    textStyle.color = MaterialTheme.colorScheme.primary
    highlightLineStyle?.color = MaterialTheme.colorScheme.secondary
    highlightPointStyle.run {
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        radius = 6.dp
    }
    setAxesColor(MaterialTheme.colorScheme.outline)
    setAxesTypeface(mainTextStyle.typeface)
}
