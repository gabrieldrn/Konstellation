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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.style.*
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

private var textStyle = TextDrawStyle()

class MainActivity : AppCompatActivity() {
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

@ExperimentalComposeUiApi
@Composable
fun Content() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var contentState by rememberSaveable { mutableStateOf(DemoContent.values().first()) }
    val scope = rememberCoroutineScope()
    // Lambda creating
    val drawerChartButtonFactory: @Composable ColumnScope.(chart: DemoContent) -> Unit = {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
            onClick = {
                scope.launch {
                    contentState = it
                    drawerState.close()
                }
            },
            content = { Text(it.chartName.uppercase()) }
        )
    }

    ModalDrawer(
        drawerState = drawerState,
        //Gestures avoid highlighting of values in charts
        gesturesEnabled = false,
        drawerContent = {
            Column(Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "DEMO APP",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
                DemoContent.values().forEach { drawerChartButtonFactory(it) }
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    onClick = { scope.launch { drawerState.close() } },
                    content = { Text("CLOSE DRAWER") }
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = contentState.chartName)
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = null)
                            }
                        }
                    )
                }
            ) {
                Box(Modifier.fillMaxSize()) {
                    when (contentState) {
                        DemoContent.LINE -> LineChart()
                        DemoContent.FUNCTION -> AnimatedFunctionChart()
                    }
                }
            }
        }
    )
}

@ExperimentalComposeUiApi
@Composable
fun LineChart() {
    var points by rememberSaveable { mutableStateOf(randomDataSet()) }
    var xRange by remember { mutableStateOf(15f) }
    var yRange by remember { mutableStateOf(15f) }
    val axisColor = MaterialTheme.colors.onBackground
    val chartProperties = LineChartProperties(
        lineStyle = LineDrawStyle(color = MaterialTheme.colors.primary),
        pointStyle = PointDrawStyle(color = MaterialTheme.colors.primary),
        textStyle = textStyle.copy(color = MaterialTheme.colors.primary),
        highlightPointStyle = PointDrawStyle(
            color = MaterialTheme.colors.primary, radius = 6.dp
        ),
        highlightTextStyle = textStyle.copy(
            color = MaterialTheme.colors.primary,
            textAlign = Paint.Align.CENTER,
            offsetY = -25f
        ),
        dataXRange = -xRange..xRange,
        dataYRange = -yRange..yRange,
        axes = setOf(
            xBottom.apply { style.setColor(axisColor) },
            xTop.apply { style.setColor(axisColor) },
            yLeft.apply { style.setColor(axisColor) },
            yRight.apply { style.setColor(axisColor) },
        ),
    )

    chartProperties.setAxisTypeface(textStyle.typeface)

    Column {
        Row {
            Surface(color = MaterialTheme.colors.background) {
                LinePlotter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.5f),
                    dataSet = points,
                    properties = chartProperties
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = "X range"
        )
        Slider(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = xRange,
            valueRange = 1f..25f,
            onValueChange = { xRange = it })
        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = "Y range"
        )
        Slider(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = yRange,
            valueRange = 1f..25f,
            onValueChange = { yRange = it })
        Button(modifier = Modifier
            .padding(top = 16.dp)
            .align(Alignment.CenterHorizontally),
            onClick = { points = randomDataSet() }, content = {
                Text(text = "NEW DATASET")
            })
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

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KonstellationTheme {
        Content()
    }
}
