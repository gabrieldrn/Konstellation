package dev.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderBottom
import androidx.compose.material.icons.filled.BorderInner
import androidx.compose.material.icons.filled.BorderLeft
import androidx.compose.material.icons.filled.BorderOuter
import androidx.compose.material.icons.filled.BorderRight
import androidx.compose.material.icons.filled.BorderTop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.plotting.Axes
import dev.gabrieldrn.konstellation.plotting.ChartAxis
import dev.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import dev.gabrieldrn.konstellationdemo.ui.composables.toggleIconButtonSize
import kotlin.reflect.KProperty1

/**
 * A setting composable that allows the user to select which axes to draw and whether to draw the
 * frame and zero lines.
 */
@Composable
fun LineChartAxisSelectorSetting(
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
                        if (toggled) {
                            add(axis)
                        } else {
                            remove(axis)
                        }
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
                t.place(IntOffset(layoutSize / 2 - bSizeHalf, 0))
                b.place(IntOffset(layoutSize / 2 - bSizeHalf, layoutSize - bSize))
                l.place(IntOffset(0, layoutSize / 2 - bSizeHalf))
                r.place(IntOffset(layoutSize - bSize, layoutSize / 2 - bSizeHalf))
            }
        }
    )
}
