package dev.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import dev.gabrieldrn.konstellation.charts.line.properties.ChartWindow
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartHighlightConfig
import dev.gabrieldrn.konstellation.charts.line.properties.LineChartProperties
import dev.gabrieldrn.konstellation.charts.line.style.LineChartStyles
import dev.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import dev.gabrieldrn.konstellation.plotting.Dataset
import dev.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import kotlin.reflect.KProperty1

private val SettingSurfaceHeight = 148.dp

/**
 * This is the main composable that will be used to display the line chart demo settings
 * composables, as a horizontal pager.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ColumnScope.LineChartSettingsContent(
    dataset: Dataset,
    properties: LineChartProperties,
    styles: LineChartStyles,
    highlightConfig: LineChartHighlightConfig,
    onGenerateRandomDataset: () -> Unit,
    onGenerateFancyDataset: () -> Unit,
    onUpdateProperty: (KProperty1<LineChartProperties, Any?>, Any?) -> Unit,
    onUpdateStyles: (LineChartStyles) -> Unit,
    onUpdateHighlightConfig: (LineChartHighlightConfig) -> Unit,
) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = 6,
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->
        @Suppress("MagicNumber")
        when (page) {

            // region Properties

            0 -> LineChartDatasetSelector(
                onGenerateRandomDataset = onGenerateRandomDataset,
                onGenerateFancyDataset = onGenerateFancyDataset,
            )

            1 -> LineChartPropertiesSetting(
                chartPaddingValues = properties.chartPaddingValues,
                chartWindow = properties.chartWindow ?: ChartWindow.fromDataset(dataset),
                chartInitialWindow = ChartWindow.fromDataset(dataset),
                panningEnabled = properties.panningEnabled,
                onUpdateProperty = onUpdateProperty
            )

            // endregion

            // region Styles

            2 -> LineChartAxisSelectorSetting(
                styles = styles,
                onUpdateStyles = onUpdateStyles,
            )

            3 -> LineChartDataDrawingSetting(
                styles = styles,
                onUpdateStyles = onUpdateStyles,
            )

            4 -> LineChartFillingSetting(
                brush = styles.fillingBrush,
                onUpdateBrush = { brush ->
                    onUpdateStyles(styles.copy(fillingBrush = brush))
                },
            )

            // endregion

            // region Highlighting

            5 -> LineChartHighlightSetting(
                contentPositions = highlightConfig.contentPositions,
                onUpdateHighlightConfig = { positions ->
                    onUpdateHighlightConfig(highlightConfig.copy(contentPositions = positions))
                }
            )

            // endregion
        }
    }
    HorizontalPagerIndicator(
        pagerState = pagerState,
        activeColor = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 4.dp),
    )
}

@Composable
internal fun SettingSurface(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Column {
        Surface(
            shape = MaterialTheme.shapes.large,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(SettingSurfaceHeight),
            content = content,
            color = MaterialTheme.colorScheme.secondaryContainer,
            tonalElevation = 1.dp
        )
        Text(
            title,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
internal fun SettingIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            Modifier.background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        if (!text.isNullOrEmpty()) {
            Text(text = text, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun LineChartDatasetSelector(
    onGenerateRandomDataset: () -> Unit,
    onGenerateFancyDataset: () -> Unit,
    modifier: Modifier = Modifier
) {
    SettingSurface(title = "Datasets", modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingIconButton(
                onClick = onGenerateRandomDataset, icon = Icons.Default.Shuffle, text = "Random"
            )
            SettingIconButton(
                onClick = onGenerateFancyDataset, icon = Icons.Default.AutoGraph, text = "Fancy"
            )
        }
    }
}

@Preview
@Composable
private fun SettingsPreviews() {
    KonstellationTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            LineChartDatasetSelector({}, {})

            LineChartPropertiesSetting(
                chartPaddingValues = PaddingValues(44.dp),
                chartWindow = ChartWindow(0f..1f, 0f..1f),
                chartInitialWindow = ChartWindow(0f..1f, 0f..1f),
                panningEnabled = true,
                onUpdateProperty = { _, _ -> }
            )

            LineChartAxisSelectorSetting(
                styles = LineChartStyles(),
                onUpdateStyles = { _ -> }
            )

            LineChartDataDrawingSetting(
                styles = LineChartStyles(),
                onUpdateStyles = { _ -> }
            )

            LineChartFillingSetting(
                brush = null,
                onUpdateBrush = { _ -> }
            )

            LineChartHighlightSetting(setOf(HighlightContentPosition.Point), { _ -> })
        }
    }
}
