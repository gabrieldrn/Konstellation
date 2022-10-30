package com.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import com.gabrieldrn.konstellationdemo.ui.composables.toggleIconButtonSize
import kotlin.reflect.KProperty1

@Composable
internal fun LineChartAxisSelectorSetting(
    axes: Set<ChartAxis>,
    drawFrame: Boolean,
    drawZeroLines: Boolean,
    onUpdateProperty: (KProperty1<LineChartProperties, Any>, Any) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Axes & framing", modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AxesSelector(
                axes = axes,
                onUpdateProperty = onUpdateProperty,
                modifier = Modifier.weight(1f)
            )
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                ToggleIconButton(
                    toggled = drawFrame,
                    onToggleChange = {
                        onUpdateProperty(LineChartProperties::drawFrame, it)
                    },
                    imageVector = Icons.Default.BorderOuter
                )
                ToggleIconButton(
                    toggled = drawZeroLines,
                    onToggleChange = {
                        onUpdateProperty(LineChartProperties::drawZeroLines, it)
                    },
                    imageVector = Icons.Default.BorderInner
                )
            }
        }
    }
}

@Composable
private fun AxesSelector(
    axes: Set<ChartAxis>,
    onUpdateProperty: (KProperty1<LineChartProperties, Any>, Any) -> Unit,
    modifier: Modifier = Modifier
) {
    @Composable
    fun AxisToggleButton(
        modifier: Modifier,
        axis: ChartAxis,
        imageVector: ImageVector
    ) {
        ToggleIconButton(
            modifier = modifier,
            toggled = axes.contains(axis),
            onToggleChange = { toggled ->
                onUpdateProperty(
                    LineChartProperties::axes,
                    axes.toMutableSet().apply {
                        if (toggled) add(axis)
                        else remove(axis)
                    }
                )
            },
            imageVector = imageVector
        )
    }

    Layout(
        modifier = modifier.fillMaxSize(),
        content = {
            AxisToggleButton(Modifier.layoutId("T"), Axes.xTop, Icons.Default.BorderTop)
            AxisToggleButton(Modifier.layoutId("B"), Axes.xBottom, Icons.Default.BorderBottom)
            AxisToggleButton(Modifier.layoutId("L"), Axes.yLeft, Icons.Default.BorderLeft)
            AxisToggleButton(Modifier.layoutId("R"), Axes.yRight, Icons.Default.BorderRight)
        },
        measurePolicy = { measurables, constraints ->
            val bSize = toggleIconButtonSize.roundToPx()
            val bSizeHalf = bSize / 2
            val bSizeCons = constraints.copy(bSize, bSize, bSize, bSize)
            val t = measurables.first { it.layoutId == "T" }.measure(bSizeCons)
            val b = measurables.first { it.layoutId == "B" }.measure(bSizeCons)
            val l = measurables.first { it.layoutId == "L" }.measure(bSizeCons)
            val r = measurables.first { it.layoutId == "R" }.measure(bSizeCons)
            val layoutSize = 116.dp.roundToPx()

            layout(layoutSize, layoutSize) {
                t.place(IntOffset((layoutSize / 2) - bSizeHalf, 0))
                b.place(IntOffset((layoutSize / 2) - bSizeHalf, layoutSize - bSize))
                l.place(IntOffset(0, (layoutSize / 2) - bSizeHalf))
                r.place(IntOffset(layoutSize - bSize, (layoutSize / 2) - bSizeHalf))
            }
        }
    )
}
