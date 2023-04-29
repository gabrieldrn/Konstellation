package dev.gabrieldrn.konstellationdemo

import androidx.core.content.res.ResourcesCompat
import dev.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import dev.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Qualifier for the main text style of the line chart demo.
 */
val QF_MAIN_TEXT_STYLE = named("MAIN_TEXT_STYLE")

/**
 * Koin module for the demo app.
 */
val appModule = module {
    viewModel { parameters -> LineChartDemoViewModel(parameters.get()) }
    single(QF_MAIN_TEXT_STYLE) {
        TextDrawStyle(
            typeface = ResourcesCompat.getFont(androidContext(), R.font.figtree_medium)!!
        )
    }
}
