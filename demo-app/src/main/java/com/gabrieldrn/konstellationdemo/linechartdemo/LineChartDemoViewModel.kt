package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.gabrieldrn.konstellation.charts.line.configuration.LineChartProperties
import com.gabrieldrn.konstellation.plotting.Dataset
import com.gabrieldrn.konstellation.plotting.datasetOf
import com.gabrieldrn.konstellation.util.randomDataSet
import com.gabrieldrn.konstellation.util.randomFancyDataSet
import kotlin.reflect.KProperty1
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions

class LineChartDemoViewModel(
    properties: LineChartProperties = getChartProperties()
) : ViewModel() {

    var dataset: Dataset by mutableStateOf(datasetOf())
        private set
    var properties: LineChartProperties by mutableStateOf(properties)
        private set

    init {
        generateNewFancyDataset()
    }

    fun generateNewFancyDataset() {
        dataset = randomFancyDataSet()
    }

    fun generateNewRandomDataset() {
        dataset = randomDataSet()
    }

    fun <T> updateProperty(property: KProperty1<out Any, T>, newValue: T) {
        val copy = properties::class.memberFunctions.first { it.name == "copy" }
        val instanceParam = copy.instanceParameter
            ?: error("LineChartProperties.copy is not a member of this class.")
        val propertyParam = copy.parameters.firstOrNull { it.name == property.name }
            ?: error("${property.name} is not a property of ${properties::class.simpleName}")
        properties = copy.callBy(
            mapOf(instanceParam to properties, propertyParam to newValue)
        ) as LineChartProperties
    }
}
