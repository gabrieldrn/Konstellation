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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrieldrn.konstellation.configuration.properties.Smoothing
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
fun ColumnScope.LineChartSettingsContent(viewModel: LineChartDemoViewModel = viewModel()) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = 5,
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->
        @Suppress("MagicNumber")
        when (page) {
            0 -> LineChartDatasetSelector(
                onGenerateRandomDataset = viewModel::generateNewRandomDataset,
                onGenerateFancyDataset = viewModel::generateNewFancyDataset
            )

            1 -> LineChartDataDrawingSetting(
                drawLines = viewModel.properties.drawLines,
                drawPoints = viewModel.properties.drawPoints,
                smoothing = viewModel.properties.smoothing,
                onToggleDrawLines = viewModel::updateDrawLines,
                onToggleDrawPoints = viewModel::updateDrawPoints,
                onChangeSmoothing = viewModel::changeSmoothing
            )

            2 -> LineChartFillingSetting(
                brush = viewModel.properties.fillingBrush,
                onChangeBrush = viewModel::changeFillingBrush
            )

            3 -> LineChartHighlightSetting(
                highlightPositions = viewModel.properties.highlightContentPositions,
                onAddHighlightPosition = viewModel::addHighlightPosition,
                onRemoveHighlightPosition = viewModel::removeHighlightPosition
            )

            4 -> LineChartAxisSelectorSetting(
                axes = viewModel.properties.axes,
                onAddAxis = viewModel::addAxis,
                onRemoveAxis = viewModel::removeAxis
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
private fun LineChartDataDrawingSetting(
    drawLines: Boolean,
    drawPoints: Boolean,
    smoothing: Smoothing,
    onToggleDrawLines: (Boolean) -> Unit,
    onToggleDrawPoints: (Boolean) -> Unit,
    onChangeSmoothing: (Smoothing) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Data drawing", modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ToggleIconButton(
                    toggled = drawLines,
                    onToggleChange = onToggleDrawLines,
                    imageVector = Icons.Outlined.ShowChart
                )
                Text(text = "Lines", textAlign = TextAlign.Center)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ToggleIconButton(
                    toggled = drawPoints,
                    onToggleChange = onToggleDrawPoints,
                    imageVector = Icons.Outlined.Commit
                )
                Text(text = "Points", textAlign = TextAlign.Center)
            }

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val methods by remember { mutableStateOf(Smoothing.values()) }
                IconButton(
                    onClick = {
                        val index = (methods.indexOf(smoothing) + 1)
                            .takeIf { it in methods.indices } ?: 0
                        onChangeSmoothing(methods[index])
                    },
                    Modifier.background(
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Text(text = "Smoothing", textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun LineChartFillingSetting(
    brush: Brush?,
    onChangeBrush: (Brush?) -> Unit,
    modifier: Modifier = Modifier
) {
    val solidColor = SolidColor(MaterialTheme.colors.primary.copy(alpha = .75f))
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colors.primary, Color.Transparent)
    )
    SettingSurface(title = "Filling", modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            ToggleIconButton(
                toggled = brush is SolidColor,
                onToggleChange = {
                    onChangeBrush(solidColor)
                },
                imageVector = Icons.Default.FormatColorFill
            )
            ToggleIconButton(
                toggled = brush is ShaderBrush,
                onToggleChange = {
                    onChangeBrush(gradientBrush)
                },
                imageVector = Icons.Default.Gradient
            )
            ToggleIconButton(
                toggled = brush == null,
                onToggleChange = {
                    onChangeBrush(null)
                },
                imageVector = Icons.Default.FormatColorReset
            )
        }
    }
}

@Composable
private fun LineChartHighlightSetting(
    highlightPositions: Set<HighlightContentPosition>,
    onAddHighlightPosition: (HighlightContentPosition) -> Unit,
    onRemoveHighlightPosition: (HighlightContentPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Highlight popup positions", modifier = modifier) {
        @Composable
        fun PositionToggleButton(position: HighlightContentPosition, imageVector: ImageVector) {
            ToggleIconButton(
                toggled = highlightPositions.contains(position),
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
            PositionToggleButton(HighlightContentPosition.Top, Icons.Default.North)
            PositionToggleButton(HighlightContentPosition.Bottom, Icons.Default.South)
            PositionToggleButton(HighlightContentPosition.Start, Icons.Default.West)
            PositionToggleButton(HighlightContentPosition.End, Icons.Default.East)
            Box(
                Modifier
                    .height(48.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
            )
            PositionToggleButton(HighlightContentPosition.Point, Icons.Default.PushPin)
        }
    }
}

@Composable
private fun LineChartAxisSelectorSetting(
    axes: Set<ChartAxis>,
    onAddAxis: (ChartAxis) -> Unit,
    onRemoveAxis: (ChartAxis) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Axes", modifier = modifier) {
        @Composable
        fun AxisToggleButton(axis: ChartAxis, imageVector: ImageVector) {
            ToggleIconButton(
                toggled = axes.contains(axis),
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

@Preview
@Composable
fun SettingsPreviews() {
    KonstellationTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .verticalScroll(rememberScrollState())
        ) {
            LineChartDatasetSelector({}, {})

            LineChartDataDrawingSetting(
                drawLines = true, drawPoints = true, Smoothing.None, {}, {}, {}
            )

            LineChartFillingSetting(null, {})

            LineChartHighlightSetting(setOf(HighlightContentPosition.Point), {}, {})

            LineChartAxisSelectorSetting(setOf(Axes.yLeft, Axes.xBottom), {}, {})
        }
    }
}
