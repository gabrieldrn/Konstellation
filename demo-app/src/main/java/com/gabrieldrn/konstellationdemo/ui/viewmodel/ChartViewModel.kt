package com.gabrieldrn.konstellationdemo.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.datasetOf

abstract class ChartViewModel : ViewModel() {

    var dataset: Dataset by mutableStateOf(datasetOf())
        protected set

}
