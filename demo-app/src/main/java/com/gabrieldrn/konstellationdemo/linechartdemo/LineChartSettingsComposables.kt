package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ColumnScope.LineChartSettingsContent(viewModel: LineChartDemoViewModel) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        count = 2,
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->
        when (page) {
            0 -> LineChartDatasetSelector(
                viewModel,
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            1 -> LineChartHighlightSetting(
                viewModel,
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
    viewModel: LineChartDemoViewModel,
    modifier: Modifier = Modifier
) {
    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { viewModel.generateNewRandomDataset() },
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
                        onClick = { viewModel.generateNewFancyDataset() },
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
    viewModel: LineChartDemoViewModel,
    modifier: Modifier = Modifier
) {
    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                @Composable
                fun PositionSwitch(position: HighlightContentPosition) {
                    Switch(
                        checked = viewModel.properties.highlightContentPositions.contains(position),
                        onCheckedChange = { checked ->
                            if (checked) viewModel.addHighlightPosition(position)
                            else viewModel.removeHighlightPosition(position)
                        }
                    )
                }
                PositionSwitch(HighlightContentPosition.TOP)
                Row {
                    PositionSwitch(HighlightContentPosition.START)
                    PositionSwitch(HighlightContentPosition.POINT)
                    PositionSwitch(HighlightContentPosition.END)
                }
                PositionSwitch(HighlightContentPosition.BOTTOM)
            }
        }
        Text(
            "Highlight popup positions",
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
                viewModel = LineChartDemoViewModel(),
                Modifier.fillMaxWidth().padding(16.dp)
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
                viewModel = LineChartDemoViewModel(),
                Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    }
}
