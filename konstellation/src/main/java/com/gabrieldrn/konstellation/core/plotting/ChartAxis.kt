package com.gabrieldrn.konstellation.core.plotting

import android.graphics.Paint
import com.gabrieldrn.konstellation.style.AxisDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle

sealed class ChartAxis(val axis: Axis, val tickCount: Int, val style: AxisDrawStyle) {

    class XBottom : ChartAxis(
        axis = Axis.X_BOTTOM,
        tickCount = 5,
        style = AxisDrawStyle(
            tickTextStyle = TextDrawStyle(
                textAlign = Paint.Align.CENTER
            )
        )
    )

    class XTop : ChartAxis(
        axis = Axis.X_TOP,
        tickCount = 5,
        style = AxisDrawStyle(
            tickTextStyle = TextDrawStyle(
                textAlign = Paint.Align.CENTER
            )
        )
    )

    class YLeft : ChartAxis(
        axis = Axis.Y_LEFT,
        tickCount = 5,
        style = AxisDrawStyle(
            tickTextStyle = TextDrawStyle(
                textAlign = Paint.Align.RIGHT
            )
        )
    )

    class YRight : ChartAxis(
        axis = Axis.Y_RIGHT,
        tickCount = 5,
        style = AxisDrawStyle(
            tickTextStyle = TextDrawStyle(
                textAlign = Paint.Align.LEFT
            )
        )
    )

    enum class Axis {
        X_BOTTOM,
        X_TOP,
        Y_LEFT,
        Y_RIGHT,
    }
}
