package com.gabrieldrn.konstellation.style

data class AxisDrawStyle(
    val axisLineStyle: LineDrawStyle = LineDrawStyle(),
    val tickLineStyle: LineDrawStyle = LineDrawStyle(),
    val tickTextStyle: TextDrawStyle = TextDrawStyle(),
)
