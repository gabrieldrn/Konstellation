package dev.gabrieldrn.konstellationdemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemo
import dev.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemoViewModel
import dev.gabrieldrn.konstellationdemo.linechartdemo.getDemoChartProperties
import dev.gabrieldrn.konstellationdemo.ui.isLandscape
import dev.gabrieldrn.konstellationdemo.ui.theme.KonstellationTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val DarkIconsLuminanceThreshold = 0.5f

/**
 * Demo app main activity.
 */
class MainActivity : AppCompatActivity() {

    private val lineChartViewModel by viewModel<LineChartDemoViewModel> {
        parametersOf(getDemoChartProperties())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            @Composable
            fun Logo(modifier: Modifier = Modifier) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primary)
                        .statusBarsPadding()
                        .padding(start = 16.dp)
                        .then(modifier)
                ) {
                    Text(
                        modifier = modifier,
                        text = "KONSTELLATION",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            @Composable
            fun Title(modifier: Modifier = Modifier) {
                Text(
                    modifier = modifier.padding(start = 16.dp, top = 16.dp),
                    text = "Line chart".uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            KonstellationTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons =
                    MaterialTheme.colorScheme.primary.luminance() > DarkIconsLuminanceThreshold

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
                        .navigationBarsPadding(),
                ) {
                    if (LocalConfiguration.current.isLandscape) {
                        Logo()
                        Title()
                    } else {
                        Logo(modifier = Modifier.align(Alignment.CenterHorizontally))
                        Title()
                    }

                    LineChartDemo(
                        viewModel = lineChartViewModel,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
