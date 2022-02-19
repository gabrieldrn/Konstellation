package com.gabrieldrn.konstellationdemo.linechartdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.gabrieldrn.konstellation.highlighting.HighlightContentPosition
import com.google.accompanist.insets.navigationBarsHeight

@Composable
fun ColumnScope.LineChartSettingsContent(viewModel: LineChartDemoViewModel) {
    Row {
        // TODO Use DrawerState to animate this arrow
        Icon(
            Icons.Rounded.ArrowUpward,
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
    LineChartSettingHeader("Highlight popup positions")
    Box(
        Modifier
            .height(156.dp)
            .aspectRatio(1f)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .align(Alignment.CenterHorizontally)
    ) {
        @Composable
        fun Checkbox(contentPosition: HighlightContentPosition) = Checkbox(
            modifier = Modifier.align(
                when (contentPosition) {
                    HighlightContentPosition.TOP -> Alignment.TopCenter
                    HighlightContentPosition.BOTTOM -> Alignment.BottomCenter
                    HighlightContentPosition.START -> Alignment.CenterStart
                    HighlightContentPosition.END -> Alignment.CenterEnd
                    HighlightContentPosition.POINT -> Alignment.Center
                }
            ),
            checked = viewModel.properties.highlightContentPositions.contains(contentPosition),
            onCheckedChange = { checked ->
                if (checked) viewModel.addHighlightPosition(contentPosition)
                else viewModel.removeHighlightPosition(contentPosition)
            },
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
        )

        HighlightContentPosition.values().forEach {
            Checkbox(it)
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

@Preview(showBackground = true)
@Composable
fun LineChartSettingsPreview() {
    Column {
        LineChartSettingsContent(
            viewModel = LineChartDemoViewModel()
        )
    }
}
