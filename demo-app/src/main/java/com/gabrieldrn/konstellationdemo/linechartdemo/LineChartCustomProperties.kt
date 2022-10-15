package com.gabrieldrn.konstellationdemo.linechartdemo

import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
        yStartOffset = 0.5f,
        yEndOffset = 0.5f
    )
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
        color = MaterialTheme.colors.primary,
        textAlign = Paint.Align.CENTER,
        offsetY = -25f
    )
).apply {
    lineStyle.color = MaterialTheme.colors.primaryVariant
    pointStyle.color = MaterialTheme.colors.primaryVariant
    textStyle.color = MaterialTheme.colors.onBackground
    highlightLineStyle?.color = MaterialTheme.colors.onBackground
    highlightPointStyle.run {
        color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.3f)
        radius = 6.dp
    }
    setAxesColor(MaterialTheme.colors.onBackground)
    setAxesTypeface(mainTextStyle.typeface)
}
