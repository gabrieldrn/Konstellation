package dev.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.charts.line.presentation.LineChart
import dev.gabrieldrn.konstellation.charts.line.style.LineChartStyles
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellation.highlighting.HighlightPopup
import dev.gabrieldrn.konstellation.highlighting.HighlightScope
import dev.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.by
import dev.gabrieldrn.konstellation.plotting.datasetOf
import dev.gabrieldrn.konstellationdemo.appModule
import dev.gabrieldrn.konstellationdemo.linechartdemo.settings.LineChartSettingsContent
import dev.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * This is the main composable that will be used to display the line chart demo.
 */
@Composable
fun LineChartDemo(
    viewModel: LineChartDemoViewModel,
    modifier: Modifier = Modifier
) {
    val lineChartStylesBaseline = getDemoChartStyles()

    var lineChartStyles by remember {
        mutableStateOf(lineChartStylesBaseline)
    }

    Column(modifier) {
        DemoContent(
            dataset = viewModel.uiState.dataset,
            properties = viewModel.uiState.properties,
            styles = lineChartStyles
        )
        LineChartSettingsContent(
            properties = viewModel.uiState.properties,
            styles = lineChartStyles,
            onGenerateRandomDataset = viewModel::generateNewRandomDataset,
            onGenerateFancyDataset = viewModel::generateNewFancyDataset,
            onUpdateProperty = viewModel::updateProperty,
            onStyleChange = { lineChartStyles = it }
        )
    }
}

@Composable
private fun DemoContent(
    dataset: Dataset,
    properties: LineChartProperties,
    styles: LineChartStyles,
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
            styles = styles,
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
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(appModule)
    }
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
            properties = LineChartProperties(),
            styles = getDemoChartStyles(),
        )
    }
}
