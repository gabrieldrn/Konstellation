package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.charts.line.composables.LineChart
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.charts.line.configuration.setAxesColor
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.highlighting.HighlightPopup
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.Point
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellationdemo.DemoContent
import com.google.accompanist.insets.LocalWindowInsets

@ExperimentalMaterialApi
@Composable
fun LineChartComposable(viewModel: LineChartDemoViewModel) {

    val settingsSheetState = rememberBottomSheetScaffoldState()

    val chartStyles = getChartStyles()

    val imeBottom = with(LocalDensity.current) {
        LocalWindowInsets.current.navigationBars.bottom.toDp()
    }

    BottomSheetScaffold(
        scaffoldState = settingsSheetState,
        sheetPeekHeight = 60.dp + imeBottom,
        sheetContent = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                LineChartSettingsContent(
                    viewModel = viewModel
                )
            }
        }
    ) {
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            DemoContent(viewModel, chartStyles)
        }
    }
}

@Composable
private fun DemoContent(
    viewModel: LineChartDemoViewModel,
    chartStyles: LineChartStyles,
) {
    var highlightedPoint: Point? by remember {
        mutableStateOf(null)
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp),
        text = DemoContent.LINE.chartName.uppercase(),
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.ExtraBold,
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f), //Keep the chart square
        dataset = viewModel.dataset,
        properties = viewModel.properties,
        styles = chartStyles,
        highlightContent = {
            HighlightPopup(
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Center),
                    text = "y -> ${point.y.toInt()}",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Start
                )
            }
        },
        onHighlightChange = {
            highlightedPoint = it
        }
    )
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = highlightedPoint?.let {
            "Selected point: ${it.x};${it.y}"
        } ?: "Hold and <-swipe-> to highlight"
    )
}

@Preview(showBackground = true)
@Composable
fun LineChartWithCustomPropertiesPreview() {
    val chartProperties = LineChartProperties(
        axes = setOf(Axes.xTop, Axes.xBottom, Axes.yLeft, Axes.yRight),
        datasetOffsets = DatasetOffsets(
            xStartOffset = 2f,
            xEndOffset = 2f,
            yStartOffset = 0.5f,
            yEndOffset = 0.5f
        )
    )
    val chartStyles = LineChartStyles().apply {
        lineStyle.color = MaterialTheme.colors.primary
        pointStyle.color = MaterialTheme.colors.primary
        textStyle.color = MaterialTheme.colors.primary
        setAxesColor(Color.Black)
    }

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        dataset = randomFancyDataSet(),
        properties = chartProperties,
        styles = chartStyles
    )
}
