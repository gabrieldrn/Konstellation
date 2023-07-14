package dev.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.line.LineChart
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartHighlightConfig
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.charts.line.style.LineChartStyles
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellation.highlighting.HighlightPopup
import dev.gabrieldrn.konstellation.highlighting.HighlightPopupShape
import dev.gabrieldrn.konstellation.highlighting.HighlightScope
import dev.gabrieldrn.konstellation.highlighting.horizontalHLPositions
import dev.gabrieldrn.konstellation.plotting.Axis
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellation.plotting.by
import dev.gabrieldrn.konstellation.plotting.datasetOf
import dev.gabrieldrn.konstellationdemo.appModule
import dev.gabrieldrn.konstellationdemo.linechartdemo.settings.LineChartSettingsContent
import dev.gabrieldrn.konstellationdemo.ui.isLandscape
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
    val lineChartHighlightConfigBaseline = getDemoHighlightConfig()

    var lineChartStyles by remember {
        mutableStateOf(lineChartStylesBaseline)
    }
    var lineChartHighlightConfig by remember {
        mutableStateOf(lineChartHighlightConfigBaseline)
    }

    @Composable
    fun Chart(modifier: Modifier = Modifier) {
        DemoLineChart(
            dataset = viewModel.uiState.dataset,
            properties = viewModel.uiState.properties,
            styles = lineChartStyles,
            highlightConfig = lineChartHighlightConfig,
            modifier = modifier.padding(8.dp)
        )
    }

    @Composable
    fun Settings(modifier: Modifier = Modifier) {
        LineChartSettingsContent(
            dataset = viewModel.uiState.dataset,
            properties = viewModel.uiState.properties,
            styles = lineChartStyles,
            highlightConfig = lineChartHighlightConfig,
            onGenerateRandomDataset = viewModel::generateNewRandomDataset,
            onGenerateFancyDataset = viewModel::generateNewFancyDataset,
            onUpdateProperty = viewModel::updateProperty,
            onUpdateStyles = { lineChartStyles = it },
            onUpdateHighlightConfig = { lineChartHighlightConfig = it },
            modifier = modifier
        )
    }

    Column(modifier) {
        if (LocalConfiguration.current.isLandscape) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Chart(modifier = Modifier.weight(1f))
                Settings(modifier = Modifier.weight(1f))
            }
        } else {
            Chart(modifier = Modifier.weight(1f).aspectRatio(1f))
            Settings(modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DemoLineChart(
    dataset: Dataset,
    properties: LineChartProperties,
    styles: LineChartStyles,
    highlightConfig: LineChartHighlightConfig,
    modifier: Modifier = Modifier
) {
    LineChart(
        modifier = modifier,
        dataset = dataset,
        properties = properties,
        styles = styles,
        highlightConfig = highlightConfig,
        highlightContent = { DemoHighlightPopup() },
        onDrawTick = { axis, value ->
            when (axis.axis) {
                Axis.X_BOTTOM, Axis.X_TOP -> "${value.toInt()}km"
                Axis.Y_LEFT, Axis.Y_RIGHT -> when {
                    value < 0 -> "${value.toInt()}m"
                    value > 0 -> "${value.toInt()}m"
                    else -> "🌎"
                }
            }
        }
    )
}

@Composable
private fun HighlightScope.DemoHighlightPopup() {
    HighlightPopup(
        color = MaterialTheme.colorScheme.primary,
        shape = HighlightPopupShape(contentPosition).apply {
            cornersRadius = 0.dp
        }
    ) {
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
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
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
            text = when {
                alt < 0 -> "🤿 ${alt}m"
                alt > 0 -> "⛰️ ${alt}m"
                else -> "0m"
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
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
        DemoLineChart(
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
            highlightConfig = LineChartHighlightConfig(),
        )
    }
}
