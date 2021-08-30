package com.gabrieldrn.konstellationdemo

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.gabrieldrn.konstellation.charts.function.FunctionPlotter
import com.gabrieldrn.konstellation.charts.line.LineChart
import com.gabrieldrn.konstellation.charts.line.LineChartProperties
import com.gabrieldrn.konstellation.charts.line.setAxisTypeface
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.core.highlighting.HighlightPosition
import com.gabrieldrn.konstellation.style.*
import com.gabrieldrn.konstellation.core.highlighting.HighlightPopup
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

private var textStyle = TextDrawStyle()

class MainActivity : AppCompatActivity() {
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ResourcesCompat.getFont(this, R.font.space_mono_regular)?.let {
            textStyle.typeface = it
        }
        setContent {
            KonstellationTheme {
                Content()
            }
        }
    }
}

enum class DemoContent(val chartName: String) {
    LINE("Line chart"),
    FUNCTION("Function chart")
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun Content() {
    var contentSelection by rememberSaveable { mutableStateOf(DemoContent.values().first()) }
    val scope = rememberCoroutineScope()

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    LaunchedEffect(scaffoldState) {
        scaffoldState.conceal()
    }
    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                title = {
                    Text(text = "Konstellation")
                },
                navigationIcon = {
                    if (scaffoldState.isConcealed) {
                        IconButton(onClick = { scope.launch { scaffoldState.reveal() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    } else {
                        IconButton(onClick = { scope.launch { scaffoldState.conceal() } }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            )
        }, backLayerContent = {
            LazyColumn {
                items(DemoContent.values()) { item ->
                    ListItem(
                        modifier = Modifier.clickable {
                            contentSelection = item
                            scope.launch { scaffoldState.conceal() }
                        },
                        text = { Text(text = item.chartName) },
                    )
                }
            }
        }, frontLayerContent = {
            Box(Modifier.fillMaxSize()) {
                when (contentSelection) {
                    DemoContent.LINE -> LineChartComp()
                    DemoContent.FUNCTION -> AnimatedFunctionChart()
                }
            }
        })
}

@Composable
fun LineChartSettings(
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
                .padding(start = 16.dp)
                .weight(1f),
            value = xRange,
            valueRange = 1f..25f,
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
                .padding(start = 16.dp)
                .weight(1f),
            value = yRange,
            valueRange = 1f..25f,
            onValueChange = onYRangeChanged
        )
    }
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
        text = "Highlight positions"
    )
    Box(Modifier
        .fillMaxWidth()
        .height(172.dp)
        .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {

        fun addOrRemovePosition(add: Boolean, position: HighlightPosition) {
            onHighlightPositionsChanged(highlightPositions.toMutableList().apply {
                if (add) add(position) else remove(position)
            }.toSet().toTypedArray())
        }

        @Composable
        fun HighlightPositionCheckbox(position: HighlightPosition) {
            Checkbox(
                modifier = Modifier.align(when (position) {
                    HighlightPosition.TOP -> Alignment.TopCenter
                    HighlightPosition.BOTTOM -> Alignment.BottomCenter
                    HighlightPosition.START -> Alignment.CenterStart
                    HighlightPosition.END -> Alignment.CenterEnd
                    HighlightPosition.POINT -> Alignment.Center
                }),
                checked = highlightPositions.contains(position),
                onCheckedChange = { addOrRemovePosition(it, position) }
            )
        }

        HighlightPosition.values().forEach {
            HighlightPositionCheckbox(position = it)
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun LineChartComp() {

    val settingsSheetState = rememberBottomSheetScaffoldState()

    var points by rememberSaveable { mutableStateOf(randomFancyDataSet()) }
    var xRange by remember { mutableStateOf(15f) }
    var yRange by remember { mutableStateOf(15f) }
    var highlightPositions by remember { mutableStateOf(arrayOf(HighlightPosition.TOP)) }

    val axisColor = MaterialTheme.colors.onBackground
    val chartProperties = LineChartProperties(
        lineStyle = LineDrawStyle(color = MaterialTheme.colors.primary),
        pointStyle = PointDrawStyle(color = MaterialTheme.colors.primary),
        textStyle = textStyle.copy(color = MaterialTheme.colors.primary),
        highlightPointStyle = PointDrawStyle(
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f), radius = 6.dp
        ),
        highlightTextStyle = textStyle.copy(
            color = MaterialTheme.colors.primary,
            textAlign = Paint.Align.CENTER,
            offsetY = -25f
        ),
        chartPaddingValues = PaddingValues(44.dp),
        dataXRange = -xRange..xRange,
        dataYRange = -yRange..yRange,
        axes = setOf(
            Axes.xBottom.apply { style.setColor(axisColor) },
            Axes.xTop.apply { style.setColor(axisColor) },
            Axes.yLeft.apply { style.setColor(axisColor) },
            Axes.yRight.apply { style.setColor(axisColor) },
        ),
    )

    chartProperties.setAxisTypeface(textStyle.typeface)

    BottomSheetScaffold(
        scaffoldState = settingsSheetState,
        sheetPeekHeight = 60.dp,
        sheetContent = {
            LineChartSettings(
                xRange = xRange,
                yRange = yRange,
                highlightPositions = highlightPositions,
                onChangeDataset = { points = it },
                onXRangeChanged = { xRange = it },
                onYRangeChanged = { yRange = it },
                onHighlightPositionsChanged = { highlightPositions = it }
            )
        }
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = DemoContent.LINE.chartName,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f),
                dataset = points,
                properties = chartProperties,
                highlightPositions = highlightPositions,
                highlightContent = {
                    HighlightPopup {
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.Center),
                            text = "y -> ${it.y.toInt()}\nx -> ${it.x.toInt()}",
                            style = MaterialTheme.typography.body2,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun AnimatedFunctionChart() {
    var animate by rememberSaveable { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition()
    val m by infiniteTransition.animateFloat(
        initialValue = -PI.toFloat(),
        targetValue = PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Column {
        Surface(Modifier.weight(1f), color = MaterialTheme.colors.background) {
            FunctionPlotter(
                pointSpacing = 5,
                lineStyle = LineDrawStyle(color = MaterialTheme.colors.primary),
                textStyle = textStyle.copy(color = MaterialTheme.colors.primary),
                dataXRange = -PI.toFloat() + (if (animate) m else 0f)..PI.toFloat() + (if (animate) m else 0f),
                dataYRange = -2f..2f
            ) {
                sin(it)
            }
        }
        Row(
            Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Switch(checked = animate, onCheckedChange = { animate = it })
            Text(text = "Animate", Modifier.padding(start = 8.dp))
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KonstellationTheme {
        Content()
    }
}
