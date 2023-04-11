package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.presentation.LineChart
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.highlighting.HighlightPopup
import com.gabrieldrn.konstellation.highlighting.HighlightScope
import com.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.by
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellationdemo.linechartdemo.settings.LineChartSettingsContent
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme

/**
 * This is the main composable that will be used to display the line chart demo.
 */
@Composable
fun LineChartDemo(
    viewModel: LineChartDemoViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        DemoContent(
            dataset = viewModel.dataset,
            properties = viewModel.properties
        )
        LineChartSettingsContent(viewModel)
    }
}

@Composable
private fun DemoContent(
    dataset: Dataset,
    properties: LineChartProperties,
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
            dataset = dataset,
            properties = properties,
            styles = getDemoChartStyles(),
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
private fun LineChartWithCustomPropertiesPreview() {
    KonstellationTheme {
        DemoContent(
            dataset = datasetOf(
                0f by 0f,
                1f by 1f,
                2f by 1f,
                3f by 3f,
                4f by 3f,
                5f by 2f,
                6f by 2f,
            ),
            properties = LineChartProperties()
        )
    }
}
