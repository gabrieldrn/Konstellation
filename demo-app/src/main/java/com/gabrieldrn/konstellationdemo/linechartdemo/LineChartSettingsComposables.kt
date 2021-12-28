package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.highlighting.HighlightPosition
import com.google.accompanist.insets.navigationBarsHeight

@Composable
fun ColumnScope.LineChartSettingsContent(viewModel: LineChartDemoViewModel) {
    Row {
        Icon(
            Icons.Rounded.ArrowDownward,
            null,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
        Text(
            modifier = Modifier
                .padding(start = 34.dp)
                .padding(vertical = 16.dp)
                .align(Alignment.CenterVertically),
            text = "Settings",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
    }
    LineChartDatasetSelector(viewModel)
    LineChartHighlightSetting(viewModel)
    Spacer(
        Modifier
            .navigationBarsHeight()
            .fillMaxWidth()
    )
}

@Composable
private fun LineChartDatasetSelector(viewModel: LineChartDemoViewModel) {
    LineChartSettingHeader("Datasets")
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            onClick = { viewModel.generateNewRandomDataset() }
        ) {
            Text(text = "NEW RANDOM", textAlign = TextAlign.Center)
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            onClick = { viewModel.generateNewFancyDataset() }
        ) {
            Text(text = "NEW FANCY", textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ColumnScope.LineChartHighlightSetting(viewModel: LineChartDemoViewModel) {
    LineChartSettingHeader("Highlight positions")
    Box(
        Modifier
            .height(156.dp)
            .aspectRatio(1f)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .align(Alignment.CenterHorizontally)
    ) {

        val highlightPositionCheckbox: @Composable (position: HighlightPosition) -> Unit = {
            Checkbox(
                modifier = Modifier.align(
                    when (it) {
                        HighlightPosition.TOP -> Alignment.TopCenter
                        HighlightPosition.BOTTOM -> Alignment.BottomCenter
                        HighlightPosition.START -> Alignment.CenterStart
                        HighlightPosition.END -> Alignment.CenterEnd
                        HighlightPosition.POINT -> Alignment.Center
                    }
                ),
                checked = viewModel.properties.highlightPositions.contains(it),
                onCheckedChange = { checked ->
                    if (checked) viewModel.addHighlightPosition(it)
                    else viewModel.removeHighlightPosition(it)
                },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
            )
        }

        HighlightPosition.values().forEach {
            highlightPositionCheckbox(it)
        }
    }
}

@Composable
private fun LineChartSettingHeader(title: String) {
    Row(Modifier.padding(top = 8.dp)) {
        Icon(
            Icons.Rounded.ArrowRight,
            null,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 16.dp)
        )
        Text(modifier = Modifier.align(Alignment.CenterVertically), text = title)
    }
}

@Preview
@Composable
fun LineChartSettingsPreview() {
    Column(Modifier.background(Color.White)) {
        LineChartSettingsContent(
            viewModel = LineChartDemoViewModel()
        )
    }
}
