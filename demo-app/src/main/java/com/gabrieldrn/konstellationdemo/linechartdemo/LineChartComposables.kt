package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrieldrn.konstellation.charts.line.presentation.LineChart
import com.gabrieldrn.konstellation.highlighting.HighlightPopup
import com.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellationdemo.linechartdemo.settings.LineChartSettingsContent
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme

@Composable
fun LineChartDemo(modifier: Modifier = Modifier) {
    Column(modifier) {
        DemoContent()
        LineChartSettingsContent()
    }
}

@Composable
private fun DemoContent() {
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
                HighlightPopup(color = MaterialTheme.colorScheme.inverseSurface) {
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
    KonstellationTheme {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            dataset = randomFancyDataSet(),
            properties = getChartProperties(),
            styles = getChartStyles()
        )
    }
}
