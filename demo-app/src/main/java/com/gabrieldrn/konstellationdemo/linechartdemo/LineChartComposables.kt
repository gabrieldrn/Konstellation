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
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
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
        val distanceHighlight: @Composable (Int) -> Unit = { dist ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "🥾 ${dist}km",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    textAlign = TextAlign.Start,
                )
            }
        }
        val altitudeHighlight: @Composable (Int) -> Unit = { alt ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "⛰️ ${alt}m",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    textAlign = TextAlign.Start,
                )
            }
        }
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), //Keep the chart square
            dataset = viewModel<LineChartDemoViewModel>().dataset,
            properties = viewModel<LineChartDemoViewModel>().properties,
            styles = getChartStyles(),
            highlightContent = {
                HighlightPopup(color = MaterialTheme.colorScheme.inverseSurface) {
                    when (contentPosition) {
                        HighlightContentPosition.Point -> Column {
                            distanceHighlight(point.x.toInt())
                            altitudeHighlight(point.y.toInt())
                        }
                        in horizontalHLPositions -> altitudeHighlight(point.y.toInt())
                        else -> distanceHighlight(point.x.toInt())
                    }
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
