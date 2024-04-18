package dev.gabrieldrn.konstellation.charts.line.limitline

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import dev.gabrieldrn.konstellation.plotting.Axis

internal val baseZeroLimitLine = LimitLine(
    value = 0f,
    axis = Axis.X_BOTTOM,
    style = LineDrawStyle(
        color = Color.LightGray,
        strokeWidth = 1.5f.dp,
        cap = StrokeCap.Square,
    )
)

internal val zeroLimitLines = listOf(
    baseZeroLimitLine,
    baseZeroLimitLine.copy(axis = Axis.Y_LEFT)
)
