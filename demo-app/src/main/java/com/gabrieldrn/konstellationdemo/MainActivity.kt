package com.gabrieldrn.konstellationdemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemo
import com.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemoViewModel
import com.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val injector = object : KoinComponent {}
private val mainTextStyle by injector.inject<TextDrawStyle>(QF_MAIN_TEXT_STYLE)

private const val DarkIconsLuminanceThreshold = 0.5f

class MainActivity : AppCompatActivity() {

    private val lineChartViewModel by viewModels<LineChartDemoViewModel>()

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

                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(color = MaterialTheme.colorScheme.background)
                        .systemBarsPadding(),
                ) {
                    Text(
                        text = "Konstellation",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp),
                        text = "Line chart",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    LineChartDemo(lineChartViewModel)
                }
            }
        }
    }
}
