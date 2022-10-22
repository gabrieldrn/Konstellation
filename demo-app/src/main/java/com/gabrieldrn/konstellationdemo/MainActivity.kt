package com.gabrieldrn.konstellationdemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import com.gabrieldrn.konstellation.charts.function.FunctionPlotter
import com.gabrieldrn.konstellation.configuration.styles.LineDrawStyle
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellationdemo.linechartdemo.LineChartComposable
import com.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemoViewModel
import com.gabrieldrn.konstellationdemo.linechartdemo.getChartProperties
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.math.PI
import kotlin.math.sin

private val injector = object : KoinComponent {}
private val mainTextStyle by injector.inject<TextDrawStyle>(QF_MAIN_TEXT_STYLE)

private const val DarkIconsLuminanceThreshold = 0.5f

class MainActivity : AppCompatActivity() {

    private val lineChartViewModel by viewModel<LineChartDemoViewModel> {
        parametersOf(getChartProperties())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lineChartViewModel // Trigger instantiation.

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ResourcesCompat.getFont(this, R.font.figtree_medium)?.let {
            mainTextStyle.typeface = it
        }

        setContent {
            KonstellationTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons =
                    MaterialTheme.colorScheme.background.luminance() > DarkIconsLuminanceThreshold
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }
                LineChartComposable()
            }
        }
    }
}

@Composable
@Suppress("MagicNumber")
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
        Surface(Modifier.weight(1f)) {
            FunctionPlotter(
                pointSpacing = 5,
                lineStyle = LineDrawStyle(color = MaterialTheme.colorScheme.primary),
                textStyle = mainTextStyle.copy(color = MaterialTheme.colorScheme.primary),
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KonstellationTheme {

    }
}
