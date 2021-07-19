package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Paint
import com.gabrieldrn.konstellation.style.AxisDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

data class ChartAxis(var axis: Axis, var tickCount: Int, var style: AxisDrawStyle)

enum class Axis {
    X_BOTTOM,
    X_TOP,
    Y_LEFT,
    Y_RIGHT,
}

val xBottom = ChartAxis(
    axis = Axis.X_BOTTOM,
    tickCount = 5,
    style = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.CENTER
        )
    )
)

val xTop = ChartAxis(
    axis = Axis.X_TOP,
    tickCount = 5,
    style = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.CENTER
        )
    )
)

val yLeft = ChartAxis(
    axis = Axis.Y_LEFT,
    tickCount = 5,
    style = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.RIGHT
        )
    )
)

val yRight = ChartAxis(
    axis = Axis.Y_RIGHT,
    tickCount = 5,
    style = AxisDrawStyle(
        tickTextStyle = TextDrawStyle(
            textAlign = Paint.Align.LEFT
        )
    )
)
