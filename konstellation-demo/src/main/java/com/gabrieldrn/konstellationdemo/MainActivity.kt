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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.gabrieldrn.konstellation.core.plotting.*
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
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

@ExperimentalComposeUiApi
@Composable
fun Content() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Konstellation demo")
                },
                elevation = 0.dp
            )
        }
    ) {
        Column {
            Row(Modifier.weight(1f)) {
                LineChart()
            }
            Row(Modifier.weight(1f)) {
                AnimatedFunctionChart()
//                FunctionChart()
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun LineChart() {
    Surface(color = MaterialTheme.colors.background) {
        val points = randomDataSet()
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
//            dataXRange = 0f..40f,
//            dataYRange = 0f..40f,
            axes = setOf(ChartAxis.XBottom(), ChartAxis.XTop(), ChartAxis.YLeft(), ChartAxis.YRight())
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
