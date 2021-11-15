package com.gabrieldrn.konstellation.style

import androidx.compose.ui.graphics.Color

data class AxisDrawStyle(
    var axisLineStyle: LineDrawStyle = LineDrawStyle(),
    var tickLineStyle: LineDrawStyle = LineDrawStyle(),
    var tickTextStyle: TextDrawStyle = TextDrawStyle(),
)

fun AxisDrawStyle.setColor(color: Color) {
    axisLineStyle.color = color
    tickLineStyle.color = color
    tickTextStyle.color = color
}
