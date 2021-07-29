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
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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

private val points = datasetOf(
    -10f by -10f,
    -5f by 0f,
    0f by 30f,
    5f by 30f,
    10f by 25f,
    15f by 35f,
    20f by 5f,
    25f by -5f,
    30f by 0f,
)

private val points2 = datasetOf(
    10f by 10f,
    20f by 20f,
    30f by 30f,
)

class MainActivity : AppCompatActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ResourcesCompat.getFont(this, R.font.space_mono_regular)?.let {
            textStyle = textStyle.copy(typeface = it)
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
            content = { Text(it.chartName) }
        )
    }

    ModalDrawer(
        drawerState = drawerState,
        //Gestures avoid highlighting of values in charts
        gesturesEnabled = false,
        drawerContent = {
            Column(Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "Konstellation demo",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    onClick = { scope.launch { drawerState.close() } },
                    content = { Text("Close Drawer") }
                )
                DemoContent.values().forEach { drawerChartButtonFactory(it) }
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
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(Modifier.weight(1f)) {
                        when (contentState) {
                            DemoContent.LINE -> LineChart()
                            DemoContent.FUNCTION -> AnimatedFunctionChart()
                        }
                    }
                }
            }
        }
    )
}

@ExperimentalComposeUiApi
@Composable
fun LineChart() {
    Surface(color = MaterialTheme.colors.background) {
        val points = randomDataSet()
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
            dataXRange = -15f..15f,
            dataYRange = -15f..15f,
            axes = setOf(
                xBottom.apply { style.setColor(axisColor) },
                xTop.apply { style.setColor(axisColor) },
                yLeft.apply { style.setColor(axisColor) },
                yRight.apply { style.setColor(axisColor) },
            ),
        )

        LinePlotter(
            dataSet = points,
            properties = chartProperties
        )
    }
}

@Composable
fun FunctionChart() {
    Surface(color = MaterialTheme.colors.background) {
        FunctionPlotter(
            pointSpacing = 5,
            lineStyle = LineDrawStyle(color = MaterialTheme.colors.primary),
            textStyle = textStyle.copy(color = MaterialTheme.colors.primary),
            dataXRange = -PI.toFloat()..PI.toFloat(),
            dataYRange = -2f..2f
        ) {
            sin(it)
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

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KonstellationTheme {
        Content()
    }
}
