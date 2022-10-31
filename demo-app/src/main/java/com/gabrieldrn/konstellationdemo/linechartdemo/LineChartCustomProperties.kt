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
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellationdemo.QF_MAIN_TEXT_STYLE
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private val injector = object : KoinComponent {}

internal fun getChartProperties() = LineChartProperties(
    highlightContentPositions = setOf(HighlightContentPosition.Top, HighlightContentPosition.Start),
    datasetOffsets = DatasetOffsets(
        xStartOffset = 0f,
        xEndOffset = 0f,
        yStartOffset = 4000f,
        yEndOffset = 4000f
    ),
    drawFrame = false,
    drawZeroLines = false
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
