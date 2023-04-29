package dev.gabrieldrn.konstellationdemo.functionchartdemo

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.gabrieldrn.konstellation.charts.function.WipChartApi
import dev.gabrieldrn.konstellation.charts.function.FunctionPlotter
import dev.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import kotlin.math.PI
import kotlin.math.sin

/**
 * Demo of the FunctionPlotter composable from the library.
 */
@OptIn(WipChartApi::class)
@Composable
@Suppress("MagicNumber")
fun AnimatedFunctionChart() {
    var animate by rememberSaveable { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "func_transition")
    val m by infiniteTransition.animateFloat(
        initialValue = -PI.toFloat(),
        targetValue = PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "func_transition_anim"
    )
    val dataXRange by remember {
        derivedStateOf {
            -PI.toFloat() + (if (animate) m else 0f)..PI.toFloat() + if (animate) m else 0f
        }
    }
    Column {
        Surface(Modifier.weight(1f)) {
            FunctionPlotter(
                pointSpacing = 5,
                lineStyle = LineDrawStyle(color = MaterialTheme.colorScheme.primary),
//                textStyle = mainTextStyle.copy(color = MaterialTheme.colorScheme.primary),
                dataXRange = dataXRange,
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
