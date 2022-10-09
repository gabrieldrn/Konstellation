package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrieldrn.konstellation.charts.line.composables.LineChart
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.charts.line.configuration.setAxesColor
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.highlighting.HighlightPopup
import com.gabrieldrn.konstellation.highlighting.verticalHLPositions
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.plotting.ChartAxis
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellationdemo.DemoContent
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme

@Composable
fun LineChartComposable() {

    val chartStyles = getChartStyles()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colors.background)
            .navigationBarsPadding(),
    ) {
        DemoContent(chartStyles)
        LineChartSettingsContent()
    }
}

@Composable
private fun DemoContent(
    chartStyles: LineChartStyles,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp),
        text = DemoContent.LINE.chartName.uppercase(),
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.ExtraBold,
    )
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), //Keep the chart square
            dataset = viewModel<LineChartDemoViewModel>().dataset,
            properties = viewModel<LineChartDemoViewModel>().properties,
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
            }
        )
    }
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

    KonstellationTheme {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            dataset = randomFancyDataSet(),
            properties = chartProperties,
            styles = chartStyles
        )
    }
}
