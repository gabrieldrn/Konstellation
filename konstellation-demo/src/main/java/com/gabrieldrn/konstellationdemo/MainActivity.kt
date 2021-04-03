package com.gabrieldrn.konstellationdemo

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.gabrieldrn.konstellation.core.plotting.LinePlotter
import com.gabrieldrn.konstellation.core.plotting.FunctionPlotter
import com.gabrieldrn.konstellation.core.plotting.by
import com.gabrieldrn.konstellation.core.plotting.datasetOf
import com.gabrieldrn.konstellation.core.plotting.yMax
import com.gabrieldrn.konstellation.core.plotting.yMin
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.PointDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import kotlin.math.PI
import kotlin.math.sin

private var textStyle = TextDrawStyle()

class MainActivity : AppCompatActivity() {
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
                FunctionChart()
            }
        }
    }
}

@Composable
fun LineChart() {
    Surface(color = MaterialTheme.colors.background) {
        LinePlotter(
            dataSet = points,
            lineStyle = LineDrawStyle(color = MaterialTheme.colors.primary),
            pointStyle = PointDrawStyle(color = MaterialTheme.colors.primary),
            textStyle = textStyle.copy(color = MaterialTheme.colors.primary),
            dataYRange = points.yMin - 10f..points.yMax + 10f
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
    val infiniteTransition = rememberInfiniteTransition()
    val m by infiniteTransition.animateFloat(
        initialValue = -PI.toFloat(),
        targetValue = PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Surface(color = MaterialTheme.colors.background) {
        FunctionPlotter(
            pointSpacing = 5,
            lineStyle = LineDrawStyle(color = MaterialTheme.colors.primary),
            textStyle = textStyle.copy(color = MaterialTheme.colors.primary),
            dataXRange = -PI.toFloat() + m..PI.toFloat() + m,
            dataYRange = -2f..2f
        ) {
            sin(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KonstellationTheme {
        Content()
    }
}
