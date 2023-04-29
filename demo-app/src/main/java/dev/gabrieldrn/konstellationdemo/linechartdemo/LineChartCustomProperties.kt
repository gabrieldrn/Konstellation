package dev.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import dev.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.PointDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import dev.gabrieldrn.konstellation.configuration.styles.updateColor
import dev.gabrieldrn.konstellation.configuration.styles.updateTypeface
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellation.plotting.Axes
import dev.gabrieldrn.konstellationdemo.QF_MAIN_TEXT_STYLE
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private val injector = object : KoinComponent {}

/**
 * Returns a [LineChartProperties] instance with some custom properties for demo purposes.
 */
@Suppress("MagicNumber")
fun getDemoChartProperties() = LineChartProperties(
    highlightContentPositions = setOf(HighlightContentPosition.Top, HighlightContentPosition.Start),
    hapticHighlight = true,
    drawFrame = false,
    drawPoints = true
)

/**
 * Returns a [LineChartStyles] instance with some custom styles for demo purposes.
 */
@Composable
fun getDemoChartStyles(
    mainTextStyle: TextDrawStyle = injector.get(QF_MAIN_TEXT_STYLE)
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
