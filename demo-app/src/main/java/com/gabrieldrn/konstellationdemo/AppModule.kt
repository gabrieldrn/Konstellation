package com.gabrieldrn.konstellationdemo

import com.gabrieldrn.konstellation.configuration.styles.TextDrawStyle
import com.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val QF_MAIN_TEXT_STYLE = named("MAIN_TEXT_STYLE")

val appModule = module {
    viewModel { parameters -> LineChartDemoViewModel(parameters.get()) }
    single(QF_MAIN_TEXT_STYLE) { TextDrawStyle() }
}
