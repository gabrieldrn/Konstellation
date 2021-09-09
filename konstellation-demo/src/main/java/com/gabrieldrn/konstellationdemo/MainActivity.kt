package com.gabrieldrn.konstellationdemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import com.gabrieldrn.konstellation.charts.function.*
import com.gabrieldrn.konstellation.style.*
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

private var mainTextStyle = TextDrawStyle()

class MainActivity : AppCompatActivity() {
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ResourcesCompat.getFont(this, R.font.manrope_medium)?.let {
            mainTextStyle.typeface = it
        }

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = isSystemInDarkTheme()
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }
            KonstellationTheme {
                ProvideWindowInsets {
                    Content()
                }
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
    val insetsPaddingValues = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars)

    LaunchedEffect(scaffoldState) {
        scaffoldState.conceal()
    }
    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                title = {
                    Text(text = "Konstellation", fontWeight = FontWeight.Bold)
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
                backgroundColor = Color.Transparent,
                contentPadding = insetsPaddingValues
            )
        },
        peekHeight = BackdropScaffoldDefaults.PeekHeight + insetsPaddingValues.calculateTopPadding(),
        backLayerContent = {
            LazyColumn {
                items(DemoContent.values()) { item ->
                    ListItem(
                        modifier = Modifier.clickable {
                            contentSelection = item
                            scope.launch { scaffoldState.conceal() }
                        },
                        text = {
                            Text(
                                text = item.chartName,
                                fontWeight = if (contentSelection == item) FontWeight.Bold else null
                            )
                        },
                    )
                }
            }
        }, frontLayerContent = {
            Box(Modifier.fillMaxSize()) {
                when (contentSelection) {
                    DemoContent.LINE -> LineChartComp(mainTextStyle)
                    DemoContent.FUNCTION -> AnimatedFunctionChart()
                }
            }
        })
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
                textStyle = mainTextStyle.copy(color = MaterialTheme.colors.primary),
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
