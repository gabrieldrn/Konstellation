package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellationdemo.ui.composables.ToggleIconButton
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

private val SettingSurfaceHeight = 124.dp

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ColumnScope.LineChartSettingsContent(
    drawPoints: Boolean,
    onGenerateRandomDataset: () -> Unit,
    onGenerateFancyDataset: () -> Unit,
    onToggleDrawPoints: (Boolean) -> Unit,
    onAddHighlightPosition: (HighlightContentPosition) -> Unit,
    onRemoveHighlightPosition: (HighlightContentPosition) -> Unit,
    onAddAxis: (ChartAxis) -> Unit,
    onRemoveAxis: (ChartAxis) -> Unit,
) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        count = 4,
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->
        when (page) {
            0 -> LineChartDatasetSelector(
                onGenerateRandomDataset = onGenerateRandomDataset,
                onGenerateFancyDataset = onGenerateFancyDataset
            )
            1 -> LineChartPointsSetting(
                drawPoints = drawPoints,
                onToggleDrawPoints = onToggleDrawPoints
            )
            2 -> LineChartHighlightSetting(
                onAddHighlightPosition = onAddHighlightPosition,
                onRemoveHighlightPosition = onRemoveHighlightPosition
            )
            3 -> LineChartAxisSelectorSetting(
                onAddAxis = onAddAxis,
                onRemoveAxis = onRemoveAxis
            )
        }
    }
    HorizontalPagerIndicator(
        pagerState = pagerState,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 4.dp),
    )
}

@Composable
private fun SettingSurface(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(SettingSurfaceHeight),
            content = content
        )
        Text(title, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
private fun LineChartDatasetSelector(
    onGenerateRandomDataset: () -> Unit,
    onGenerateFancyDataset: () -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Datasets", modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onGenerateRandomDataset,
                    Modifier.background(
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Text(text = "Random", textAlign = TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onGenerateFancyDataset,
                    Modifier.background(
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoGraph,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Text(text = "Fancy", textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun LineChartPointsSetting(
    drawPoints: Boolean,
    onToggleDrawPoints: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Points", modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ToggleIconButton(
                toggled = drawPoints,
                onToggleChange = onToggleDrawPoints,
                imageVector = Icons.Outlined.Commit
            )
            Text(text = "Draw points", textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun LineChartHighlightSetting(
    onAddHighlightPosition: (HighlightContentPosition) -> Unit,
    onRemoveHighlightPosition: (HighlightContentPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Highlight popup positions", modifier = modifier) {
        @Composable
        fun PositionToggleButton(position: HighlightContentPosition, imageVector: ImageVector) {
            ToggleIconButton(
                toggled = viewModel<LineChartDemoViewModel>()
                    .properties.highlightContentPositions.contains(position),
                onToggleChange = { toggled ->
                    if (toggled) onAddHighlightPosition(position)
                    else onRemoveHighlightPosition(position)
                },
                imageVector = imageVector
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            PositionToggleButton(HighlightContentPosition.TOP, Icons.Default.North)
            PositionToggleButton(HighlightContentPosition.BOTTOM, Icons.Default.South)
            PositionToggleButton(HighlightContentPosition.START, Icons.Default.West)
            PositionToggleButton(HighlightContentPosition.END, Icons.Default.East)
            Box(
                Modifier
                    .height(48.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
            )
            PositionToggleButton(HighlightContentPosition.POINT, Icons.Default.PushPin)
        }
    }
}

@Composable
private fun LineChartAxisSelectorSetting(
    onAddAxis: (ChartAxis) -> Unit,
    onRemoveAxis: (ChartAxis) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Axes", modifier = modifier) {
        @Composable
        fun AxisToggleButton(axis: ChartAxis, imageVector: ImageVector) {
            ToggleIconButton(
                toggled = viewModel<LineChartDemoViewModel>()
                    .properties.axes.contains(axis),
                onToggleChange = { toggled ->
                    if (toggled) onAddAxis(axis)
                    else onRemoveAxis(axis)
                },
                imageVector = imageVector
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            AxisToggleButton(Axes.xTop, Icons.Default.BorderTop)
            AxisToggleButton(Axes.xBottom, Icons.Default.BorderBottom)
            Box(
                Modifier
                    .height(48.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
            )
            AxisToggleButton(Axes.yLeft, Icons.Default.BorderLeft)
            AxisToggleButton(Axes.yRight, Icons.Default.BorderRight)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChartSelectorPreview() {
    KonstellationTheme {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            LineChartDatasetSelector({}, {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChartPointsSettingsPreview() {
    KonstellationTheme {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            var value by remember { mutableStateOf(true) }
            LineChartPointsSetting(value, { value = it })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChartHighlightSelectorPreview() {
    KonstellationTheme {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            LineChartHighlightSetting({}, {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChartAxesSelectorPreview() {
    KonstellationTheme {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            LineChartAxisSelectorSetting({}, {})
        }
    }
}
