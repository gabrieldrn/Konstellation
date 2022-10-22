package com.gabrieldrn.konstellationdemo.linechartdemo

import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.charts.line.configuration.setAxesColor
import com.gabrieldrn.konstellation.charts.line.configuration.setAxesTypeface
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellationdemo.QF_MAIN_TEXT_STYLE
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.lang.IllegalStateException

private val injector = object : KoinComponent {}

internal fun getChartProperties() = LineChartProperties(
    chartPaddingValues = PaddingValues(44.dp),
    axes = setOf(Axes.xBottom, Axes.yLeft),
    datasetOffsets = DatasetOffsets(
        xStartOffset = 2f,
        xEndOffset = 2f,
        yStartOffset = 1f,
        yEndOffset = 1f
    ),
//    drawFrame = false,
//    drawZeroLines = false
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
    highlightTextStyle = mainTextStyle.copy(
        textAlign = Paint.Align.CENTER
    )
).apply {
    lineStyle.color = MaterialTheme.colorScheme.primary
    pointStyle.color = MaterialTheme.colorScheme.primary
    textStyle.color = MaterialTheme.colorScheme.primary
    highlightLineStyle?.color = MaterialTheme.colorScheme.secondary
    highlightPointStyle.run {
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        radius = 6.dp
    }
    highlightTextStyle.color = MaterialTheme.colorScheme.onSecondaryContainer
    setAxesColor(MaterialTheme.colorScheme.outline)
    setAxesTypeface(mainTextStyle.typeface)
}
