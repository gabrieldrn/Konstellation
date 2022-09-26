package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
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
    onGenerateRandomDataset: () -> Unit,
    onGenerateFancyDataset: () -> Unit,
    onAddHighlightPosition: (HighlightContentPosition) -> Unit,
    onRemoveHighlightPosition: (HighlightContentPosition) -> Unit,
    onAddAxis: (ChartAxis) -> Unit,
    onRemoveAxis: (ChartAxis) -> Unit,
) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        count = 3,
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->
        when (page) {
            0 -> LineChartDatasetSelector(
                onGenerateRandomDataset = onGenerateRandomDataset,
                onGenerateFancyDataset = onGenerateFancyDataset,
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            1 -> LineChartHighlightSetting(
                onAddHighlightPosition = onAddHighlightPosition,
                onRemoveHighlightPosition = onRemoveHighlightPosition,
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            2 -> LineChartAxisSelectorSetting(
                onAddAxis = onAddAxis,
                onRemoveAxis = onRemoveAxis,
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
private fun LineChartDatasetSelector(
    onGenerateRandomDataset: () -> Unit,
    onGenerateFancyDataset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier.height(SettingSurfaceHeight)
        ) {
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
        Text("Datasets", modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
private fun LineChartHighlightSetting(
    onAddHighlightPosition: (HighlightContentPosition) -> Unit,
    onRemoveHighlightPosition: (HighlightContentPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier.height(SettingSurfaceHeight)
        ) {
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
                PositionToggleButton(HighlightContentPosition.POINT, Icons.Default.PushPin)
            }
        }
        Text(
            "Highlight popup positions",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun LineChartAxisSelectorSetting(
    onAddAxis: (ChartAxis) -> Unit,
    onRemoveAxis: (ChartAxis) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier.height(SettingSurfaceHeight)
        ) {
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
                AxisToggleButton(Axes.yLeft, Icons.Default.BorderLeft)
                AxisToggleButton(Axes.yRight, Icons.Default.BorderRight)
            }
        }
        Text(
            "Axes",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChartSelectorPreview() {
    KonstellationTheme {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            LineChartDatasetSelector(
                {},
                {},
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChartHighlightSelectorPreview() {
    KonstellationTheme {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            LineChartHighlightSetting(
                {},
                {},
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChartAxesSelectorPreview() {
    KonstellationTheme {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            LineChartAxisSelectorSetting(
                {},
                {},
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
