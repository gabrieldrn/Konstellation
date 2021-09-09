package com.gabrieldrn.konstellationdemo

import android.graphics.Paint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.charts.line.*
import com.gabrieldrn.konstellation.core.highlighting.*
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.style.*
import com.gabrieldrn.konstellation.util.*
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight

private const val LINE_CHART_X_RANGE_MIN = 1f
private const val LINE_CHART_X_RANGE_MAX = 25f
private const val LINE_CHART_X_RANGE_DEFAULT = 15f
private const val LINE_CHART_Y_RANGE_MIN = 1f
private const val LINE_CHART_Y_RANGE_MAX = 25f
private const val LINE_CHART_Y_RANGE_DEFAULT = 15f

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun LineChartComp(mainTextStyle: TextDrawStyle) {

    val settingsSheetState = rememberBottomSheetScaffoldState()

    var points by rememberSaveable { mutableStateOf(randomFancyDataSet()) }
    var xRange by remember { mutableStateOf(LINE_CHART_X_RANGE_DEFAULT) }
    var yRange by remember { mutableStateOf(LINE_CHART_Y_RANGE_DEFAULT) }
    var highlightPositions by remember { mutableStateOf(arrayOf(HighlightPosition.TOP)) }

    val axisColor = MaterialTheme.colors.onBackground
    val chartProperties = LineChartProperties().apply {
        lineStyle.color = MaterialTheme.colors.primary
        pointStyle.color = MaterialTheme.colors.primary
        textStyle.color = MaterialTheme.colors.primary
        highlightPointStyle.run {
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f)
            radius = 6.dp
        }
        highlightLineStyle.color = MaterialTheme.colors.onBackground
        highlightTextStyle = mainTextStyle.copy(
            color = MaterialTheme.colors.primary,
            textAlign = Paint.Align.CENTER,
            offsetY = -25f
        )
        chartPaddingValues = PaddingValues(44.dp)
        dataXRange = -xRange..xRange
        dataYRange = -yRange..yRange
        axes = setOf(
            Axes.xBottom.apply { style.setColor(axisColor) },
            Axes.xTop.apply { style.setColor(axisColor) },
            Axes.yLeft.apply { style.setColor(axisColor) },
            Axes.yRight.apply { style.setColor(axisColor) },
        )
    }

    chartProperties.setAxisTypeface(mainTextStyle.typeface)

    val imeBottom = with(LocalDensity.current) {
        LocalWindowInsets.current.navigationBars.bottom.toDp()
    }

    BottomSheetScaffold(
        scaffoldState = settingsSheetState,
        sheetPeekHeight = 60.dp + imeBottom,
        sheetContent = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                LineChartSettingsContent(
                    xRange = xRange,
                    yRange = yRange,
                    highlightPositions = highlightPositions,
                    onChangeDataset = { points = it },
                    onXRangeChanged = { xRange = it },
                    onYRangeChanged = { yRange = it },
                    onHighlightPositionsChanged = { highlightPositions = it }
                )
            }
        }
    ) {
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp),
                text = DemoContent.LINE.chartName.uppercase(),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold,
            )
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), //Keep the chart square
                dataset = points,
                properties = chartProperties,
                highlightPositions = highlightPositions,
                highlightContent = {
                    HighlightPopup(
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.Center),
                            text = "y -> ${it.y.toInt()}",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun LineChartSettingsContent(
    xRange: Float,
    yRange: Float,
    highlightPositions: Array<HighlightPosition>,
    onChangeDataset: (Dataset) -> Unit,
    onXRangeChanged: (Float) -> Unit,
    onYRangeChanged: (Float) -> Unit,
    onHighlightPositionsChanged: (Array<HighlightPosition>) -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        text = "${DemoContent.LINE.chartName} settings",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h6
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            onClick = { onChangeDataset(randomDataSet()) }, content = {
                Text(text = "NEW RANDOM DATASET", textAlign = TextAlign.Center)
            }
        )
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            onClick = { onChangeDataset(randomFancyDataSet()) }, content = {
                Text(text = "NEW FANCY DATASET", textAlign = TextAlign.Center)
            }
        )
    }
    Row(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically),
            text = "X range"
        )
        Slider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f),
            value = xRange,
            valueRange = LINE_CHART_X_RANGE_MIN..LINE_CHART_X_RANGE_MAX,
            onValueChange = onXRangeChanged
        )
    }
    Row(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically),
            text = "Y range"
        )
        Slider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f),
            value = yRange,
            valueRange = LINE_CHART_Y_RANGE_MIN..LINE_CHART_Y_RANGE_MAX,
            onValueChange = onYRangeChanged
        )
    }
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
        text = "Highlight positions"
    )
    Box(
        Modifier
            .fillMaxWidth()
            .height(172.dp)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {

        fun addOrRemovePosition(add: Boolean, position: HighlightPosition) {
            onHighlightPositionsChanged(highlightPositions.toMutableList().apply {
                if (add) add(position) else remove(position)
            }.toSet().toTypedArray())
        }

        val highlightPositionCheckbox: @Composable (position: HighlightPosition) -> Unit = {
            Checkbox(
                modifier = Modifier.align(
                    when (it) {
                        HighlightPosition.TOP -> Alignment.TopCenter
                        HighlightPosition.BOTTOM -> Alignment.BottomCenter
                        HighlightPosition.START -> Alignment.CenterStart
                        HighlightPosition.END -> Alignment.CenterEnd
                        HighlightPosition.POINT -> Alignment.Center
                    }
                ),
                checked = highlightPositions.contains(it),
                onCheckedChange = { checked -> addOrRemovePosition(checked, it) }
            )
        }

        HighlightPosition.values().forEach {
            highlightPositionCheckbox(it)
        }
    }
    Spacer(Modifier.navigationBarsHeight().fillMaxWidth())
}
