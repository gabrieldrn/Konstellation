package com.gabrieldrn.konstellationdemo

import com.gabrieldrn.konstellationdemo.linechartdemo.LineChartDemoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LineChartDemoViewModel() }
}
