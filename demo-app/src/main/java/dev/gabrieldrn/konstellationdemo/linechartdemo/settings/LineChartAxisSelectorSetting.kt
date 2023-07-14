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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.line.style.LineChartStyles
import dev.gabrieldrn.konstellation.plotting.Axes
import dev.gabrieldrn.konstellation.plotting.ChartAxis
import dev.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import dev.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme

/**
 * A setting composable that allows the user to select which axes to draw and whether to draw the
 * frame and zero lines.
 */
@Composable
fun LineChartAxisSelectorSetting(
    styles: LineChartStyles,
    onUpdateStyles: (LineChartStyles) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Axes & framing", modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            AxesSelector(
                axes = styles.axes,
                onUpdateAxes = { onUpdateStyles(styles.copy(axes = it)) },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp)
            )
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(start = 16.dp)
            ) {
                ToggleIconButton(
                    toggled = styles.drawFrame,
                    onToggleChange = { onUpdateStyles(styles.copy(drawFrame = it)) },
                    imageVector = Icons.Default.BorderOuter
                )
                ToggleIconButton(
                    toggled = styles.drawZeroLines,
                    onToggleChange = { onUpdateStyles(styles.copy(drawZeroLines = it)) },
                    imageVector = Icons.Default.BorderInner
                )
            }
        }
    }
}

@Composable
private fun AxesSelector(
    axes: Set<ChartAxis>,
    onUpdateAxes: (Set<ChartAxis>) -> Unit,
    modifier: Modifier = Modifier
) {
    @Composable
    fun AxisToggleButton(
        axis: ChartAxis,
        imageVector: ImageVector,
        modifier: Modifier = Modifier,
    ) {
        ToggleIconButton(
            modifier = modifier,
            toggled = axes.contains(axis),
            onToggleChange = { toggled ->
                onUpdateAxes(
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

    Column(modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            AxisToggleButton(Axes.xTop, Icons.Default.BorderTop)
            AxisToggleButton(Axes.yRight, Icons.Default.BorderRight)
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            AxisToggleButton(Axes.yLeft, Icons.Default.BorderLeft)
            AxisToggleButton(Axes.xBottom, Icons.Default.BorderBottom)
        }
    }
}

@Preview
@Composable
private fun SettingsPreviews() {
    KonstellationTheme {
        LineChartAxisSelectorSetting(
            styles = LineChartStyles(),
            onUpdateStyles = { _ -> }
        )
    }
}
