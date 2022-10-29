package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrieldrn.konstellation.charts.line.presentation.LineChart
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartStyles
import com.gabrieldrn.konstellation.charts.line.configuration.setAxesColor
import com.gabrieldrn.konstellation.configuration.properties.DatasetOffsets
import com.gabrieldrn.konstellation.highlighting.HighlightPopup
import com.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellationdemo.linechartdemo.settings.LineChartSettingsContent
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme

@Composable
fun LineChartComposable(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
    ) {
        Text(
            text = "Konstellation",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
        DemoContent()
        LineChartSettingsContent()
    }
}

@Composable
private fun DemoContent() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp),
        text = "Line chart",
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
    )
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
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
            styles = getChartStyles(),
            highlightContent = {
                HighlightPopup(
                    color = MaterialTheme.colorScheme.inverseSurface
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Center),
                        text = if (contentPosition in horizontalHLPositions) {
                            "x = ${point.x.toInt()}"
                        } else {
                            "y = ${point.y.toInt()}"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
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
        lineStyle.color = MaterialTheme.colorScheme.primary
        pointStyle.color = MaterialTheme.colorScheme.primary
        textStyle.color = MaterialTheme.colorScheme.primary
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
