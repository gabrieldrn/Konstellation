package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.charts.line.drawing.Smoothing
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.updateColor
import com.gabrieldrn.konstellation.configuration.styles.updateTypeface
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellationdemo.QF_MAIN_TEXT_STYLE
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private val injector = object : KoinComponent {}

@Suppress("MagicNumber")
internal fun getDemoChartProperties() = LineChartProperties(
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
) = LineChartStyles(
    lineStyle = LineDrawStyle(color = MaterialTheme.colorScheme.primary),
    pointStyle = PointDrawStyle(color = MaterialTheme.colorScheme.primary),
    xAxisBottomStyle = Axes.xBottomStyle
        .updateColor(MaterialTheme.colorScheme.outline)
        .updateTypeface(mainTextStyle.typeface),
    xAxisTopStyle = Axes.xTopStyle
        .updateColor(MaterialTheme.colorScheme.outline)
        .updateTypeface(mainTextStyle.typeface),
    yAxisLeftStyle = Axes.yLeftStyle
        .updateColor(MaterialTheme.colorScheme.outline)
        .updateTypeface(mainTextStyle.typeface),
    yAxisRightStyle = Axes.yRightStyle
        .updateColor(MaterialTheme.colorScheme.outline)
        .updateTypeface(mainTextStyle.typeface),
    textStyle = TextDrawStyle(color = MaterialTheme.colorScheme.primary),
    highlightLineStyle = LineDrawStyle(
        strokeWidth = 1.dp,
        dashed = true,
        color = MaterialTheme.colorScheme.secondary
    ),
    highlightPointStyle = PointDrawStyle(
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
        radius = 6.dp
    )
)
