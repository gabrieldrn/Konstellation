package com.gabrieldrn.konstellationdemo.linechartdemo

import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.style.DatasetOffsets
import com.gabrieldrn.konstellation.charts.line.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.setAxesColor
import com.gabrieldrn.konstellation.charts.line.setAxisTypeface
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.style.TextDrawStyle

@Composable
internal fun getChartProperties(mainTextStyle: TextDrawStyle) = LineChartProperties().apply {
    lineStyle.color = MaterialTheme.colors.primary
    pointStyle.color = MaterialTheme.colors.primary
    textStyle.color = MaterialTheme.colors.primary
    highlightPointStyle.run {
        color = MaterialTheme.colors.primary.copy(alpha = 0.3f)
        radius = 6.dp
    }
    highlightLineStyle.color = MaterialTheme.colors.onBackground
    highlightTextStyle = mainTextStyle.copy(
        color = MaterialTheme.colors.primary,
        textAlign = Paint.Align.CENTER,
        offsetY = -25f
    )
    chartPaddingValues = PaddingValues(44.dp)
    axes = setOf(Axes.xBottom, Axes.xTop, Axes.yLeft, Axes.yRight)
    datasetOffsets = DatasetOffsets(
        xStartOffset = 2f,
        xEndOffset = 2f,
        yStartOffset = 0.5f,
        yEndOffset = 0.5f
    )
    setAxesColor(MaterialTheme.colors.onBackground)
    setAxisTypeface(mainTextStyle.typeface)
}
