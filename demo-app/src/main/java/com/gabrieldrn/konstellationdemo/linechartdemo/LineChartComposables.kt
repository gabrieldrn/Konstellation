package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.charts.line.presentation.LineChart
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.highlighting.HighlightPopup
import com.gabrieldrn.konstellation.highlighting.HighlightScope
import com.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellationdemo.linechartdemo.settings.LineChartSettingsContent
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme

@Composable
fun LineChartDemo(
    viewModel: LineChartDemoViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        DemoContent(viewModel)
        LineChartSettingsContent(viewModel)
    }
}

@Composable
private fun DemoContent(
    viewModel: LineChartDemoViewModel,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        modifier = modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), //Keep the chart square
            dataset = viewModel.dataset,
            properties = viewModel.properties,
            styles = getChartStyles(),
            highlightContent = { DemoHighlightPopup() }
        )
    }
}

@Composable
private fun HighlightScope.DemoHighlightPopup() {
    HighlightPopup(color = MaterialTheme.colorScheme.inverseSurface) {
        when (contentPosition) {
            HighlightContentPosition.Point -> Column {
                DistanceHighlight(point.x.toInt())
                AltitudeHighlight(point.y.toInt())
            }
            in horizontalHLPositions -> AltitudeHighlight(point.y.toInt())
            else -> DistanceHighlight(point.x.toInt())
        }
    }
}

@Composable
private fun DistanceHighlight(dist: Int) {
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

@Composable
private fun AltitudeHighlight(alt: Int) {
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
