package com.gabrieldrn.konstellationdemo

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gabrieldrn.konstellation.core.plotting.KonstellationCanvas
import com.gabrieldrn.konstellation.core.plotting.Vertex
import com.gabrieldrn.konstellation.style.LineDrawStyle
import com.gabrieldrn.konstellation.style.TextDrawStyle
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import kotlin.math.PI
import kotlin.math.sin

private var textStyle = TextDrawStyle()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textStyle = TextDrawStyle(
                resources.getFont(R.font.space_mono_regular)
            )
        }
        setContent {
            KonstellationTheme {
                Content()
            }
        }
    }
}

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
        var points by rememberSaveable { mutableStateOf(randomDataSet()) }
        var precision by rememberSaveable { mutableStateOf(1f) }
        //ChartContent(points = points) { points = it }
        FunctionChartContent(precision = precision) { precision = it }
    }
}

@Composable
fun ChartContent(points: Array<Vertex>, onDataSetChange: (Array<Vertex>) -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.weight(1f)) {
                KonstellationCanvas(points)
            }
            Row(Modifier.padding(8.dp)) {
                Button(onClick = {
                    onDataSetChange(randomDataSet())
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "NEW DATASET")
                }
            }
        }
    }
}

@Composable
fun FunctionChartContent(precision: Float, onPrecisionChange: (Float) -> Unit) {
    val primary = MaterialTheme.colors.primary
    Surface(color = MaterialTheme.colors.background) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.weight(1f)) {
                KonstellationCanvas(
                    precision = precision.toInt(),
                    drawStyle = LineDrawStyle(color = primary),
                    textStyle = textStyle.copy(color = primary),
                    xRange = -PI.toFloat()..PI.toFloat(),
                    function = { sin(it) }
                )
            }
            Row(Modifier.padding(8.dp)) {
                Column {
//                    Text(text = "Precision: $precision")
//                    Slider(
//                        value = precision,
//                        onValueChange = onPrecisionChange,
//                        steps = 5,
//                        valueRange = 5f..100f
//                    )
                }
            }
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
