package com.gabrieldrn.konstellationdemo.linechartdemo.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrieldrn.konstellation.configuration.properties.Smoothing
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.gabrieldrn.konstellation.plotting.Axes
import com.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemoViewModel
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

private val SettingSurfaceHeight = 148.dp

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ColumnScope.LineChartSettingsContent(viewModel: LineChartDemoViewModel = viewModel()) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = 5,
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->
        @Suppress("MagicNumber")
        when (page) {
            0 -> LineChartDatasetSelector(
                onGenerateRandomDataset = viewModel::generateNewRandomDataset,
                onGenerateFancyDataset = viewModel::generateNewFancyDataset
            )

            1 -> LineChartDataDrawingSetting(
                drawLines = viewModel.properties.drawLines,
                drawPoints = viewModel.properties.drawPoints,
                smoothing = viewModel.properties.smoothing,
                onUpdateProperty = viewModel::updateProperty,
            )

            2 -> LineChartFillingSetting(
                brush = viewModel.properties.fillingBrush,
                onUpdateProperty = viewModel::updateProperty
            )

            3 -> LineChartHighlightSetting(
                highlightPositions = viewModel.properties.highlightContentPositions,
                onUpdateProperty = viewModel::updateProperty
            )

            4 -> LineChartAxisSelectorSetting(
                axes = viewModel.properties.axes,
                drawFrame = viewModel.properties.drawFrame,
                drawZeroLines = viewModel.properties.drawZeroLines,
                onUpdateProperty = viewModel::updateProperty,
            )
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
            shape = RoundedCornerShape(16.dp),
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
                shape = CircleShape
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
fun SettingsPreviews() {
    KonstellationTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            LineChartDatasetSelector({}, {})

            LineChartDataDrawingSetting(
                drawLines = true, drawPoints = true, Smoothing.None, { _, _ -> }
            )

            LineChartFillingSetting(null, { _, _ -> })

            LineChartHighlightSetting(setOf(HighlightContentPosition.Point), { _, _ -> })

            LineChartAxisSelectorSetting(
                axes = setOf(Axes.yLeft, Axes.xBottom),
                drawFrame = false,
                drawZeroLines = false,
                { _, _ -> },
            )
        }
    }
}
